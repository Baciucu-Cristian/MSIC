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

public class EducationalPlanTableServlet extends HttpServlet {

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
            resp.getWriter().print(gson.toJson(sqlcmd.getEducationalPlans("select " + sqlcmd.schemaName + ".MATERIE.denumire as denumire_materie, " + 
                sqlcmd.schemaName + ".DEPARTAMENT.denumire as denumire_departament, denumire_completa, " + sqlcmd.schemaName + ".TIP_EXAMINARE.denumire as denumire_examinare, " + 
                sqlcmd.schemaName + ".TIP_DISCIPLINA.denumire as denumire_disciplina, " + sqlcmd.schemaName + ".CATEGORIE_DISCIPLINA.denumire as denumire_categorie, " + 
                "licenta_master, numar_credite, semestru, an, curs, seminar, lucrari_practice, proiect, an_universitar from " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT, " + 
                sqlcmd.schemaName + ".MATERIE, " + sqlcmd.schemaName + ".DEPARTAMENT, " + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU, " + sqlcmd.schemaName + 
                ".TIP_EXAMINARE, " + sqlcmd.schemaName + ".TIP_DISCIPLINA, " + sqlcmd.schemaName + ".CATEGORIE_DISCIPLINA where " + sqlcmd.schemaName + ".MATERIE.id_materie=" + 
                sqlcmd.schemaName + ".PLAN_DE_INVATAMANT.id_materie and " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT.id_departament=" + sqlcmd.schemaName + 
                ".DEPARTAMENT.id_departament and " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT.id_formatiune=" + sqlcmd.schemaName + ".FORMATIUNE_DE_STUDIU.id_formatiune and " + 
                sqlcmd.schemaName + ".PLAN_DE_INVATAMANT.id_tip_examinare=" + sqlcmd.schemaName + ".TIP_EXAMINARE.id_tip_examinare and " + sqlcmd.schemaName + 
                ".PLAN_DE_INVATAMANT.id_tip_disciplina=" + sqlcmd.schemaName + ".TIP_DISCIPLINA.id_tip_disciplina and " + sqlcmd.schemaName + 
                ".PLAN_DE_INVATAMANT.id_categorie_disciplina=" + sqlcmd.schemaName + ".CATEGORIE_DISCIPLINA.id_categorie_disciplina order by an_universitar desc, an, semestru, denumire_materie")));
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
                
                query = "insert into " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT(id_materie, id_departament, id_formatiune, id_tip_examinare, id_tip_disciplina, " + 
                        "id_categorie_disciplina, licenta_master, semestru, an, curs, seminar, lucrari_practice, proiect, numar_credite, an_universitar) values(" 
                        + req.getParameter("educational_plan_subject_id") + ",'" + req.getParameter("educational_plan_department_id") + "'," 
                        + req.getParameter("educational_plan_formation_study_id") + "," + req.getParameter("educational_plan_examination_type_id") + "," 
                        + req.getParameter("educational_plan_subject_type_id") + "," + req.getParameter("educational_plan_subject_category_id") + ",'"
                        + req.getParameter("educational_plan_lincense_master") + "'," + req.getParameter("educational_plan_semester") + ","
                        + req.getParameter("educational_plan_study_year") + "," + req.getParameter("educational_plan_course") + ","
                        + req.getParameter("educational_plan_seminar") + "," + req.getParameter("educational_plan_laboratory") + ","
                        + req.getParameter("educational_plan_project") + "," + req.getParameter("educational_plan_credits") + ",'"
                        + req.getParameter("educational_plan_year") + "')";
                result = "Informatia a fost adaugata";
                
                break;
            
            case "update":
                
                query = "update " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT set id_tip_examinare=" + req.getParameter("educational_plan_examination_type_id") + 
                        ", id_tip_disciplina=" + req.getParameter("educational_plan_subject_type_id") + ", id_categorie_disciplina=" + 
                        req.getParameter("educational_plan_subject_category_id") + ", licenta_master='" + req.getParameter("educational_plan_lincense_master") + 
                        "', semestru=" + req.getParameter("educational_plan_semester") + ", an=" + req.getParameter("educational_plan_study_year") + 
                        ", curs=" + req.getParameter("educational_plan_course") + ", seminar=" + req.getParameter("educational_plan_seminar") + ", lucrari_practice=" + 
                        req.getParameter("educational_plan_laboratory") + ", proiect=" + req.getParameter("educational_plan_project") + ", numar_credite=" + 
                        req.getParameter("educational_plan_credits") + " where id_materie=" + req.getParameter("educational_plan_subject_id") + " and id_departament='" + 
                        req.getParameter("educational_plan_department_id") + "' and id_formatiune=" + req.getParameter("educational_plan_formation_study_id") + 
                        " and an_universitar='" + req.getParameter("educational_plan_year") + "'";
                result = "Informatia a fost actualizata";
                
                break;

            case "delete":
                
                query = "delete from " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT where id_materie=" + req.getParameter("educational_plan_subject_id") + " and "
                        + "id_departament='" + req.getParameter("educational_plan_department_id") + "' and "
                        + "id_formatiune=" + req.getParameter("educational_plan_formation_study_id") + " and "
                        + "an_universitar='" + req.getParameter("educational_plan_year") + "'";
                result = "Informatia a fost stearsa";
                
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
