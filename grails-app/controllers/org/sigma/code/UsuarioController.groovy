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

		response.status = 200
		
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

    def save() {
        def usuarioInstance = new Usuario(request.JSON)
		def perfil = Perfil.get(request.JSON.idPerfil)
		usuarioInstance.perfil = perfil
        
		if (!usuarioInstance.save(flush: true)) {
			response.status = 500
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
		response.status = 201
		render usuarioInstance as JSON
	
    }

    def show() {
        def usuarioInstance = Usuario.get(params.id)
        
		if (!usuarioInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            response.status = 404
			render flash.message
            return
        }
		
		response.status = 200
		
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
            response.status = 404
			render flash.message
            return
        }
				
        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (usuarioInstance.version > version) {
                flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
				response.status = 409
				render flash.message
                return
            }
        }
		
        usuarioInstance.properties = request.JSON
				
        if (!usuarioInstance.save(flush: true)) {
            response.status = 500
			render usuarioInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
		
		response.status = 200
		render usuarioInstance as JSON
    }

    def delete() {
        def usuarioInstance = Usuario.get(params.id)
        if (!usuarioInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            response.status = 404
			render flash.message
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
