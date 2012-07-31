
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON



class LocalidadController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def localidadInstanceList = Localidad.list()
		
		response.status = 200
		
		render localidadInstanceList as JSON
    }
    
    def save() {
        def localidadInstance = new Localidad(request.JSON)
		
		
        
		if (!localidadInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'localidad.label', default: 'Localidad'), localidadInstance.id])
        response.status = 201
		render localidadInstance as JSON
    }

    def show() {
        def localidadInstance = Localidad.get(params.id)
        if (!localidadInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'localidad.label', default: 'Localidad'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render localidadInstance as JSON
    }

    def update() {
        def localidadInstance = Localidad.get(params.id)
        if (!localidadInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'localidad.label', default: 'Localidad'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (localidadInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'localidad.label', default: 'Localidad'), localidadInstance.id])
                response.status = 409
                return
            }
        }

        localidadInstance.properties = request.JSON
		
		
		
        if (!localidadInstance.save(flush: true)) {
            response.status = 500
			render localidadInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'localidad.label', default: 'Localidad'), localidadInstance.id])
		response.status = 200
        render localidadInstance as JSON
    }

    def delete() {
        def localidadInstance = Localidad.get(params.id)
        if (!localidadInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'localidad.label', default: 'Localidad'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            localidadInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'localidad.label', default: 'Localidad'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'localidad.label', default: 'Localidad'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
