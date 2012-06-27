
package org.sigma.code

import java.text.SimpleDateFormat;

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class NovedadController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def novedadInstanceList = Novedad.list()
		
		response.status = 200
		
		render novedadInstanceList as JSON
    }
    
    def save() {
        def novedadInstance = new Novedad()
        bindData(novedadInstance, request.JSON, ['fecha'])
		novedadInstance.fecha = request.JSON.fecha ? new SimpleDateFormat("dd/MM/yyyy").parse(request.JSON.fecha) : null
		novedadInstance.categoria = Novedad_Sidebar.get(request.JSON.idCategoria)
		if (!novedadInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'novedad.label', default: 'Novedad'), novedadInstance.id])
        response.status = 201
		
		render novedadInstance as JSON
    }

    def show() {
        def novedadInstance = Novedad.get(params.id)
        if (!novedadInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'novedad.label', default: 'Novedad'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render novedadInstance as JSON
    }

    def update() {
        def novedadInstance = Novedad.get(params.id)
        if (!novedadInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'novedad.label', default: 'Novedad'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (novedadInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'novedad.label', default: 'Novedad'), novedadInstance.id])
                response.status = 409
                return
            }
        }

        bindData(novedadInstance, request.JSON, ['fecha'])
		novedadInstance.fecha = request.JSON.fecha ? new SimpleDateFormat("dd/MM/yyyy").parse(request.JSON.fecha) : novedadInstance.fecha

        if (!novedadInstance.save(flush: true)) {
            response.status = 500
			render novedadInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'novedad.label', default: 'Novedad'), novedadInstance.id])
		response.status = 200
        render novedadInstance as JSON
    }

    def delete() {
        def novedadInstance = Novedad.get(params.id)
        if (!novedadInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'novedad.label', default: 'Novedad'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            novedadInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'novedad.label', default: 'Novedad'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'novedad.label', default: 'Novedad'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
