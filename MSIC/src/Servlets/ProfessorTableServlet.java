package Servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import control.SQL.MYSQLCMD;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfessorTableServlet extends HttpServlet {

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
            String query = "select cnp, nume, " + sqlcmd.schemaName + ".PROFESOR.id_departament, " +
                    sqlcmd.schemaName + ".DEPARTAMENT.denumire as denumire_departament, " + sqlcmd.schemaName + ".FUNCTIE.id_functie, " + sqlcmd.schemaName +
                    ".FUNCTIE.denumire as denumire_functie, prioritate, curs, seminar, lucrari_practice, proiect from " +
                    sqlcmd.schemaName + ".PROFESOR, " + sqlcmd.schemaName + ".DEPARTAMENT, " + sqlcmd.schemaName + ".FUNCTIE where " +
                    sqlcmd.schemaName + ".PROFESOR.id_departament=" + sqlcmd.schemaName + ".DEPARTAMENT.id_departament and " + 
                    sqlcmd.schemaName + ".PROFESOR.id_functie=" + sqlcmd.schemaName + ".FUNCTIE.id_functie";
            
            if (req.getParameter("sortalphabetically").equals("yes"))
            {
                query += " order by nume";
            }
            else
            {
                query += " order by denumire_departament, prioritate, nume";
            }
            resp.getWriter().print(gson.toJson(sqlcmd.getProfessors(query)));
        }
        catch (SQLException ex)
        {
            resp.getWriter().print("Eroare de conectare la server-ul de baze de date");
        }    
                
        resp.getWriter().flush();
        resp.getWriter().close();
    }
    
    private void InsertProfessor(String query, String query2, HttpServletRequest req, Gson gson, Type type)
    {
    	 ArrayList<Integer> insertProfessorSubjects = gson.fromJson(req.getParameter("insertProfessorSubjects"), type);
         ArrayList<Integer> insertProfessorFormationStudy = gson.fromJson(req.getParameter("insertProfessorFormationStudy"), type);
         
         query = "insert into " + sqlcmd.schemaName + ".PROFESOR(cnp, nume, id_departament, id_functie, curs, seminar, lucrari_practice, proiect) values('" + req.getParameter("professor_cnp") + "','" + req.getParameter("professor_name") + "','" + req.getParameter("professor_department") + "'," + req.getParameter("professor_function") + "," + req.getParameter("professor_course") + "," + req.getParameter("professor_seminar") + "," + req.getParameter("professor_laboratory") + "," + req.getParameter("professor_project") + ")";
         
         query2 = "BEGIN\n" + "delete from " + sqlcmd.schemaName + ".PROFESOR_MATERIE where cnp='" + req.getParameter("professor_cnp") + "';\n";
         for (int i = 0; i < insertProfessorSubjects.size(); i++)
         {
             query2 += "insert into " + sqlcmd.schemaName + ".PROFESOR_MATERIE(cnp, id_materie, prioritate) values('" + req.getParameter("professor_cnp") + "'," + insertProfessorSubjects.get(i) + "," + (i + 1) + ");\n";
         }
         
         query2 += "delete from " + sqlcmd.schemaName + ".PROFESOR_FORMATIUNE where cnp='" + req.getParameter("professor_cnp") + "';\n";
         for (int i = 0; i < insertProfessorFormationStudy.size(); i++)
         {
             query2 += "insert into " + sqlcmd.schemaName + ".PROFESOR_FORMATIUNE(cnp, id_formatiune, prioritate) values('" + req.getParameter("professor_cnp") + "'," + insertProfessorFormationStudy.get(i) + "," + (i + 1) + ");\n";
         }
         
         query2 += "END;\n";
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
               
            	InsertProfessor(query, query2, req, gson, type);
                result = "Cadrul didactic a fost adaugat";
                
                break;
            case "update":
                ArrayList<Integer> insertNewProfessorSubjects = gson.fromJson(req.getParameter("insertProfessorSubjects"), type);
                ArrayList<Integer> insertNewProfessorFormationStudy = gson.fromJson(req.getParameter("insertProfessorFormationStudy"), type);
                
                query = "update " + sqlcmd.schemaName + ".PROFESOR set nume='" + req.getParameter("professor_name") + "', id_departament='" + req.getParameter("professor_department") + "', id_functie=" + req.getParameter("professor_function") + ", curs=" + req.getParameter("professor_course") +", seminar=" +req.getParameter("professor_seminar") + ", lucrari_practice=" + req.getParameter("professor_laboratory") + ", proiect=" +req.getParameter("professor_project") + " where cnp='" + req.getParameter("professor_cnp") + "'";
                
                query2 = "BEGIN\n" + "delete from " + sqlcmd.schemaName + ".PROFESOR_MATERIE where cnp='" + req.getParameter("professor_cnp") + "';\n";
                for (int i = 0; i < insertNewProfessorSubjects.size(); i++)
                {
                    query2 += "insert into " + sqlcmd.schemaName + ".PROFESOR_MATERIE(cnp, id_materie, prioritate) values('" + req.getParameter("professor_cnp") + "'," + insertNewProfessorSubjects.get(i) + "," + (i + 1) + ");\n";
                }
                
                query2 += "delete from " + sqlcmd.schemaName + ".PROFESOR_FORMATIUNE where cnp='" + req.getParameter("professor_cnp") + "';\n";
                for (int i = 0; i < insertNewProfessorFormationStudy.size(); i++)
                {
                    query2 += "insert into " + sqlcmd.schemaName + ".PROFESOR_FORMATIUNE(cnp, id_formatiune, prioritate) values('" + req.getParameter("professor_cnp") + "'," + insertNewProfessorFormationStudy.get(i) + "," + (i + 1) + ");\n";
                }
                
                query2 += "END;\n";
                result = "Cadrul didactic a fost actualizat";
                
                break;
            case "delete":
                
                query = "delete from " + sqlcmd.schemaName + ".PROFESOR where cnp=" + req.getParameter("professor_cnp");
                result = "Cadrul didactic a fost sters";
                
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
