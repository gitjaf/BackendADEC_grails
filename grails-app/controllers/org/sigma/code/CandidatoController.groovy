
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

import java.text.SimpleDateFormat

class CandidatoController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def candidatoInstanceList = Candidato.list()
		
		response.status = 200
		
		render candidatoInstanceList as JSON
    }
    
    def save() {
        def candidatoInstance = new Candidato(request.JSON)
		
			 bindData(candidatoInstance, request.JSON, ['fechaDeNacimiento']) 
	 	 candidatoInstance.fechaDeNacimiento = request.JSON.fechaDeNacimiento ? new SimpleDateFormat('yyyy-MM-dd').parse(request.JSON.fechaDeNacimiento) : null 

	 	 candidatoInstance.localidad = Localidad.get(request.JSON?.idLocalidad) 
 
	 	 request.JSON?.idCursosAprobados?.each{ id -> candidatoInstance.addToCursosAprobados(CursoAprobado.get(id))} 

	 	 request.JSON?.idLenguajes?.each{ id -> candidatoInstance.addToLenguajes(Lenguaje.get(id))} 


        
		if (!candidatoInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'candidato.label', default: 'Candidato'), candidatoInstance.id])
        response.status = 201
		render candidatoInstance as JSON
    }

    def show() {
        def candidatoInstance = Candidato.get(params.id)
        if (!candidatoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'candidato.label', default: 'Candidato'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render candidatoInstance as JSON
    }

    def update() {
        def candidatoInstance = Candidato.get(params.id)
        if (!candidatoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'candidato.label', default: 'Candidato'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (candidatoInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'candidato.label', default: 'Candidato'), candidatoInstance.id])
                response.status = 409
                return
            }
        }

        candidatoInstance.properties = request.JSON
		
			 bindData(candidatoInstance, request.JSON, ['fechaDeNacimiento']) 
	 	 candidatoInstance.fechaDeNacimiento = request.JSON.fechaDeNacimiento ? new SimpleDateFormat('yyyy-MM-dd').parse(request.JSON.fechaDeNacimiento) : null 

	 	 candidatoInstance.localidad = (request.JSON?.idLocalidad) ?  Localidad.get(request.JSON?.idLocalidad) : candidatoInstance.localidad 
 
 	 	 if(request.JSON?.idCursosAprobados || request.JSON?.idCursosAprobados?.isEmpty()){ 
	 	 	 candidatoInstance.cursosAprobados?.clear() 
	 	 	 request.JSON.idCursosAprobados.each{id -> candidatoInstance.addToCursosAprobados(CursoAprobado.get(id))} 
	 	} 

 	 	 if(request.JSON?.idLenguajes || request.JSON?.idLenguajes?.isEmpty()){ 
	 	 	 candidatoInstance.lenguajes?.clear() 
	 	 	 request.JSON.idLenguajes.each{id -> candidatoInstance.addToLenguajes(Lenguaje.get(id))} 
	 	} 


		
        if (!candidatoInstance.save(flush: true)) {
            response.status = 500
			render candidatoInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'candidato.label', default: 'Candidato'), candidatoInstance.id])
		response.status = 200
        render candidatoInstance as JSON
    }

    def delete() {
        def candidatoInstance = Candidato.get(params.id)
        if (!candidatoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'candidato.label', default: 'Candidato'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            candidatoInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'candidato.label', default: 'Candidato'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'candidato.label', default: 'Candidato'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
