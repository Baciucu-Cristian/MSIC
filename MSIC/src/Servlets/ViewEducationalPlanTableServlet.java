package Servlets;

import com.google.gson.Gson;
import control.SQL.MYSQLCMD;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewEducationalPlanTableServlet extends HttpServlet {

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
            resp.getWriter().print(gson.toJson(sqlcmd.getViewEducationalPlans("select " + sqlcmd.schemaName + ".DEPARTAMENT.denumire, denumire_completa, an_universitar, nume_pdf from " + 
                sqlcmd.schemaName + ".PLAN_DE_INVATAMANT_LINK, " + sqlcmd.schemaName + ".DEPARTAMENT, " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU where " + sqlcmd.schemaName + 
                ".PLAN_DE_INVATAMANT_LINK.id_departament=" + sqlcmd.schemaName + ".DEPARTAMENT.id_departament and " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT_LINK.id_formatiune=" + 
                sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU.id_formatiune order by an_universitar, denumire, denumire_completa")));
        }
        catch (SQLException ex)
        {
            resp.getWriter().print("Eroare de conectare la server-ul de baze de date");
        }    
                
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
