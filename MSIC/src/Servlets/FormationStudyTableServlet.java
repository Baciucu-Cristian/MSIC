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

public class FormationStudyTableServlet extends HttpServlet {

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
            if (req.getParameter("all").equals("yes"))
            {
                resp.getWriter().print(gson.toJson(sqlcmd.getFormationStudy("select * from " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU order by denumire_completa")));
        
            }
            else
            {
                resp.getWriter().print(gson.toJson(sqlcmd.getFormationStudy("select * from " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU where parinte is null " + 
                        "order by denumire_completa")));
            }
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
        String completeName = null;
        
        switch (operation) {
            case "insert":
                
                if (req.getParameter("formation_study_parent").equals("0") || req.getParameter("formation_study_parent").equals(req.getParameter("formation_study_id")))
                {
                    query = "insert into " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU(id_formatiune, denumire, denumire_completa) values(" + req.getParameter("formation_study_id")
                            + ",'" + req.getParameter("formation_study_name") + "','" + req.getParameter("formation_study_complete_name") + "')";
                }
                else
                {
                    query = "insert into " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU(id_formatiune, denumire, denumire_completa, parinte) values("
                            + req.getParameter("formation_study_id") + ",'" + req.getParameter("formation_study_name") + "','" + req.getParameter("formation_study_complete_name")
                            + "'," + req.getParameter("formation_study_parent") + ")";
                }
                result = "Formatiunea de studiu a fost adaugata";
                
                break;
            case "update":
                
                if (req.getParameter("formation_study_parent").equals("0"))
                {
                    query = "update " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU set denumire='" + req.getParameter("formation_study_name") + "', denumire_completa='" 
                            + req.getParameter("formation_study_complete_name") + "', parinte=null where id_formatiune=" + req.getParameter("formation_study_id");
                    
                    completeName = req.getParameter("formation_study_complete_name");
                }
                else
                {
                    query = "update " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU set denumire='" + req.getParameter("formation_study_name") + "', denumire_completa='" 
                            + req.getParameter("formation_study_complete_name") + " -> " + req.getParameter("formation_study_name") + "', parinte="
                            + req.getParameter("formation_study_parent") + " where id_formatiune=" + req.getParameter("formation_study_id");
                    completeName = req.getParameter("formation_study_complete_name").split(" -> ")[0];
                }
                result = "Formatiunea de studiu si subformatiunile ale acesteia au fost actualizate";
                
                break;
            case "delete":
                
                query = "delete from " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU where id_formatiune=" + req.getParameter("formation_study_id");
                result = "Formatiunea de studiu si subformatiunile ale acesteia au fost sterse";
                
                break;
                
            default:
                break;
        }
        
        try
        {
            sqlcmd.execute(query);
            
            if (completeName != null)
            {
                sqlcmd.updatesDerivedFormationStudy(completeName);
            }
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
