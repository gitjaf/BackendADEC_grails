package org.sigma.code

class Perfil {

	int id
	String descripcion
	
	static hasMany = [menues: Menu, secciones: Seccion, sidebars: Novedad_Sidebar, novedades: Novedad]
	
    static constraints = {
		descripcion(required: true, nullable: false, blank: false)
	}
}
