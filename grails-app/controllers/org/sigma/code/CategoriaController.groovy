
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON



class CategoriaController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def categoriaInstanceList = Categoria.list()
		
		response.status = 200
		
		render categoriaInstanceList as JSON
    }
    
    def save() {
        def categoriaInstance = new Categoria(request.JSON)
		
		
        
		if (!categoriaInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'categoria.label', default: 'Categoria'), categoriaInstance.id])
        response.status = 201
		render categoriaInstance as JSON
    }

    def show() {
        def categoriaInstance = Categoria.get(params.id)
        if (!categoriaInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'categoria.label', default: 'Categoria'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render categoriaInstance as JSON
    }

    def update() {
        def categoriaInstance = Categoria.get(params.id)
        if (!categoriaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'categoria.label', default: 'Categoria'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (categoriaInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'categoria.label', default: 'Categoria'), categoriaInstance.id])
                response.status = 409
                return
            }
        }

        categoriaInstance.properties = request.JSON
		
		
		
        if (!categoriaInstance.save(flush: true)) {
            response.status = 500
			render categoriaInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'categoria.label', default: 'Categoria'), categoriaInstance.id])
		response.status = 200
        render categoriaInstance as JSON
    }

    def delete() {
        def categoriaInstance = Categoria.get(params.id)
        if (!categoriaInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'categoria.label', default: 'Categoria'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            categoriaInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'categoria.label', default: 'Categoria'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'categoria.label', default: 'Categoria'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
