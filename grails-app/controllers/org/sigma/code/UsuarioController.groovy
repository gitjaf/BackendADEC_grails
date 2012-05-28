package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class UsuarioController {

    static allowedMethods = [show:['GET', 'POST'], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
		
    	params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def usuarios = Usuario.list()

		if (params.callback) {
			render (
					text: "${params.callback}(${usuarios as JSON})",
					contentType: "text/javascript",
					encoding: "UTF-8"
					)
		} else {
			render usuarios as JSON
		}
		
    }

    def create() {
		[usuarioInstance: new Usuario(params)]
    }

    def save() {
        def usuarioInstance = new Usuario(request.JSON)
        if (!usuarioInstance.save(flush: true)) {
//			render(view: "create", model: [usuarioInstance: usuarioInstance])
			response.status = 500
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
        
		response.status = 201
		render flash.message
	
    }

    def show() {
        def usuarioInstance = Usuario.get(params.id)
        
		if (!usuarioInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "list")
            return
        }
		
        if (params.callback) {
        	render (
        			text: "${params.callback}(${usuarioInstance as JSON})",
        			contentType: "text/javascript",
        			encoding: "UTF-8"
        			)
        } else {
        	render usuarioInstance as JSON
        }
		
		
    }


    def update() {
        def usuarioInstance = Usuario.get(params.id)
        if (!usuarioInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "list")
            return
        }
				
        if (params.version) {
            def version = params.version.toLong()
            if (usuarioInstance.version > version) {
                usuarioInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'usuario.label', default: 'Usuario')] as Object[],
                          "Another user has updated this Usuario while you were editing")
                render(view: "show", model: [usuarioInstance: usuarioInstance])
                return
            }
        }
		
        usuarioInstance.properties = request.JSON
				
        if (!usuarioInstance.save(flush: true)) {
            render(view: "show", model: [usuarioInstance: usuarioInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
		
		response.status = 200
		render flash.message
    }

    def delete() {
        def usuarioInstance = Usuario.get(params.id)
        if (!usuarioInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "list")
            return
        }

        try {
            usuarioInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
			
			response.status = 200
			render flash.message
            
			
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
           
			response.status = 500
			render flash.message
			
        }
    }
}
