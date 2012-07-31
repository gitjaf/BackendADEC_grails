package org.sigma.code

class Idioma {

	String idioma
	
    static constraints = {
		idioma(nullable: false, blank: false, required: true)
    }
	
	
}
