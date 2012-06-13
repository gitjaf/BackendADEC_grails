package org.sigma.code



class CORSFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
				response.setHeader("Access-Control-Allow-Origin", "*")
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
