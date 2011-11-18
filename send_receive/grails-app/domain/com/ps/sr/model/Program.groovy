package com.ps.sr.model

/**
 * This represents a high level concept of a learning program
 *
 * i.e. "Learn English Program"
 *
 * <todo> ADD CONSTRAINTS THAT ARE MEANINGFUL
 */
class Program {
    List levels
    static hasMany = [levels: Level]

    String name
    String description
    String languageCode

    static constraints = { levels()
    }

    String toString(){
        return name
    }
}
