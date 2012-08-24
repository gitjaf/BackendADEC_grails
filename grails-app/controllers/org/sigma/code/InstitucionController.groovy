
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
		
		def institucionInstanceList = Institucion.list()
		
		response.status = 200
		
		render institucionInstanceList as JSON
    }
    
    def save() {
        def institucionInstance = new Institucion(request.JSON.institucion)
				
		institucionInstance.localidad = Localidad.get(request.JSON.institucion?.idLocalidad) 
		
		request.JSON?.idCursos?.each{ id -> institucionInstance.addToCursos(Curso.get(id))}
        
		if (!institucionInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'institucion.label', default: 'Institucion'), institucionInstance.id])
        response.status = 201
		render institucionInstance as JSON
    }

    def show() {
        def institucionInstance = Institucion.get(params.id)
        if (!institucionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'institucion.label', default: 'Institucion'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
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
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'institucion.label', default: 'Institucion'), institucionInstance.id])
                response.status = 409
                return
            }
        }

        institucionInstance.properties = request.JSON
		
		institucionInstance.localidad = (request.JSON?.idLocalidad) ?  Localidad.get(request.JSON?.idLocalidad) : institucionInstance.localidad 
 
		if(request.JSON?.idCursos || request.JSON?.idCursos?.isEmpty()){
			institucionInstance.cursos.clear()
			request.JSON?.idCursos.each{id -> institucionInstance.addToCursos(Curso.get(id))}
		}
		
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
