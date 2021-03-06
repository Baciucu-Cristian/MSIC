package Servlets;

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

@WebServlet("/ExaminationTypeTableServlet")
public class ExaminationTypeTableServlet extends HttpServlet {

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
            resp.getWriter().print(gson.toJson(sqlcmd.getExaminationTypes("select * from " + sqlcmd.schemaName + ".TIP_EXAMINARE order by denumire")));
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
                
                query = "insert into " + sqlcmd.schemaName + ".TIP_EXAMINARE(id_tip_examinare, denumire) values(" + req.getParameter("examinationType_id") + ",'" +
                        req.getParameter("examinationType_name") + "')";
                result = "Tipul de examinare a fost adaugat";
                
                break;
            case "update":
                
                query = "update " + sqlcmd.schemaName + ".TIP_EXAMINARE set denumire='" + req.getParameter("examinationType_name") + "' where id_tip_examinare=" +
                        req.getParameter("examinationType_id");
                result = "Tipul de examinare a fost actualizat";
                
                break;
            case "delete":
                
                query = "delete from " + sqlcmd.schemaName + ".TIP_EXAMINARE where id_tip_examinare=" + req.getParameter("examinationType_id");
                result = "Tipul de examinare a fost sters";
                
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
