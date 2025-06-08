
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.net.HttpURLConnection;
	import java.net.URI;
	import java.net.URISyntaxException;
	import java.util.stream.Collectors;

	public class Cliente {

		public static void main(String[] args) {
			String request;
			request = request("GET", "http://localhost:8080/servletsapirest/api", "buscar=Pepe");
			System.out.println(request);
			request = request("GET", "http://localhost:8080/servletsapirest/api", "contactos");
			System.out.println(request);
			request = request("DELETE", "http://localhost:8080/servletsapirest/api", "borrar=Pepe");
			System.out.println(request);
			request = request("POST", "http://localhost:8080/servletsapirest/api", "nombre=Pepe&telefono=601001001");
			System.out.println(request);
		}
		

		public static String request(String method, String url, String query) {
			String response = null;
	        HttpURLConnection con = null;
			try {
				con = (HttpURLConnection)  new URI(url + "?" + query).toURL().openConnection();
				con.setRequestMethod(method);
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

	}

