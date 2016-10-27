package Servlets;

import control.SQL.MYSQLCMD;
import control.SiteSettings.OracleSettings;
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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    MYSQLCMD sqlcmd;
    OracleSettings oracleSettings;
    
    @Override
    public void init() throws ServletException
    {
        ServletContext servletContext = this.getServletContext();
        String webSiteLocation = servletContext.getRealPath("");
        oracleSettings = new OracleSettings(webSiteLocation);
        oracleSettings.init();
        sqlcmd = new MYSQLCMD(webSiteLocation);
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
        resp.setContentType("text/plain");
        
        try
        {
            if (sqlcmd.countRows("select count(*) as randuri from " + sqlcmd.schemaName + "." + table + " where email='" + email + "' and parola='" + password + "'") != 0)
            {
                resp.addCookie(new Cookie("numeUtilizator", email));
                resp.addCookie(new Cookie("tipUtilizator", req.getParameter("userType")));
                resp.getWriter().print("Cont valid");
            }
            else
            {
                resp.getWriter().print("Nu exista " + req.getParameter("userType") + "ul ,," + email + "\" sau parola introdusa este incorecta!");
            }
        }
        catch (SQLException ex)
        {
            resp.getWriter().print("Eroare de conectare la server-ul de baze de date");
        }
        
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
