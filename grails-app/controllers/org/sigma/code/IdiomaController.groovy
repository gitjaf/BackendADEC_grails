
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON



class IdiomaController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def idiomaInstanceList = Idioma.list()
		
		response.status = 200
		
		render idiomaInstanceList as JSON
    }
    
    def save() {
        def idiomaInstance = new Idioma(request.JSON)
		
		
        
		if (!idiomaInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'idioma.label', default: 'Idioma'), idiomaInstance.id])
        response.status = 201
		render idiomaInstance as JSON
    }

    def show() {
        def idiomaInstance = Idioma.get(params.id)
        if (!idiomaInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'idioma.label', default: 'Idioma'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render idiomaInstance as JSON
    }

    def update() {
        def idiomaInstance = Idioma.get(params.id)
        if (!idiomaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'idioma.label', default: 'Idioma'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (idiomaInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'idioma.label', default: 'Idioma'), idiomaInstance.id])
                response.status = 409
                return
            }
        }

        idiomaInstance.properties = request.JSON
		
		
		
        if (!idiomaInstance.save(flush: true)) {
            response.status = 500
			render idiomaInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'idioma.label', default: 'Idioma'), idiomaInstance.id])
		response.status = 200
        render idiomaInstance as JSON
    }

    def delete() {
        def idiomaInstance = Idioma.get(params.id)
        if (!idiomaInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'idioma.label', default: 'Idioma'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            idiomaInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'idioma.label', default: 'Idioma'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'idioma.label', default: 'Idioma'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
