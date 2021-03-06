package Servlets2;

import com.google.gson.Gson;
import control.SQL.MYSQLCMD;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TeachingActivityTableServlet")
public class TeachingActivityTableServlet extends HttpServlet {

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
        Gson gson = new Gson();

        resp.setContentType("text/plain");
        try
        {
            resp.getWriter().print(gson.toJson(sqlcmd.getTeachingActivities("select * from " + sqlcmd.schemaName + ".ACTIVITATE_DE_PREDARE")));
        }
        catch (SQLException ex)
        {
            resp.getWriter().print("Eroare de conectare la server-ul de baze de date");
        }    
                
        resp.getWriter().flush();
        resp.getWriter().close();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String operation = req.getParameter("operation");
        String query = null;
        String result = null;
        
        switch (operation) {
            case "insert":
                
                query = "insert into " + sqlcmd.schemaName + ".ACTIVITATE_DE_PREDARE(id_activitate, denumire) values(" + req.getParameter("teachingActivity_id") + ",'" +
                        req.getParameter("teachingActivity_name") + "')";
                result = "Activitatea de predare a fost adaugata";
                
                break;
            case "update":
                
                query = "update " + sqlcmd.schemaName + ".ACTIVITATE_DE_PREDARE set denumire='" + req.getParameter("teachingActivity_name") + "' where id_activitate=" +
                        req.getParameter("teachingActivity_id");
                result = "Activitatea de predare a fost actualizata";
                
                break;
            case "delete":
                
                query = "delete from " + sqlcmd.schemaName + ".ACTIVITATE_DE_PREDARE where id_activitate=" + req.getParameter("teachingActivity_id");
                result = "Activitatea de predare a fost stearsa";
                
                break;
                
            default:
                break;
        }
        
        try
        {
            sqlcmd.execute(query);
        }
        catch (SQLException ex)
        {
            result = "Eroare de conectare la server-ul de baze de date";
        }
        
        resp.getWriter().print(result);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
