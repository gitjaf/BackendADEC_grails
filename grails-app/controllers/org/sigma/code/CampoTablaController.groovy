
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class CampoTablaController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def campoTablaInstanceList = CampoTabla.list()
		
		response.status = 200
		
		render campoTablaInstanceList as JSON
    }
    
    def save() {
        def campoTablaInstance = new CampoTabla(request.JSON)
		
        if (!campoTablaInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'campoTabla.label', default: 'CampoTabla'), campoTablaInstance.id])
        response.status = 201
		render campoTablaInstance as JSON
    }

    def show() {
        def campoTablaInstance = CampoTabla.get(params.id)
        if (!campoTablaInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'campoTabla.label', default: 'CampoTabla'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render campoTablaInstance as JSON
    }

    def update() {
        def campoTablaInstance = CampoTabla.get(params.id)
        if (!campoTablaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'campoTabla.label', default: 'CampoTabla'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (campoTablaInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'campoTabla.label', default: 'CampoTabla'), campoTablaInstance.id])
                response.status = 409
                return
            }
        }

        campoTablaInstance.properties = request.JSON

        if (!campoTablaInstance.save(flush: true)) {
            response.status = 500
			render campoTablaInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'campoTabla.label', default: 'CampoTabla'), campoTablaInstance.id])
		response.status = 200
        render campoTablaInstance as JSON
    }

    def delete() {
        def campoTablaInstance = CampoTabla.get(params.id)
        if (!campoTablaInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'campoTabla.label', default: 'CampoTabla'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            campoTablaInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'campoTabla.label', default: 'CampoTabla'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'campoTabla.label', default: 'CampoTabla'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
