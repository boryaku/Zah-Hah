package com.ps.sr.model

/**
 * This is a mapping between a phoneNumber and a Program, we'll use the portal to send us the details in the future.
 *
 * ie. 503-709-0096 is in English Program
 */
class Subscription {

    Program program
    String phoneNumber
    Boolean active
    String preferredLanguageCode
    ProgramSession lastSession

    //active record access, last sent item
    List programSessions
    static hasMany = [programSessions: ProgramSession]

    static constraints = {
        programSessions()
        lastSession(nullable: true)
    }

    static Subscription create(Program program, String phoneNumber, Boolean active, String preferredLanguage){
        new Subscription(program: program, phoneNumber: phoneNumber, active: active, preferredLanguageCode: preferredLanguage).save(flush: true, insert: true)
    }
}
