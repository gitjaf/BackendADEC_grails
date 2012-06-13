package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException

class HistorialController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [historialInstanceList: Historial.list(params), historialInstanceTotal: Historial.count()]
    }

    def create() {
        [historialInstance: new Historial(params)]
    }

    def save() {
        def historialInstance = new Historial(params)
        if (!historialInstance.save(flush: true)) {
            render(view: "create", model: [historialInstance: historialInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'historial.label', default: 'Historial'), historialInstance.id])
        redirect(action: "show", id: historialInstance.id)
    }

    def show() {
        def historialInstance = Historial.get(params.id)
        if (!historialInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            redirect(action: "list")
            return
        }

        [historialInstance: historialInstance]
    }

    def edit() {
        def historialInstance = Historial.get(params.id)
        if (!historialInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            redirect(action: "list")
            return
        }

        [historialInstance: historialInstance]
    }

    def update() {
        def historialInstance = Historial.get(params.id)
        if (!historialInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (historialInstance.version > version) {
                historialInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'historial.label', default: 'Historial')] as Object[],
                          "Another user has updated this Historial while you were editing")
                render(view: "edit", model: [historialInstance: historialInstance])
                return
            }
        }

        historialInstance.properties = params

        if (!historialInstance.save(flush: true)) {
            render(view: "edit", model: [historialInstance: historialInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'historial.label', default: 'Historial'), historialInstance.id])
        redirect(action: "show", id: historialInstance.id)
    }

    def delete() {
        def historialInstance = Historial.get(params.id)
        if (!historialInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            redirect(action: "list")
            return
        }

        try {
            historialInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
