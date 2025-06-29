import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;



@WebServlet("/api")
public class APIRestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String consulta = "select numero from telefonos where contacto = ?";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		String val;
		PrintWriter out = new PrintWriter(resp.getWriter());
		if ((val = req.getParameter("buscar")) != null)
			buscar(val, out);			
		else if ((val = req.getParameter("contactos")) != null)
			listar(out);
	}
		
	static void buscar(String nombre, PrintWriter out) {
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
			con = ds.getConnection();
			PreparedStatement statement = con.prepareStatement(consulta);
			statement.setString(1, nombre);
			rs = statement.executeQuery();
			while (rs.next())
				out.println(rs.getString(1));
			out.flush();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	static void listar(PrintWriter out) {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
			con = ds.getConnection();
			statement = con.prepareStatement("select * from telefonos");
			rs= statement.executeQuery();
			Map<String, List<String>> contactos= new HashMap<>();
			while(rs.next()) {
				String nombre= rs.getString(2);
				String telefono= rs.getString(1);
				List<String> telefonos = contactos.get(nombre);
	            if (telefonos == null) {
	                telefonos = new ArrayList<>();
	                contactos.put(nombre, telefonos);
	            }
	            telefonos.add(telefono);    
			}
			for (String nombre : contactos.keySet()) {
	            out.println("NOMBRE: " + nombre);

	            List<String> telefonos = contactos.get(nombre);
	            for (int i = 0; i < telefonos.size(); i++) {
	                out.println("	TELEFONO: " + telefonos.get(i));
	            }
	        }
			
		}catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
			}
		
	}
	static void borrarC(String nombre, PrintWriter out) {
		Connection con = null;
		PreparedStatement statement = null;

		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
			con = ds.getConnection();
			statement = con.prepareStatement("delete from telefonos where contacto = ?");
			statement.setString(1, nombre);
			int rs = statement.executeUpdate();
			if (rs>0) {
				out.println("Eliminado correctamente los telefonos de: "+ nombre);
			}else {
				out.println("Error al intentar eliminar los telefonos del contacto");
			}
			//PARA ELIMINAR TAMBIEN DE LA TABLA CONTACTOS PERO NO TENGO IMPLEMENTADO UN METODO PARA AÑADIR A LA TABLA CONTACTOS ENTONCES DARIA PROBLEMAS
			/*statement = con.prepareStatement("delete from contactos where nombre = ?");
			statement.setString(1, nombre);
			rs=statement.executeUpdate();
			if (rs>0) {
				out.println("Eliminado correctamente el contacto de: "+ nombre);
			}else {
				out.println("Error al intentar eliminar el contacto");
			}
			*/
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	static void borrarT(String telefono, PrintWriter out) {
		Connection con = null;
		PreparedStatement statement = null;
		
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
			con = ds.getConnection();
			
			statement = con.prepareStatement("delete from telefonos where numero = ?");
			statement.setString(1, telefono);
			int rs = statement.executeUpdate();
			if (rs>0) {
				out.println("Eliminado correctamente el telefono: "+ telefono);
			}else {
				out.println("Error al intentar eliminar el telefono");
			}
			out.flush();
					
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Connection con = null;
		PreparedStatement statement = null;
		try {
		String sql= "INSERT INTO telefonos(numero, contacto) VALUES (?, ?)";
		resp.setContentType("text/plain");
		String nombre= req.getParameter("nombre");
		String telefono= req.getParameter("telefono");
		PrintWriter out = new PrintWriter(resp.getWriter());
		
		
		 if(nombre !=null && telefono != null) {
			 Context context = new InitialContext();
				DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
				 con = ds.getConnection();
				 statement = con.prepareStatement(sql);
				statement.setString(1, telefono);
				statement.setString(2, nombre);
				
			 int rs = statement.executeUpdate();
				if (rs>0) {
					out.println("Añadido correctamente el contacto: "+ nombre+"\nCon el numero de telefono: "+telefono);
				}else {
					out.println("Error al intentar añadir el contacto");
				}
		 }else {
			 out.println("Error al recibir el nombre y telefono");
		 }
		 out.flush();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		String val;
		PrintWriter out = new PrintWriter(resp.getWriter());
		if ((val = req.getParameter("borrarTelefono")) != null)
			borrarT(val, out);			
		else if ((val = req.getParameter("borrarContacto")) != null)
			borrarC(val, out);
		out.flush();
	}
	
	
}