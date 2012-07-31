package org.sigma.code

class Institucion {

	String nombre
	String domicilio
	
	Localidad localidad
	
	static hasMany = [cursos : Curso]
	
    static constraints = {
    	nombre(required: true, nullable: false, blank: false)
		domicilio(nullable: true)
	}
	
	static mapping = {
		cursos cascade: "save-update"
	}
	
}
