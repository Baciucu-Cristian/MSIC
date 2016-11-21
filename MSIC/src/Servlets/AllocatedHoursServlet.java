package Servlets;

import Models.HoursFormations;
import Models.Professor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.DocumentException;
import control.SiteSettings.PDFInformation;
import control.export.ExportAllocatedHours;
import java.io.FileNotFoundException;
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

@WebServlet("/AllocatedHoursServlet")
public class AllocatedHoursServlet extends HttpServlet {

    ExportAllocatedHours export;
    PDFInformation pdfInformation;
    
    @Override
    public void init() throws ServletException {
        ServletContext servletContext = this.getServletContext();

        String webSiteLocation = servletContext.getRealPath("");
        pdfInformation = new PDFInformation(webSiteLocation);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Gson gson = new Gson();
        Type educationalPlanType = new TypeToken<ArrayList<Professor>>(){}.getType();
        Type hoursType = new TypeToken<ArrayList<HoursFormations>>(){}.getType();
        
        String result;
        resp.setContentType("text/plain");
        
        ArrayList<Professor> professors = gson.fromJson(req.getParameter("allocated_hours_professors"), educationalPlanType);
        ArrayList<HoursFormations> unallocatedHours = gson.fromJson(req.getParameter("unallocated_hours"), hoursType);
        
        String localPath = "\\Repartizare Ore\\repartizare ore sem" + req.getParameter("semester") + ".pdf";
        
        ServletContext servletContext = this.getServletContext();

        String webSiteLocation = servletContext.getRealPath("");

        export = new ExportAllocatedHours();

        try
        {
            pdfInformation.readData();
            export.rector = pdfInformation.rector;
            export.director_departament = pdfInformation.director_departament;
            export.decan = pdfInformation.decan;
            result = export.exportToPDF(professors, unallocatedHours, webSiteLocation, localPath, Integer.parseInt(req.getParameter("semester")));
        }
        catch (DocumentException | FileNotFoundException ex)
        {
            result = "Repartizarea orelor nu a fost exportata in urma unei erori aparute";
        }
        
        if (result == null)
        {
            result = "Repartizarea orelor a fost exportata cu succes.";
        }
                
        resp.getWriter().print(result);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
