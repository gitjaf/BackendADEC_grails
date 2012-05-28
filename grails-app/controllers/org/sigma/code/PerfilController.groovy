package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class PerfilController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        
		def perfiles = Perfil.list();
		
		if(params.callback){
			render (
				text: "${params.callback}(${perfiles as JSON})",
				contentType: "text/javascript",
				encoding: "UTF-8"
				)
		} else {
			render perfiles as JSON
		}
		
    }

    def create() {
        [perfilInstance: new Perfil(params)]
    }

    def save() {
        def perfilInstance = new Perfil(request.JSON)
        if (!perfilInstance.save(flush: true)) {
//            render(view: "create", model: [perfilInstance: perfilInstance])
			response.status = 500
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'perfil.label', default: 'Perfil'), perfilInstance.id])
        response.setStatus(201)
		render flash.message
    }

    def show() {
        def perfilInstance = Perfil.get(params.id)
        if (!perfilInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
            redirect(action: "list")
            return
        }

		if (params.callback) {
        	render (
        			text: "${params.callback}(${perfilInstance as JSON})",
        			contentType: "text/javascript",
        			encoding: "UTF-8"
        			)
        } else {
        	render perfilInstance as JSON
        }
    }

    

    def update() {
        def perfilInstance = Perfil.get(params.id)
        if (!perfilInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (perfilInstance.version > version) {
                perfilInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'perfil.label', default: 'Perfil')] as Object[],
                          "Another user has updated this Perfil while you were editing")
                render(view: "show", model: [perfilInstance: perfilInstance])
                return
            }
        }
				
        perfilInstance.properties = request.JSON

        if (!perfilInstance.save(flush: true)) {
            render(view: "show", model: [perfilInstance: perfilInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'perfil.label', default: 'Perfil'), perfilInstance.id])
        response.status = 200
		render flash.message
    }

    def delete() {
        def perfilInstance = Perfil.get(params.id)
        if (!perfilInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
            redirect(action: "list")
            return
        }

        try {
            perfilInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
            response.status = 200
			render flash.message
            
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
            response.status = 500
			render flash.message
            
        }
    }
}
