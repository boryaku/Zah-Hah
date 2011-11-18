package com.ps.sr.service

import com.ps.sr.model.LevelItem

class TranscriptionProgramService extends ProgramService{

    static transactional = true

    @Override
    send(LevelItem levelItem, String phoneNumber, String languageCode) {
        return null
    }


}
