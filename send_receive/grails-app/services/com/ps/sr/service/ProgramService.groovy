package com.ps.sr.service

import com.ps.sr.model.Subscription
import com.ps.sr.model.ProgramSession
import com.twilio.sdk.TwilioRestClient
import com.twilio.sdk.resource.instance.Account
import com.twilio.sdk.resource.factory.SmsFactory
import org.apache.commons.logging.LogFactory
import com.ps.sr.model.LevelItem
import com.ps.sr.model.LevelItemResponse
import com.ps.sr.model.ResponseStatus
import com.ps.sr.model.Level

/**
 * This Service is the Brains.  It needs to determine the open session for the phoneNumber(user) and which program
 * they are interacting with.  The "BrainService" also will need to understand which level the user is on and which
 * message to send.
 *
 * <todo>
 * In future releases the Brain will understand which phonics the user struggles with and will prescribe additional
 * messages that correlate with that phonic.
 * </todo>
 */
abstract class ProgramService {

    static transactional = true

    private static final log = LogFactory.getLog(this)

    def messageSource

    def ACCOUNT_SID = "AC4a96626e76164b2ba24a708d956f45df"
    def AUTH_TOKEN = "22ad3656de217ebe17b8308ac236c61b"
    def FROM = "+1 415-599-2671"

    /**
     * Interact with the user. Users start sessions by saying "hello" and end by saying "bye" otherwise we are responding.
     * If there is no subscription then we will do nothing right now.  When a new message is received we create a LevelItemResponse
     * object and wait for a response back from the same number and perform analysis on the message that was received.
     * Right now we simply mark it correct or not.  If it's not correct we mark it and go on to the next item.
     * @param phoneNumber
     * @param body
     * @param phoneCity
     * @param phoneState
     * @param phoneZip
     * @param phoneCountry
     * @return
     */
    def interactAction(String phoneNumber, String body, String phoneCity, String phoneState, String phoneZip,
                                                                                                String phoneCountry) {
        def now = new Date()

        //we'll get the subscription so we know what items we can pull from
        Subscription subscription = Subscription.findByPhoneNumberAndActive(phoneNumber, true)

        //if there is a no subscription then they haven't subscribed.
        if(subscription == null){
           //if we send a message back we need to store the user's phone so we don't spam someone...
           //so for now we won't respond saying "sign up it's free..." we'll just return and do nothing...
           return
        }

        //Are we starting a session?
        if(body.toLowerCase() == "hello"){
             //handle the hello case
             handleHello subscription, now, phoneNumber

        } else if(body.toLowerCase() == "bye"){
             //calculate the duration and clean up any waiting items

            def lastSession = subscription.lastSession
            lastSession.endTime = now
            lastSession.duration =  now.time - lastSession.startTime.time

        }else{
            //compare what we sent with what we got, then add data about what we got...
            def lastItemResponse = subscription.lastSession.lastItemResponse

            if(lastItemResponse.expectedResponseBody == body){
                lastItemResponse.responseStatus = ResponseStatus.RECIEVED_CORRECT
            } else{
                lastItemResponse.responseStatus = ResponseStatus.RECEIVED_INCORRECT
            }

            lastItemResponse.responseReceivedDate = now
            lastItemResponse.responseTime = now.time - lastItemResponse.itemSentDate.time
            lastItemResponse.responseBody = body

            handleNext subscription, now, phoneNumber
        }
        //TODO we need a clean up routine that finds orphaned sessions we need to pop off the last response
    }

    /**
     * In future releases we will do more figuring out here, ie. what phonic are involved and start calculating
     * issues in the user's speaking abilities and prescribe other words or phrases to help them.
     * @param subscription
     * @param now
     * @param phoneNumber
     * @return
     */
    def handleNext(Subscription subscription, Date now, String phoneNumber){
        //select the item based on the potential historical interactions
        def levelItem = levelItemSelection(subscription)

        def levelItemResponse =
                        new LevelItemResponse(levelItem: levelItem,
                                expectedResponseBody: getMessage(levelItem.text, [].toArray(), new Locale(subscription.program.languageCode)),
                                itemSentDate: now,
                                responseStatus: ResponseStatus.WAITING)

        def session = subscription.lastSession
        session.addToLevelItemResponses(levelItemResponse).save(flush: true, insert: true)

        //send the text...
        send(levelItem, phoneNumber, subscription.program.languageCode)
    }

    /**
     * The hello message begins a new session for the user.
     * @param subscription
     * @param now
     * @param phoneNumber
     * @return
     */
    def handleHello(Subscription subscription, Date now, String phoneNumber){
        //select the item based on the potential historical interactions
        def levelItem
        def lastSession = subscription.lastSession

        if(lastSession != null && lastSession.lastItemResponse.responseStatus == ResponseStatus.WAITING){
            levelItem = subscription.lastSession.lastItemResponse.levelItem
        }else{
            levelItem = levelItemSelection(subscription)
        }

        def levelItemResponse =
                        new LevelItemResponse(levelItem: levelItem,
                                expectedResponseBody: getMessage(levelItem.text, [].toArray(), new Locale(subscription.program.languageCode)),
                                itemSentDate: now,
                                responseStatus: ResponseStatus.WAITING).save(flush: true)

        def session = new ProgramSession(startTime: now, lastItemResponse: levelItemResponse)
        session.addToLevelItemResponses(levelItemResponse).save(flush: true, insert: true)

        subscription.lastSession = session
        subscription.addToProgramSessions(session).save(flush: true)
        //send the text...
        send(levelItem, phoneNumber, subscription.program.languageCode)
    }

    def levelItemSelection(Subscription subscription){
        def levelItem

        //if we have no last session then we are just starting off
        if(subscription.lastSession == null){
            levelItem = subscription.program.levels[0].levelItems[0]
        }else{
            //did the user leave a response item waiting?
            def lastItem = subscription.lastSession.lastItemResponse.levelItem
            def indexOf = lastItem.level.levelItems.indexOf(lastItem)

            try{
               //we'll try to get the next item in the list but, if there is no more in the list...
               def nextIndex = indexOf + 1
               levelItem = lastItem.level.levelItems.get(nextIndex)
            }catch (e){
               //then we'll get the next level
               def currentLevelIndexOf = subscription.program.levels.indexOf(lastItem.level)
               def nextLevel = subscription.program.levels.get(currentLevelIndexOf+1)

               levelItem = nextLevel.levelItems[0]
            } finally{
               //no more items or levels, did we finish the Program?
               assert levelItem != null
               assert levelItem != lastItem
            }
        }

        return levelItem
    }

    def abstract send (LevelItem levelItem, String phoneNumber, String languageCode)

    def getMessage(String key, Object[] params, Locale locale){
       return messageSource.getMessage(key, params, locale);
    }
}

