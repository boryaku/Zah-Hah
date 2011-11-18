package com.ps.sr.message

class SmsReceiverController {

    def smsProgramService

    /**
     * This is our Twilio Configured URL for SMS
     *
     * This is our SMS Message Receiver and Action Handler...
     *
     */
    def index = {
        def from = params.From
        def body = params.Body

        def fromCity = params.FromCity
        def fromState = params.FromState
        def fromZip = params.FromZip
        def fromCountry = params.FromCountry

        smsProgramService.interactAction(from, body, fromCity, fromState, fromZip, fromCountry)
    }
}
