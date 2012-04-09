class UrlMappings {

	static mappings = {
//		"/$controller/$action?/$id?"{
//			constraints {
//				// apply constraints here
//			}
//		}
		
		"/usuarios"{
			controller = 'usuario'
			action = [GET: "list", POST: "save"]
		}
		
		"/usuarios/${id}"{
			controller = 'usuario'
			action = [GET: "show", DELETE: "delete", PUT: "update"]
			
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
