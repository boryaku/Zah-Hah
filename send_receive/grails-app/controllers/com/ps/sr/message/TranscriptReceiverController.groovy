package com.ps.sr.message

class TranscriptReceiverController {

    def transcriptionProgramService

    /**
     * This is our Twilio Configured URL for Phone
     *
     * This is our Transcription Message Receiver and Action Handler...
     *
     */
    def index = {
        def from = params.From
        def body = params.Body

        def fromCity = params.FromCity
        def fromState = params.FromState
        def fromZip = params.FromZip
        def fromCountry = params.FromCountry

        transcriptionProgramService.interactAction(from, body, fromCity, fromState, fromZip, fromCountry)
    }

}
