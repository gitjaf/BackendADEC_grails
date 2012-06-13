package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class InstitucionController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        
		def instituciones = Institucion.list()
		
		response.status = 200
		
		render instituciones as JSON
    }

    def create() {
        [institucionInstance: new Institucion(params)]
    }

    def save() {
        def institucionInstance = new Institucion(request.JSON)
        if (!institucionInstance.save(flush: true)) {
            response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'institucion.label', default: 'Institucion'), institucionInstance.id])
		response.status = 201
		render flash.message
		
	}

    def show() {
        def institucionInstance = Institucion.get(params.id)
        if (!institucionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'institucion.label', default: 'Institucion'), params.id])
            response.status = 404
			render flash.message
            return
        }
		
		render institucionInstance as JSON
    }
   

    def update() {
        def institucionInstance = Institucion.get(params.id)
        if (!institucionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'institucion.label', default: 'Institucion'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (institucionInstance.version > version) {
                institucionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'institucion.label', default: 'Institucion')] as Object[],
                          "Another user has updated this Institucion while you were editing")
                response.status = 409
                return
            }
        }

        institucionInstance.properties = request.JSON

        if (!institucionInstance.save(flush: true)) {
            response.status = 500
			render institucionInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'institucion.label', default: 'Institucion'), institucionInstance.id])
		response.status = 200
		render institucionInstance as JSON
    }

    def delete() {
        def institucionInstance = Institucion.get(params.id)
        if (!institucionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'institucion.label', default: 'Institucion'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            institucionInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'institucion.label', default: 'Institucion'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'institucion.label', default: 'Institucion'), params.id])
			response.status = 500
			render flash.message
        }
    }
}
