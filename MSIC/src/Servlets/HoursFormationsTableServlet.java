package Servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import control.SQL.MYSQLCMD;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HoursFormationsTableServlet")
public class HoursFormationsTableServlet extends HttpServlet {

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
            String select = "select id, " + sqlcmd.schemaName + ".DEPARTAMENT.denumire as denumire_departament, denumire_completa, "
            + sqlcmd.schemaName + ".MATERIE.denumire as denumire_materie, " + sqlcmd.schemaName + ".ACTIVITATE_DE_PREDARE.denumire as denumire_activitate, ore, semestru from " + 
            sqlcmd.schemaName + ".FORMATIUNE_MATERIE, " + sqlcmd.schemaName + ".DEPARTAMENT, " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU, " + sqlcmd.schemaName + 
            ".MATERIE, " + sqlcmd.schemaName + ".ACTIVITATE_DE_PREDARE where " + sqlcmd.schemaName + ".FORMATIUNE_MATERIE.id_departament=" + sqlcmd.schemaName + 
            ".DEPARTAMENT.id_departament and " + sqlcmd.schemaName + ".FORMATIUNE_MATERIE.id_formatiune=" + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU.id_formatiune and " + 
            sqlcmd.schemaName + ".FORMATIUNE_MATERIE.id_materie=" + sqlcmd.schemaName + ".MATERIE.id_materie and " + sqlcmd.schemaName + ".FORMATIUNE_MATERIE.id_activitate=" + 
            sqlcmd.schemaName + ".ACTIVITATE_DE_PREDARE.id_activitate";
            
            String order = " order by denumire_departament, denumire_completa, denumire_materie, semestru, denumire_activitate";
            if (req.getParameter("all").equals("yes"))
            {
                resp.getWriter().print(gson.toJson(sqlcmd.getHoursFormations(select + order)));
            }
            else
            {
                resp.getWriter().print(gson.toJson(sqlcmd.getHoursFormations(select + " and semestru=" + req.getParameter("semester") + order)));
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
        String query2 = null;
        String result = null;
        Gson gson = new Gson(); 
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        
        switch (operation) {
            case "insert":
                
                ArrayList<Integer> commonSubjects = gson.fromJson(req.getParameter("hours_formations_common_subjects"), type);
                
                query = "insert into " + sqlcmd.schemaName + ".FORMATIUNE_MATERIE(id, id_departament, id_formatiune, id_materie, id_activitate, ore, semestru) values(" + 
                        req.getParameter("hours_formations_id") + ",'" + req.getParameter("hours_formations_department") + "'," + req.getParameter("hours_formations_formation") +
                        "," + req.getParameter("hours_formations_subject") + "," + req.getParameter("hours_formations_activity") + "," + 
                        req.getParameter("hours_formations_hours") + "," + req.getParameter("hours_formations_semester") + ")";
                
                if (!commonSubjects.isEmpty())
                {
                    query2 = "BEGIN\n";
                    
                    for (int i = 0; i < commonSubjects.size(); i++)
                    {
                        query2 += "insert into " + sqlcmd.schemaName + ".MATERII_COMUNE(id_materie_1, id_materie_2) values(" + req.getParameter("hours_formations_id") + "," + 
                                commonSubjects.get(i) + ");\n";
                        query2 += "insert into " + sqlcmd.schemaName + ".MATERII_COMUNE(id_materie_1, id_materie_2) values(" + commonSubjects.get(i) + "," + 
                                req.getParameter("hours_formations_id") + ");\n";
                    }   
                    query2 += "END;\n";
                }
                
                result = "Informatia a fost adaugata";
                
                break;
                
            case "delete":
                
                query = "delete from " + sqlcmd.schemaName + ".FORMATIUNE_MATERIE where id=" + req.getParameter("hours_formations_id");
                result = "Informatia a fost stearsa";
                
                break;
                
            default:
                break;
        }
        
        try
        {
            sqlcmd.execute(query);
            if (query2 != null)
            {
                sqlcmd.execute(query2);
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
