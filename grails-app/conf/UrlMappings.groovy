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
		
		"/perfiles"{
			controller = 'perfil'
			action =[GET: "list", POST: "save"]
			
		}
		
		"/perfiles/${id}"{
			controller = 'perfil'
			action = [GET: "show", DELETE: "delete", PUT: "update"]
		}
		
		"/login"{
			controller = 'static'
			action = 'login'
		}
		
		
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
