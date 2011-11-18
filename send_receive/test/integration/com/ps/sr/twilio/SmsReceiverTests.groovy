package com.ps.sr.twilio

import com.ps.sr.model.Subscription
import com.ps.sr.model.Level
import com.ps.sr.model.Program
import com.ps.sr.model.LevelItem
import com.ps.sr.message.SmsReceiverController
import com.ps.sr.model.ProgramSession
import com.ps.sr.model.LevelItemResponse
import com.ps.sr.model.ResponseStatus

/**
 *
 * Author: cnbuckley
 * Date: 11/16/11
 * Time: 11:01 AM
 *
 * Test the Receiver functionality
 */
class SmsReceiverTests extends GroovyTestCase{

    def smsProgramService
    def bootStrapService
    def phone = "5037090096"

    protected void setUp() {
        bootStrapService.initData()
    }

    protected void tearDown() {
        super.tearDown()
    }

    def testReceiveHello(){

        assert Program.count() == 1
        assert Level.count() == 3
        assert LevelItem.count() ==  14
        assert Subscription.count() == 1

        def receiver = new SmsReceiverController()
        receiver.smsProgramService = smsProgramService
        receiver.params.From = phone
        receiver.params.Body = "hello"

        receiver.index()

        //we should assert that we have one Session and one pending LineItemResponse

        assert ProgramSession.count() == 1
        assert LevelItemResponse.count() == 1

        def subscription = Subscription.findByPhoneNumber(phone)

        assert subscription.phoneNumber ==  phone
        assert subscription.programSessions.get(0).levelItemResponses.get(0).responseStatus == ResponseStatus.WAITING
        assert subscription.programSessions.get(0).levelItemResponses.get(0).expectedResponseBody == "Blue"
    }

    /**
     *
     * In this test we will create a hello message that will send a SMS with the first item of the subscription's
     * first level's item.
     *
     */
    def testReceiveBlueWaitingForBall(){

        //send Blue (this is the first item)
        def receiver = new SmsReceiverController()
        receiver.smsProgramService = smsProgramService
        receiver.params.From = phone
        receiver.params.Body = "Blue"

        receiver.index()

        //we should assert that we have one Session and one pending LineItemResponse and one correct item
        assert ProgramSession.count() == 1
        assert LevelItemResponse.count() == 2

        def subscription = Subscription.findByPhoneNumber(phone)

        //Note: hibernate maintains the list's order so we can test it like this...
        assert subscription.programSessions.get(0).levelItemResponses.get(0).responseStatus == ResponseStatus.RECIEVED_CORRECT
        assert subscription.programSessions.get(0).levelItemResponses.get(0).expectedResponseBody == "Blue"
        assert subscription.programSessions.get(0).levelItemResponses.get(0).responseTime > 0

        assert subscription.programSessions.get(0).levelItemResponses.get(1).responseStatus == ResponseStatus.WAITING
        assert subscription.programSessions.get(0).levelItemResponses.get(1).expectedResponseBody == "Ball"
    }

    def testReceiveByeWaitingForBall(){

        //send Blue (this is the first item)
        def receiver = new SmsReceiverController()
        receiver.smsProgramService = smsProgramService
        receiver.params.From = phone
        receiver.params.Body = "Bye"

        receiver.index()

        //we still only have 1 session and 2 responses
        assert ProgramSession.count() == 1
        assert LevelItemResponse.count() == 2

        def subscription = Subscription.findByPhoneNumber(phone)

        //The session duration should be > 0
        assert subscription.programSessions.get(0).duration > 0

    }

    def testHelloWhileWaitingForBall(){

        //send hello again
        def receiver = new SmsReceiverController()
        receiver.smsProgramService = smsProgramService
        receiver.params.From = phone
        receiver.params.Body = "hello"

        receiver.index()

        def subscription = Subscription.findByPhoneNumber(phone)
        //we should assert that we have 2 Sessions now and we should be getting the "Ball" message back
        assert ProgramSession.count() == 2
        assert subscription.lastSession.levelItemResponses.size() == 1
        assert subscription.programSessions.get(1).levelItemResponses.get(0).responseStatus == ResponseStatus.WAITING
        assert subscription.programSessions.get(1).levelItemResponses.get(0).expectedResponseBody == "Ball"
    }

}
