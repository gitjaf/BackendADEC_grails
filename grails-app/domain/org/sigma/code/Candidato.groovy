package org.sigma.code

class Candidato {

	String nombre
	String apellido
	String cuil
	String dni
	Date fechaDeNacimiento
	String domicilio
	String telefono
	String celular
	String email
	String nivelDeEstudios
	String titulo
	String institucion
	String nivelDeInformatica
	String observacion
	Boolean ocupadoEnOficio
	Integer estado
	
	Localidad localidad
	
	static hasMany = [cursosAprobados: CursoAprobado, lenguajes: Lenguaje]
	
    static constraints = {
		nombre(required: true, nullable: false, blank: false)
		apellido(required: true, nullable: false, blank: false)
		dni(required: true, nullable: false, blank: false)
		email(required: true, nullable: false, blank: false)
		cuil(required: false, nullable: true, blank: true)
		fechaDeNacimiento(required: false, nullable: true)
		domicilio(required: false, nullable: true)
		telefono(required: false, nullable: true)
		celular(required: false, nullable: true)
		nivelDeEstudios(required: false, nullable: true)
		titulo(required: false, nullable: true)
		institucion(required: false, nullable: true)
		nivelDeInformatica(required: false, nullable: true)
		observacion(required: false, nullable: true)
		ocupadoEnOficio(required: false, nullable: true)
		estado(required: false, nullable: true)
    }
	
	static mapping = {
		lenguajes cascade: "save-update"
		cursosAprobados cascade: "save-update"
	}
}
