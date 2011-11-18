package com.ps.sr.service

import com.ps.sr.model.Subscription
import com.ps.sr.model.Level
import com.ps.sr.model.Program
import com.ps.sr.model.PersonAuthority
import com.ps.sr.model.Person
import com.ps.sr.model.Authority
import com.ps.sr.model.LevelItem

class BootStrapService {

    static transactional = true

    def initData() {
        //our first user to administrate the site...
        if(!Authority.count()){
            def adminAuth = new Authority(authority: 'ADMIN').save(flush: true)
            def adminUser = new Person(username: 'admin', enabled: true, password: 's1r1r0c4').save(flush: true)

            PersonAuthority.create adminUser, adminAuth,  true
        }

        //boot strapping the "Learn English Program" and Assoc.
        if(!Program.count()){
            //use keys so we can translate the titles - We are storing this in the messages.properties...
            def englishProgram = new Program(name: "learn.english", description: "learn.english.desc", languageCode: "en")

            //let's define 3 levels for the english program EASY, MEDIUM, HARD
            def levelOne = new Level(name: "learn.english.level.1", description: "learn.english.level.1.desc", program: englishProgram)
            def levelTwo = new Level(name: "learn.english.level.2", description: "learn.english.level.2.desc", program: englishProgram)
            def levelThree = new Level(name: "learn.english.level.3", description: "learn.english.level.3.desc", program: englishProgram)

            englishProgram.addToLevels(levelOne)
            englishProgram.addToLevels(levelTwo)
            englishProgram.addToLevels(levelThree)

            //level 1 has 9 items defined in the message.properties...
            createMessageItems levelOne, 9

            //level 2 has 3 items...
            createMessageItems levelTwo, 3

            //3 has 2
            createMessageItems levelThree, 2

            //save'em all up...
            englishProgram.save(flush: true)

            //Now let's create a Testing Subscription for my Phone
            Subscription.create(englishProgram, "5037090096", true, "en")

        }
    }


    def createMessageItems(Level level, int steps){
        def x = 0

        while ( steps-- > 0 ) {
            x++
            level.addToLevelItems new LevelItem(level: level,text: "${level.name}.item.${x}", icon: "${level.name}.item.${x}.icon")
        }
    }

}
