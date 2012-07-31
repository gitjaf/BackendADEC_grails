
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

import java.text.SimpleDateFormat

class CursoAprobadoController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def cursoAprobadoInstanceList = CursoAprobado.list()
		
		response.status = 200
		
		render cursoAprobadoInstanceList as JSON
    }
    
    def save() {
        def cursoAprobadoInstance = new CursoAprobado(request.JSON)
		
			 bindData(cursoAprobadoInstance, request.JSON, ['fechaFinalizacion']) 
	 	 cursoAprobadoInstance.fechaFinalizacion = request.JSON.fechaFinalizacion ? new SimpleDateFormat('yyyy-MM-dd').parse(request.JSON.fechaFinalizacion) : null 

	 	 cursoAprobadoInstance.candidato = Candidato.get(request.JSON?.idCandidato) 
 
	 	 cursoAprobadoInstance.curso = Curso.get(request.JSON?.idCurso) 
 
	 	 cursoAprobadoInstance.institucion = Institucion.get(request.JSON?.idInstitucion) 
 

        
		if (!cursoAprobadoInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'cursoAprobado.label', default: 'CursoAprobado'), cursoAprobadoInstance.id])
        response.status = 201
		render cursoAprobadoInstance as JSON
    }

    def show() {
        def cursoAprobadoInstance = CursoAprobado.get(params.id)
        if (!cursoAprobadoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cursoAprobado.label', default: 'CursoAprobado'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render cursoAprobadoInstance as JSON
    }

    def update() {
        def cursoAprobadoInstance = CursoAprobado.get(params.id)
        if (!cursoAprobadoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'cursoAprobado.label', default: 'CursoAprobado'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (cursoAprobadoInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'cursoAprobado.label', default: 'CursoAprobado'), cursoAprobadoInstance.id])
                response.status = 409
                return
            }
        }

        cursoAprobadoInstance.properties = request.JSON
		
			 bindData(cursoAprobadoInstance, request.JSON, ['fechaFinalizacion']) 
	 	 cursoAprobadoInstance.fechaFinalizacion = request.JSON.fechaFinalizacion ? new SimpleDateFormat('yyyy-MM-dd').parse(request.JSON.fechaFinalizacion) : null 

	 	 cursoAprobadoInstance.candidato = (request.JSON?.idCandidato) ?  Candidato.get(request.JSON?.idCandidato) : cursoAprobadoInstance.candidato 
 
	 	 cursoAprobadoInstance.curso = (request.JSON?.idCurso) ?  Curso.get(request.JSON?.idCurso) : cursoAprobadoInstance.curso 
 
	 	 cursoAprobadoInstance.institucion = (request.JSON?.idInstitucion) ?  Institucion.get(request.JSON?.idInstitucion) : cursoAprobadoInstance.institucion 
 

		
        if (!cursoAprobadoInstance.save(flush: true)) {
            response.status = 500
			render cursoAprobadoInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'cursoAprobado.label', default: 'CursoAprobado'), cursoAprobadoInstance.id])
		response.status = 200
        render cursoAprobadoInstance as JSON
    }

    def delete() {
        def cursoAprobadoInstance = CursoAprobado.get(params.id)
        if (!cursoAprobadoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cursoAprobado.label', default: 'CursoAprobado'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            cursoAprobadoInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'cursoAprobado.label', default: 'CursoAprobado'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'cursoAprobado.label', default: 'CursoAprobado'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
