package com.ps.sr.model

/**
 * This represents a user interaction with a program.  The user initiates a session via a text message indicating "start"
 * we will then create a session for the user and interact with them in the context of this sessions time out after 30 minutes.
 *
 * The phoneNumber is the key to a session and is the primary integration point with the portal.
 *
 */
class ProgramSession {

    Date startTime
    Date updateTime
    Date endTime
    Long duration
    LevelItemResponse lastItemResponse

    List levelItemResponses
    static hasMany = [levelItemResponses: LevelItemResponse]

    static constraints = {
       updateTime(nullable: true)
       endTime(nullable: true)
       duration(nullable: true)
    }
}
