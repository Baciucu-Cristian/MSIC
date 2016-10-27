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

public class SubjectCategoryTableServlet extends HttpServlet {

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
            resp.getWriter().print(gson.toJson(sqlcmd.getSubjectCategories("select * from " + sqlcmd.schemaName + ".CATEGORIE_DISCIPLINA order by prioritate")));
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
                
                query = "insert into " + sqlcmd.schemaName + ".CATEGORIE_DISCIPLINA(id_categorie_disciplina, denumire, prioritate) values(" +
                        req.getParameter("subjectCategory_id") + ",'" + req.getParameter("subjectCategory_name") + "'," + req.getParameter("subjectCategory_priority") + ")";
                result = "Tipul de categorie a fost adaugat";
                
                break;
            case "update":
                
                query = "update " + sqlcmd.schemaName + ".CATEGORIE_DISCIPLINA set denumire='" + req.getParameter("subjectCategory_name") + "', prioritate=" + 
                        req.getParameter("subjectCategory_priority") + " where id_categorie_disciplina=" + req.getParameter("subjectCategory_id");
                result = "Tipul de categorie a fost actualizat";
                
                break;
            case "delete":
                
                query = "delete from " + sqlcmd.schemaName + ".CATEGORIE_DISCIPLINA where id_categorie_disciplina=" + req.getParameter("subjectCategory_id");
                result = "Tipul de disciplina a fost sters";
                
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
