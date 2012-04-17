package org.sigma.code

class Perfil {

	int id
	String seccion
	String perfil
	
    static constraints = {
    	seccion(required: true)
		perfil(required: true)
	}
}
