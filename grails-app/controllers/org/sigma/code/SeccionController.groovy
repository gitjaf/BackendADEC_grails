
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON



class SeccionController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def seccionInstanceList = Seccion.list()
		
		response.status = 200
		
		render seccionInstanceList as JSON
    }
    
    def save() {
        def seccionInstance = new Seccion(request.JSON)
		
		
        
		if (!seccionInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'seccion.label', default: 'Seccion'), seccionInstance.id])
        response.status = 201
		render seccionInstance as JSON
    }

    def show() {
        def seccionInstance = Seccion.get(params.id)
        if (!seccionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'seccion.label', default: 'Seccion'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render seccionInstance as JSON
    }

    def update() {
        def seccionInstance = Seccion.get(params.id)
        if (!seccionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'seccion.label', default: 'Seccion'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (seccionInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'seccion.label', default: 'Seccion'), seccionInstance.id])
                response.status = 409
                return
            }
        }

        seccionInstance.properties = request.JSON
		
		
		
        if (!seccionInstance.save(flush: true)) {
            response.status = 500
			render seccionInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'seccion.label', default: 'Seccion'), seccionInstance.id])
		response.status = 200
        render seccionInstance as JSON
    }

    def delete() {
        def seccionInstance = Seccion.get(params.id)
        if (!seccionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'seccion.label', default: 'Seccion'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            seccionInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'seccion.label', default: 'Seccion'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'seccion.label', default: 'Seccion'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
