import grails.util.GrailsUtil
import org.sigma.code.*
import org.codehaus.groovy.grails.commons.GrailsApplication

class BootStrap {

	def fixtureLoader // Autowired by Fixtures plugin
	
    def init = { servletContext ->
    	
		boolean isProd = GrailsUtil.environment == GrailsApplication.ENV_PRODUCTION
		
		if (!isProd){
			fixtureLoader.load('usuarioFixtures')
			fixtureLoader.load('institucionFixtures')   // Ommit the .groovy extension!
			fixtureLoader.load('localidadFixtures')
			assert Usuario.count == 10
			assert Perfil.count == 9
			assert Novedad.count == 5
			assert Categoria.count == 5
			assert Seccion.count == 5
			assert Menu.count == 5
			assert Historial.count == 5
			assert Institucion.count == 5
			assert Localidad.count == 5
			assert CampoTabla.count == 5
			assert Curso.count == 5
			assert Tab.count == 5
			
			
		}
		
	}
    def destroy = {
    }
}
