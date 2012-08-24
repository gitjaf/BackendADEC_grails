package org.sigma.code

import grails.converters.JSON
import java.text.SimpleDateFormat

class CandidatoService {

   Candidato nuevoCandidato(JSON json){
	   
	   Candidato candidatoInstance = new Candidato()
	   
	   bindData(candidatoInstance, json, ['fechaDeNacimiento'])
	   candidatoInstance.fechaDeNacimiento = json.fechaDeNacimiento ? new SimpleDateFormat('yyyy-MM-dd').parse(json.fechaDeNacimiento) : null

	   candidatoInstance.localidad = Localidad.get(json?.idLocalidad)
   	
	   json?.nivelIdiomas?.each{ it ->
		   def lenguaje = new Lenguaje(it)
		   lenguaje.idioma = Idioma.get(it.idioma.id)
		   
		   candidatoInstance.addToLenguajes(lenguaje)
	   } 
	   
	   json?.cursos?.each{it ->
		   def curso = new CursoAprobado()
		   curso.curso = Curso.get(it.idCurso)
		   curso.Institucion = Institucion(it.idInstitucion)
		   curso.fechaFinalizacion = it.fechaFinalizacion
		   
		   candidatoInstance.addToCursosAprobados(curso)
		   
	   }
	   
	   return candidatoInstance
	   
   }
	
	
}
