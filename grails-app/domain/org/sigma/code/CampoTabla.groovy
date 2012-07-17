package org.sigma.code

class CampoTabla {

	String nombre
	String descripcion
	
	static belongsTo = [Usuario, Perfil]
	static hasMany = [usuarios: Usuario, perfiles: Perfil]
	
    static constraints = {
    	nombre(nullable: false, blank: false, required: true)
		descripcion(nullable: false, blank: false, required: true)
	}
	
}
