import grails.util.GrailsUtil
import org.sigma.code.*
import org.codehaus.groovy.grails.commons.GrailsApplication

class BootStrap {

	def fixtureLoader // Autowired by Fixtures plugin
	
    def init = { servletContext ->
    	
		boolean isProd = GrailsUtil.environment == GrailsApplication.ENV_PRODUCTION
		
		if (!isProd){
			fixtureLoader.load('novedadFixtures')
			fixtureLoader.load('seccionFixtures')
			fixtureLoader.load('menuFixtures')
			fixtureLoader.load('historialFixtures')
			fixtureLoader.load('institucionFixtures')   // Ommit the .groovy extension!
			assert Usuario.count == 5
			assert Perfil.count == 4
			assert Novedad.count == 5
			assert Novedad_Sidebar.count == 5
			assert Seccion.count == 5
			assert Menu.count == 5
			assert Historial.count == 5
			assert Institucion.count == 5
			
		}
		
	}
    def destroy = {
    }
}
