package org.sigma.code

class Perfil {

	int id
	String descripcion
	
	static belongsTo = Usuario
	
	static hasMany = [menues: Menu, secciones: Seccion, sidebars: Novedad_Sidebar, novedades: Novedad]
	
    static constraints = {
		descripcion(required: false, nullable: true)
	}
}
