package org.sigma.code

class CursoAprobado {

	Date fechaFinalizacion
	
	Curso curso
	Institucion Institucion
	
	static belongsTo = [candidato: Candidato]

		
    static constraints = {
		fechaFinalizacion(required: true, nullable: false)
    }
}
