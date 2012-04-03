class UrlMappings {

	static mappings = {
//		"/$controller/$action?/$id?"{
//			constraints {
//				// apply constraints here
//			}
//		}
		
		"/usuarios"{
			controller = 'usuario'
			action = [GET: "list"]
		}
		
		"/usuarios/${id}"{
			controller = 'usuario'
			action = [GET: "show"]
			
		}
		
		"/usuarios/edit/${id}"{
			controller = 'usuario'
			action = [GET: "edit"]
		}
		
		"/login"{
			controller = 'static'
			action = 'login'
		}
		
		
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
