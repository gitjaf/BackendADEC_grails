import grails.util.GrailsUtil
import org.sigma.code.*
import org.codehaus.groovy.grails.commons.GrailsApplication

class BootStrap {

	def fixtureLoader // Autowired by Fixtures plugin
	
    def init = { servletContext ->
    	
		boolean isProd = GrailsUtil.environment == GrailsApplication.ENV_PRODUCTION
		
		if (!isProd){
			fixtureLoader.load('usuarioFixtures')    // Ommit the .groovy extension!
			fixtureLoader.load('perfilFixtures')
			assert Usuario.count == 5
			assert Perfil.count == 4
		}
		
	}
    def destroy = {
    }
}
