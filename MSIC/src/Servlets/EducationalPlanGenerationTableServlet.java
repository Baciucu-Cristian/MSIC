package Servlets;

import Models.EducationalPlan;
import Models.EducationalPlanHoursAndCredits;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.DocumentException;
import control.SQL.MYSQLCMD;
import control.SiteSettings.PDFInformation;
import control.export.ExportEducationalPlan;
import java.io.FileNotFoundException;
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

public class EducationalPlanGenerationTableServlet extends HttpServlet {

    MYSQLCMD sqlcmd;
    ExportEducationalPlan export;
    PDFInformation pdfInformation;

    @Override
    public void init() throws ServletException
    {
        ServletContext servletContext = this.getServletContext();
        String webSiteLocation = servletContext.getRealPath("");
        sqlcmd = new MYSQLCMD(webSiteLocation);
        pdfInformation = new PDFInformation(webSiteLocation);
    }
    
    public String getShortName(String text)
    {
        String[] words = text.split(" ");
        String shortText = "";
        for (String word : words) {
            if (word.charAt(0) >= 'A' && word.charAt(0) <= 'Z') {
                shortText += word.charAt(0);
            }
        }
        return shortText;
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
                ".PLAN_DE_INVATAMANT.id_categorie_disciplina=" + sqlcmd.schemaName + ".CATEGORIE_DISCIPLINA.id_categorie_disciplina and " + sqlcmd.schemaName + 
                ".PLAN_DE_INVATAMANT.id_departament='" + req.getParameter("department") + "' and " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT.id_formatiune=" + 
                req.getParameter("formationStudy") + " and an_universitar='" + req.getParameter("year") + "' order by an_universitar desc, an, semestru, " + 
                sqlcmd.schemaName + ".TIP_DISCIPLINA.prioritate, denumire_materie")));
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
        Gson gson = new Gson();
        Type educationalPlanType = new TypeToken<ArrayList<EducationalPlan>>(){}.getType();
        Type hoursType = new TypeToken<ArrayList<EducationalPlanHoursAndCredits>>(){}.getType();
        
        String result = null;
        resp.setContentType("text/plain");
        
        ArrayList<EducationalPlan> educationalPlan = gson.fromJson(req.getParameter("educational_plan_subjects"), educationalPlanType);
        ArrayList<EducationalPlanHoursAndCredits> educational_plan_hours = gson.fromJson(req.getParameter("educational_plan_hours"), hoursType);
        
        String localPath = "\\Planuri de invatamant\\" + getShortName(req.getParameter("educational_plan_department_name")) + "_" + 
                getShortName(req.getParameter("educational_plan_formation_study")) + "_" + req.getParameter("educational_plan_years") + "_plan_educational.pdf";
        
        try
        {
            if (sqlcmd.countRows("select count(*) as randuri from " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT_LINK where id_departament='" + 
                    req.getParameter("educational_plan_department_id") + "' and id_formatiune=" + req.getParameter("educational_plan_formation_study_id") +
                    " and an_universitar='" + req.getParameter("educational_plan_years") + "'") == 0)
            {
                sqlcmd.execute("insert into " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT_LINK(id_departament, id_formatiune, an_universitar, nume_pdf) values('" +
                    req.getParameter("educational_plan_department_id") + "', " + req.getParameter("educational_plan_formation_study_id") + ", '" + 
                    req.getParameter("educational_plan_years") + "','" + localPath + "')");
            }
        }
        catch (SQLException ex)
        {
            result = "Eroare de conectare la server-ul de baze de date, operatia de export nu s-a executat";
        }
        if (result == null)
        {
            ServletContext servletContext = this.getServletContext();

            String webSiteLocation = servletContext.getRealPath("");

            export = new ExportEducationalPlan(req.getParameter("educational_plan_department_name"), req.getParameter("educational_plan_department_id"), 
                    req.getParameter("educational_plan_formation_study"), Integer.parseInt(req.getParameter("educational_plan_year")));

            try
            {
                pdfInformation.readData();
                export.facultate = pdfInformation.facultate;
                export.rector = pdfInformation.rector;
                export.director_departament = pdfInformation.director_departament;
                export.decan = pdfInformation.decan;
                result = export.exportToPDF(educationalPlan, educational_plan_hours, webSiteLocation, localPath);
            }
            catch (DocumentException | FileNotFoundException ex)
            {
                result = "Planul de invatamant nu a fost exportat in urma unei erori aparute";
            }
            if (result == null)
            {
                result = "Planul de invatamant a fost exportat cu succes. ";
            }
            else
            {
                try
                {
                    sqlcmd.execute("delete from " + sqlcmd.schemaName + ".PLAN_DE_INVATAMANT_LINK where id_departament='" + 
                    req.getParameter("educational_plan_department_id") + "' and id_formatiune=" + req.getParameter("educational_plan_formation_study_id") +
                    " and an_universitar='" + req.getParameter("educational_plan_years") + "'");
                }
                catch (SQLException ex1)
                {
                    result = "Eroare de conectare la server-ul de baze de date, operatia de export nu s-a executat";
                }
            }
        }
        resp.getWriter().print(result);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
