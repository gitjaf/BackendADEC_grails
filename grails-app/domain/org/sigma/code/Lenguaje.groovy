package org.sigma.code

class Lenguaje {

	String nivel
	
	Idioma idioma
	
	static belongsTo = [Candidato, Idioma]
	
    static constraints = {
		nivel(required: true, nullable: false, blank: false)
    }
	
	
}
