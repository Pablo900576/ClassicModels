
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
	import java.net.URI;
	import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.stream.Collectors;


	public class Cliente {

		public static void main(String[] args) {
			String request;
			request = request("GET", "http://localhost:8080/ClassicModels/api", "buscar=Pepe");
			System.out.println("Buscar a Pepe:\n"+ request);
			request = request("GET", "http://localhost:8080/ClassicModels/api", "contactos");
			System.out.println("Lista de contactos:\n"+request);
			/*request = request("DELETE", "http://localhost:8080/servletsapirest/api", "borrar=Pepe");
			System.out.println(request);*/
			/*request = request("POST", "http://localhost:8080/ClassicModels/api", "nombre=Pepe&telefono=601001001");
			System.out.println("Post añadir:\n"+request);*/
			request = request("GET", "http://localhost:8080/ClassicModels/api", "contactos");
			System.out.println("Lista de contactos:\n"+request);
			

			Scanner sc = new Scanner(System.in);
			while(true) {
			System.out.println("Menú: ");
            System.out.println("Para mostrar contactos escribe-> listar ");
            System.out.println("Para buscar contacto escribe-> buscar:nombre ");
            System.out.println("Para eliminar contacto escribe-> borrarC:nombre ");
            System.out.println("Para eliminar un telefono escribe-> borrarT:telefono ");
            System.out.println("Para añadir contacto escribe-> añadir:nombre:telefono ");
            System.out.println("Para finalizar introduzca ';'");
            
            String peticion= sc.nextLine();
            
            if(!peticion.equals(";")) {
            	System.out.println("Enviando petición: " + peticion);
            	System.out.println("Respuesta:");
            	System.out.println(tratarPeticion(peticion));
            }else {
            	break;
            }
			}
			sc.close();
		}
		

		public static String request(String method, String url, String query) {
			String response = null;
	        HttpURLConnection con = null;
			try {
				if(method.equals("GET")|| method.equals("DELETE")) {
				con = (HttpURLConnection)  new URI(url + "?" + query).toURL().openConnection();
				con.setRequestMethod(method);}
				else {
					con = (HttpURLConnection) new URI(url).toURL().openConnection();
		            con.setRequestMethod(method);
		            con.setDoOutput(true);
		            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		            try (OutputStream os = con.getOutputStream()) {
		                byte[] input = query.getBytes("utf-8");
		                os.write(input, 0, input.length);
		            }
		           
				}
		        
				
				int responseCode = con.getResponseCode();
		        
		        
		        if (responseCode == HttpURLConnection.HTTP_OK) {
		            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		            response = in.lines().collect(Collectors.joining("\n"));
		        }
		        else
		            System.out.println(responseCode);
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
			finally {
				if (con != null)
					con.disconnect();
			}
	        return response;
		}
		
		
		private static  String tratarPeticion(String cadena) {
			String salida="";
			String q;
	        if (!cadena.contains(":")) {
	        	salida = request("GET", "http://localhost:8080/ClassicModels/api", "contactos");

	        } else {
	            String trozos[] = cadena.split(":");

	            switch (trozos[0].toLowerCase()) {
	                case "buscar":
	                	q=trozos[0]+"="+trozos[1];
	                    salida = request("GET","http://localhost:8080/ClassicModels/api",q);

	                    break;
	                case "borrarc":
	                	q="borrarContacto="+trozos[1];
	                	salida = request("DELETE", "http://localhost:8080/ClassicModels/api", q);

	        			break;
	                case "borrart":
	                	q="borrarTelefono="+trozos[1];
	        			salida = request("DELETE", "http://localhost:8080/ClassicModels/api", q);


	                    break;
	                case "añadir":
	                    if (trozos.length == 3) {
	                        q="nombre="+trozos[1]+"&telefono="+trozos[2];
	                        salida = request("POST", "http://localhost:8080/ClassicModels/api", q);

	                    } else {
	                        salida="Faltan datos"; // Error por parámetros incorrectos
	                    }
         
	                    break;
	                default:
	                    salida="Comando incorrecto"; // Comando no reconocido
	                    break;
	            }
	        }
	        return salida;
	    }

	  

	}

