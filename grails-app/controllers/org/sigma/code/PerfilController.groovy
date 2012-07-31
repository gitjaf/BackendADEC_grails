
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
		
		def perfilInstanceList = Perfil.list()
		
		response.status = 200
		
		render perfilInstanceList as JSON
    }
    
    def save() {
        def perfilInstance = new Perfil(request.JSON)
		
			 	 request.JSON?.idCategorias?.each{ id -> perfilInstance.addToCategorias(Categoria.get(id))} 

	 	 request.JSON?.idMenues?.each{ id -> perfilInstance.addToMenues(Menu.get(id))} 

	 	 request.JSON?.idNovedades?.each{ id -> perfilInstance.addToNovedades(Novedad.get(id))} 

	 	 request.JSON?.idSecciones?.each{ id -> perfilInstance.addToSecciones(Seccion.get(id))} 

	 	 request.JSON?.idUsuarios?.each{ id -> perfilInstance.addToUsuarios(Usuario.get(id))} 


        
		if (!perfilInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'perfil.label', default: 'Perfil'), perfilInstance.id])
        response.status = 201
		render perfilInstance as JSON
    }

    def show() {
        def perfilInstance = Perfil.get(params.id)
        if (!perfilInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render perfilInstance as JSON
    }

    def update() {
        def perfilInstance = Perfil.get(params.id)
        if (!perfilInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (perfilInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'perfil.label', default: 'Perfil'), perfilInstance.id])
                response.status = 409
                return
            }
        }

        perfilInstance.properties = request.JSON
		
		 	 	 if(request.JSON?.idCategorias || request.JSON?.idCategorias?.isEmpty()){ 
	 	 	 perfilInstance.categorias?.clear() 
	 	 	 request.JSON.idCategorias.each{id -> perfilInstance.addToCategorias(Categoria.get(id))} 
	 	} 

 	 	 if(request.JSON?.idMenues || request.JSON?.idMenues?.isEmpty()){ 
	 	 	 perfilInstance.menues?.clear() 
	 	 	 request.JSON.idMenues.each{id -> perfilInstance.addToMenues(Menu.get(id))} 
	 	} 

 	 	 if(request.JSON?.idNovedades || request.JSON?.idNovedades?.isEmpty()){ 
	 	 	 perfilInstance.novedades?.clear() 
	 	 	 request.JSON.idNovedades.each{id -> perfilInstance.addToNovedades(Novedad.get(id))} 
	 	} 

 	 	 if(request.JSON?.idSecciones || request.JSON?.idSecciones?.isEmpty()){ 
	 	 	 perfilInstance.secciones?.clear() 
	 	 	 request.JSON.idSecciones.each{id -> perfilInstance.addToSecciones(Seccion.get(id))} 
	 	} 

 	 	 if(request.JSON?.idUsuarios || request.JSON?.idUsuarios?.isEmpty()){ 
	 	 	 perfilInstance.usuarios?.clear() 
	 	 	 request.JSON.idUsuarios.each{id -> perfilInstance.addToUsuarios(Usuario.get(id))} 
	 	} 


		
        if (!perfilInstance.save(flush: true)) {
            response.status = 500
			render perfilInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'perfil.label', default: 'Perfil'), perfilInstance.id])
		response.status = 200
        render perfilInstance as JSON
    }

    def delete() {
        def perfilInstance = Perfil.get(params.id)
        if (!perfilInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
            response.status = 404
			render flash.message
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
