package Servlets;

import control.SQL.MYSQLCMD;
import control.SiteSettings.AdminPassword;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    MYSQLCMD sqlcmd;
    AdminPassword adminPassword;
    
    @Override
    public void init() throws ServletException
    {
        ServletContext servletContext = this.getServletContext();
        String webSiteLocation = servletContext.getRealPath("");
        sqlcmd = new MYSQLCMD(webSiteLocation);
        adminPassword = new AdminPassword(webSiteLocation);
        adminPassword.init();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    { 
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String table;
        if (req.getParameter("userType").equals("utilizator"))
        {
            table = "PERSONAL";
        }
        else
        {
            table = "ADMINISTRATOR";
        }
        
        adminPassword.readData();

        if (req.getParameter("adminpassword") == null || req.getParameter("adminpassword").equals(adminPassword.parola))
        {
            try
            {    
                if (sqlcmd.countRows("select count(*) as randuri from " + sqlcmd.schemaName + "." + table + " where email='" + email + "'") != 0)
                {
                    resp.getWriter().print("Exista deja un cont creat cu adresa dumneavoastra de email!");
                }
                else
                {
                    String query = "INSERT INTO " + sqlcmd.schemaName + "." + table + "(EMAIL, PAROLA) VALUES('" + email + "','" + password + "')";
                    sqlcmd.execute(query);
                    resp.addCookie(new Cookie("numeUtilizator", email));
                    resp.addCookie(new Cookie("tipUtilizator", req.getParameter("userType")));
                    resp.getWriter().print("Contul a fost creat");
                }
            }
            catch (SQLException ex)
            {
                resp.getWriter().print("Eroare de conectare la server-ul de baze de date");
            }
        }
        else
        {
            resp.getWriter().print("Parola de administrator este incorecta. Initial aceasta este ,,administrator''." +
                    " In cazul in care a fost schimbata, luati legatura cu un administrator existent pentru aflarea acesteia !");
        }
    }   
}
