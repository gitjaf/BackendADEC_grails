
include "cursoFixtures"

build{
		institucion1(org.sigma.code.Institucion, nombre: "Nombre 1", domicilio: "Un domicilio 1", cursos: [curso1, curso2], localidad: localidad1)
		institucion2(org.sigma.code.Institucion, nombre: "Nombre 2", domicilio: "Un domicilio 2", cursos: [curso2, curso3], localidad: localidad1)
		institucion3(org.sigma.code.Institucion, nombre: "Nombre 3", domicilio: "Un domicilio 3", cursos: [curso1, curso4], localidad: localidad1)
		institucion4(org.sigma.code.Institucion, nombre: "Nombre 4", domicilio: "Un domicilio 4", cursos: [curso2, curso5], localidad: localidad1)
		institucion5(org.sigma.code.Institucion, nombre: "Nombre 5", domicilio: "Un domicilio 5", cursos: [curso3, curso4, curso5], localidad: localidad1)
	}
