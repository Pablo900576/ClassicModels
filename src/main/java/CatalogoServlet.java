
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
PrintWriter out = new PrintWriter(response.getWriter());
Connection con = null;
Statement stm = null;
ResultSet rs = null;
try {
Context context = new InitialContext();
DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/classicmodels");
con = ds.getConnection();
stm = con.createStatement();
rs = stm.executeQuery("select productName, productScale, productDescription from products");
out.println("<!DOCTYPE html>");
out.println("<html>");
out.println("<head>");
out.println("<meta charset=<\"UTF-8\">");
out.println("<title>Catálogo de Productos</title>");
out.println("</head>");
out.println("<body>");
out.println("<h1>Catálogo de Modelos a Escala</h1>");
while (rs.next()) {
out.println("<h3>" + rs.getString("productName") + "</h3>");
out.println("<p>Escala " + rs.getString("productScale") + "</p>");
out.println("<p>" + rs.getString("productDescription") + "</p><hr/>");
}
out.println("</body>");
out.println("</html>");
out.println("");
} catch (NamingException | SQLException e) {
e.printStackTrace();
} finally {
if (rs != null) {
try { rs.close(); } catch (SQLException e) {}
}
if (stm != null) {
try { stm.close(); } catch (SQLException e) {}
}
if (con != null) {
try { con.close(); } catch (SQLException e) {}
}
}
}
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
doGet(request, response);
}
}
