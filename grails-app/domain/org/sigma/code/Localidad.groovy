package org.sigma.code

class Localidad {

	String nombre
	String codigoPostal
	String provincia
	
    static constraints = {
		nombre(nullable: false, blank: false, required: true)
		codigoPostal(nullable: true)
		provincia(nullable: false, blank: false, required: true)
    }
	
}
