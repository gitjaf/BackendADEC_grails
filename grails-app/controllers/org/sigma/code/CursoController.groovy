
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON



class CursoController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def cursoInstanceList = Curso.list()
		
		response.status = 200
		
		render cursoInstanceList as JSON
    }
    
    def save() {
        def cursoInstance = new Curso(request.JSON)
		
		
        
		if (!cursoInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'curso.label', default: 'Curso'), cursoInstance.id])
        response.status = 201
		render cursoInstance as JSON
    }

    def show() {
        def cursoInstance = Curso.get(params.id)
        if (!cursoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'curso.label', default: 'Curso'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render cursoInstance as JSON
    }

    def update() {
        def cursoInstance = Curso.get(params.id)
        if (!cursoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'curso.label', default: 'Curso'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (cursoInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'curso.label', default: 'Curso'), cursoInstance.id])
                response.status = 409
                return
            }
        }

        cursoInstance.properties = request.JSON
		
		
		
        if (!cursoInstance.save(flush: true)) {
            response.status = 500
			render cursoInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'curso.label', default: 'Curso'), cursoInstance.id])
		response.status = 200
        render cursoInstance as JSON
    }

    def delete() {
        def cursoInstance = Curso.get(params.id)
        if (!cursoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'curso.label', default: 'Curso'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            cursoInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'curso.label', default: 'Curso'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'curso.label', default: 'Curso'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
