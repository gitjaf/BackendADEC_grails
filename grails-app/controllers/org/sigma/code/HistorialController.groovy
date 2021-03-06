
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

import java.text.SimpleDateFormat

class HistorialController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def historialInstanceList = Historial.list()
		
		response.status = 200
		
		render historialInstanceList as JSON
    }
    
    def save() {
        def historialInstance = new Historial(request.JSON)
		
			 bindData(historialInstance, request.JSON, ['fecha']) 
	 	 historialInstance.fecha = request.JSON.fecha ? new SimpleDateFormat('yyyy-MM-dd').parse(request.JSON.fecha) : null 

	 	 historialInstance.usuario = Usuario.get(request.JSON?.idUsuario) 
 

        
		if (!historialInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'historial.label', default: 'Historial'), historialInstance.id])
        response.status = 201
		render historialInstance as JSON
    }

    def show() {
        def historialInstance = Historial.get(params.id)
        if (!historialInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render historialInstance as JSON
    }

    def update() {
        def historialInstance = Historial.get(params.id)
        if (!historialInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (historialInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'historial.label', default: 'Historial'), historialInstance.id])
                response.status = 409
                return
            }
        }

        historialInstance.properties = request.JSON
		
			 bindData(historialInstance, request.JSON, ['fecha']) 
	 	 historialInstance.fecha = request.JSON.fecha ? new SimpleDateFormat('yyyy-MM-dd').parse(request.JSON.fecha) : null 

	 	 historialInstance.usuario = (request.JSON?.idUsuario) ?  Usuario.get(request.JSON?.idUsuario) : historialInstance.usuario 
 

		
        if (!historialInstance.save(flush: true)) {
            response.status = 500
			render historialInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'historial.label', default: 'Historial'), historialInstance.id])
		response.status = 200
        render historialInstance as JSON
    }

    def delete() {
        def historialInstance = Historial.get(params.id)
        if (!historialInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            historialInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'historial.label', default: 'Historial'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
