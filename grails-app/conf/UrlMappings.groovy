class UrlMappings {

	static mappings = {
//		"/$controller/$action?/$id?"{
//			constraints {
//				// apply constraints here
//			}
//		}
		
		"/usuarios.json"{
			controller = 'usuario'
			action = [GET: "list"]
		}
		
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
