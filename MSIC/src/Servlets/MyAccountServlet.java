package Servlets;

import control.SQL.MYSQLCMD;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyAccountServlet extends HttpServlet {

    MYSQLCMD sqlcmd;

    @Override
    public void init() throws ServletException
    {
        ServletContext servletContext = this.getServletContext();
        String webSiteLocation = servletContext.getRealPath("");
        sqlcmd = new MYSQLCMD(webSiteLocation);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
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
            resp.getWriter().print(sqlcmd.getPassword("select parola from " + sqlcmd.schemaName + "." + table + " where email='" + req.getParameter("email") + "'"));
        }
        catch (SQLException ex)
        {
            resp.getWriter().print("Eroare de conectare la server-ul de baze de date");
        }    
                
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
