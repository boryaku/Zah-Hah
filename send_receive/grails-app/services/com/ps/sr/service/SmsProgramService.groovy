package com.ps.sr.service

import com.twilio.sdk.resource.factory.SmsFactory
import com.twilio.sdk.resource.instance.Account
import com.twilio.sdk.TwilioRestClient
import com.ps.sr.model.LevelItem

class SmsProgramService extends ProgramService{

    static transactional = true

    def send (LevelItem levelItem, String phoneNumber, String languageCode) {
        // Create a rest client
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Get the main account (The one we used to authenticate the client)
        Account mainAccount = client.getAccount();

        // Send an sms
        SmsFactory smsFactory = mainAccount.getSmsFactory();
        Map<String, String> smsParams = new HashMap<String, String>();
        smsParams.put("To", phoneNumber)
        smsParams.put("From", FROM)
        smsParams.put("Body", getMessage(levelItem.text, [].toArray(), new Locale(languageCode)))
        smsFactory.create(smsParams)
    }
}
