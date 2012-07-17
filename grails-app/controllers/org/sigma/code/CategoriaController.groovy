
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
		
		def novedad_SidebarInstanceList = Categoria.list()
		
		response.status = 200
		
		render novedad_SidebarInstanceList as JSON
    }
    
    def save() {
        def novedad_SidebarInstance = new Categoria(request.JSON)
        if (!novedad_SidebarInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'novedad_Sidebar.label', default: 'Novedad_Sidebar'), novedad_SidebarInstance.id])
        response.status = 201
		render novedad_SidebarInstance as JSON
    }

    def show() {
        def novedad_SidebarInstance = Categoria.get(params.id)
        if (!novedad_SidebarInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'novedad_Sidebar.label', default: 'Novedad_Sidebar'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render novedad_SidebarInstance as JSON
    }

    def update() {
        def novedad_SidebarInstance = Categoria.get(params.id)
        if (!novedad_SidebarInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'novedad_Sidebar.label', default: 'Novedad_Sidebar'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (novedad_SidebarInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'novedad_Sidebar.label', default: 'Novedad_Sidebar'), novedad_SidebarInstance.id])
                response.status = 409
                return
            }
        }

        novedad_SidebarInstance.properties = request.JSON

        if (!novedad_SidebarInstance.save(flush: true)) {
            response.status = 500
			render novedad_SidebarInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'novedad_Sidebar.label', default: 'Novedad_Sidebar'), novedad_SidebarInstance.id])
		response.status = 200
        render novedad_SidebarInstance as JSON
    }

    def delete() {
        def novedad_SidebarInstance = Categoria.get(params.id)
        if (!novedad_SidebarInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'novedad_Sidebar.label', default: 'Novedad_Sidebar'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            novedad_SidebarInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'novedad_Sidebar.label', default: 'Novedad_Sidebar'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'novedad_Sidebar.label', default: 'Novedad_Sidebar'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
