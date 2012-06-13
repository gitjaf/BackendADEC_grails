package org.sigma.code

class Institucion {

	String nombre
	String domicilio
	
    static constraints = {
    	nombre(nullable: true)
		domicilio(nullable: true)
	}
	
}
