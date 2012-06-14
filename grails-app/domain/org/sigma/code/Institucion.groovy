package org.sigma.code

class Institucion {

	String nombre
	String domicilio
	
    static constraints = {
    	nombre(required: true, nullable: false, blank: false)
		domicilio(nullable: true)
	}
	
}
