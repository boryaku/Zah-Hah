package com.ps.sr.model

class LevelItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [levelItemInstanceList: LevelItem.list(params), levelItemInstanceTotal: LevelItem.count()]
    }

    def create = {
        def levelItemInstance = new LevelItem()
        levelItemInstance.properties = params
        return [levelItemInstance: levelItemInstance]
    }

    def save = {
        def levelItemInstance = new LevelItem(params)
        if (levelItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'levelItem.label', default: 'LevelItem'), levelItemInstance.id])}"
            redirect(action: "show", id: levelItemInstance.id)
        }
        else {
            render(view: "create", model: [levelItemInstance: levelItemInstance])
        }
    }

    def show = {
        def levelItemInstance = LevelItem.get(params.id)
        if (!levelItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'levelItem.label', default: 'LevelItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [levelItemInstance: levelItemInstance]
        }
    }

    def edit = {
        def levelItemInstance = LevelItem.get(params.id)
        if (!levelItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'levelItem.label', default: 'LevelItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [levelItemInstance: levelItemInstance]
        }
    }

    def update = {
        def levelItemInstance = LevelItem.get(params.id)
        if (levelItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (levelItemInstance.version > version) {

                    levelItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'levelItem.label', default: 'LevelItem')] as Object[], "Another user has updated this LevelItem while you were editing")
                    render(view: "edit", model: [levelItemInstance: levelItemInstance])
                    return
                }
            }
            levelItemInstance.properties = params
            if (!levelItemInstance.hasErrors() && levelItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'levelItem.label', default: 'LevelItem'), levelItemInstance.id])}"
                redirect(action: "show", id: levelItemInstance.id)
            }
            else {
                render(view: "edit", model: [levelItemInstance: levelItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'levelItem.label', default: 'LevelItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def levelItemInstance = LevelItem.get(params.id)
        if (levelItemInstance) {
            try {
                levelItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'levelItem.label', default: 'LevelItem'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'levelItem.label', default: 'LevelItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'levelItem.label', default: 'LevelItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
