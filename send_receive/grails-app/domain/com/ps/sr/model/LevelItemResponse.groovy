package com.ps.sr.model

/**
 * This represents a response to an item that a learner could interact with.
 *
 * i.e. the answer applied to the level item
 */
class LevelItemResponse {

    LevelItem levelItem
    ResponseStatus responseStatus

    //we hold the translated text here
    String expectedResponseBody
    String responseBody

    Date responseReceivedDate
    Date itemSentDate
    Long responseTime

    static constraints = {
        responseReceivedDate(nullable: true)
        responseTime(nullable: true)
        responseBody(nullable: true)
    }

}