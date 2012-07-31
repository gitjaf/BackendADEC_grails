
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON



class LenguajeController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def lenguajeInstanceList = Lenguaje.list()
		
		response.status = 200
		
		render lenguajeInstanceList as JSON
    }
    
    def save() {
        def lenguajeInstance = new Lenguaje(request.JSON)
		
			 	 lenguajeInstance.idioma = Idioma.get(request.JSON?.idIdioma) 
 

        
		if (!lenguajeInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'lenguaje.label', default: 'Lenguaje'), lenguajeInstance.id])
        response.status = 201
		render lenguajeInstance as JSON
    }

    def show() {
        def lenguajeInstance = Lenguaje.get(params.id)
        if (!lenguajeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'lenguaje.label', default: 'Lenguaje'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render lenguajeInstance as JSON
    }

    def update() {
        def lenguajeInstance = Lenguaje.get(params.id)
        if (!lenguajeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'lenguaje.label', default: 'Lenguaje'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (lenguajeInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'lenguaje.label', default: 'Lenguaje'), lenguajeInstance.id])
                response.status = 409
                return
            }
        }

        lenguajeInstance.properties = request.JSON
		
			 	 lenguajeInstance.idioma = (request.JSON?.idIdioma) ?  Idioma.get(request.JSON?.idIdioma) : lenguajeInstance.idioma 
 

		
        if (!lenguajeInstance.save(flush: true)) {
            response.status = 500
			render lenguajeInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'lenguaje.label', default: 'Lenguaje'), lenguajeInstance.id])
		response.status = 200
        render lenguajeInstance as JSON
    }

    def delete() {
        def lenguajeInstance = Lenguaje.get(params.id)
        if (!lenguajeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'lenguaje.label', default: 'Lenguaje'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            lenguajeInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'lenguaje.label', default: 'Lenguaje'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'lenguaje.label', default: 'Lenguaje'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
