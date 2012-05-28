package org.sigma.code

class Perfil {

	int id
	String seccion
	String perfil
	
    static constraints = {
    	seccion(required: true, blank: false)
		perfil(required: true, blank: false)
	}
}
