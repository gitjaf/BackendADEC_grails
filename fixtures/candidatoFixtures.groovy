include  "localidadFixtures", "cursoAprobadoFixtures", "lenguajeFixtures"

build {
	
	candidato1(org.sigma.code.Candidato, apellido: "Apellido 1", nombre: "Nombre 1", email: "unEmail@unDominio.com", dni: "20.000.000",
		   lenguajes: [lenguaje1], cursosAprobados: [cursoAprobado1, cursoAprobado2], localidad: localidad1)
	candidato2(org.sigma.code.Candidato, apellido: "Apellido 1", nombre: "Nombre 1", email: "unEmail@unDominio.com", dni: "20.000.000",
		  lenguajes: [lenguaje1], cursosAprobados: [cursoAprobado2, cursoAprobado3], localidad: localidad1)
	candidato3(org.sigma.code.Candidato, apellido: "Apellido 1", nombre: "Nombre 1", email: "unEmail@unDominio.com", dni: "20.000.000",
		  lenguajes: [lenguaje1], cursosAprobados: [cursoAprobado3], localidad: localidad1)
	candidato4(org.sigma.code.Candidato, apellido: "Apellido 1", nombre: "Nombre 1", email: "unEmail@unDominio.com", dni: "20.000.000",
		  lenguajes: [lenguaje1], cursosAprobados: [cursoAprobado4, cursoAprobado1], localidad: localidad1)
	candidato5(org.sigma.code.Candidato, apellido: "Apellido 1", nombre: "Nombre 1", email: "unEmail@unDominio.com", dni: "20.000.000", 
		  lenguajes: [lenguaje1], cursosAprobados: [cursoAprobado5, cursoAprobado2, cursoAprobado3], localidad: localidad1)
}