package com.ps.sr.model

class LevelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [levelInstanceList: Level.list(params), levelInstanceTotal: Level.count()]
    }

    def create = {
        def levelInstance = new Level()
        levelInstance.properties = params
        return [levelInstance: levelInstance]
    }

    def save = {
        def levelInstance = new Level(params)
        if (levelInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'level.label', default: 'Level'), levelInstance.id])}"
            redirect(action: "show", id: levelInstance.id)
        }
        else {
            render(view: "create", model: [levelInstance: levelInstance])
        }
    }

    def show = {
        def levelInstance = Level.get(params.id)
        if (!levelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'level.label', default: 'Level'), params.id])}"
            redirect(action: "list")
        }
        else {
            [levelInstance: levelInstance]
        }
    }

    def edit = {
        def levelInstance = Level.get(params.id)
        if (!levelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'level.label', default: 'Level'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [levelInstance: levelInstance]
        }
    }

    def update = {
        def levelInstance = Level.get(params.id)
        if (levelInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (levelInstance.version > version) {
                    
                    levelInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'level.label', default: 'Level')] as Object[], "Another user has updated this Level while you were editing")
                    render(view: "edit", model: [levelInstance: levelInstance])
                    return
                }
            }
            levelInstance.properties = params
            if (!levelInstance.hasErrors() && levelInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'level.label', default: 'Level'), levelInstance.id])}"
                redirect(action: "show", id: levelInstance.id)
            }
            else {
                render(view: "edit", model: [levelInstance: levelInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'level.label', default: 'Level'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def levelInstance = Level.get(params.id)
        if (levelInstance) {
            try {
                levelInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'level.label', default: 'Level'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'level.label', default: 'Level'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'level.label', default: 'Level'), params.id])}"
            redirect(action: "list")
        }
    }
}
