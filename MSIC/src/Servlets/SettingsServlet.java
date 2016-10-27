package Servlets;

import control.SiteSettings.AdminPassword;
import control.SiteSettings.PDFInformation;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SettingsServlet extends HttpServlet {

    AdminPassword adminPassword;
    PDFInformation pdfInformation;
    
    @Override
    public void init() throws ServletException {
        ServletContext servletContext = this.getServletContext();
        String webSiteLocation = servletContext.getRealPath("");
        
        adminPassword = new AdminPassword(webSiteLocation);
        pdfInformation = new PDFInformation(webSiteLocation);
        pdfInformation.init();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String result = null;
        resp.setContentType("text/plain");
           
        switch (req.getParameter("operation")) {
            
            case "getAdminPassword":
                
                adminPassword.readData();
                if (adminPassword.error.equals("error"))
                {
                    result = "Eroare la preluarea datelor legate de parola de administrator";
                }
                else
                {
                    result = adminPassword.parola;
                }
                break;
                
            case "getPdfInformation":
                if (pdfInformation.error.equals("error"))
                {
                    result = "Eroare la preluarea datelor legate de informatiile pentru PDF";
                }
                else
                {
                    result = "[\"" +pdfInformation.facultate + "\",\"" + pdfInformation.rector + "\",\"" + pdfInformation.director_departament + "\",\"" + pdfInformation.decan + "\"]";
                }
                
                break;
            case "delete":
                
                
                break;
                
            default:
                break;
        }
        
        resp.getWriter().print(result);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String result = null;
        resp.setContentType("text/plain");
        switch (req.getParameter("operation")) {
            
            case "setAdminPassword":
                
                adminPassword.parola = req.getParameter("password");
                adminPassword.writeData();
                if (adminPassword.error.equals("error"))
                {
                    result = "Eroare la preluarea datelor legate de parola de administrator";
                }
                else
                {
                    result = "Parola de administrator a fost actualizata";
                }
                
                break;
                
            case "setPdfInformation":
                  
                pdfInformation.facultate = req.getParameter("faculty");
                pdfInformation.rector = req.getParameter("rector");
                pdfInformation.director_departament = req.getParameter("departmentdirector");
                pdfInformation.decan = req.getParameter("dean");
                pdfInformation.writeData();
                if (pdfInformation.error.equals("error"))
                {
                    result = "Eroare la preluarea datelor legate de informatiile pentru PDF";
                }
                else
                {
                    result = "Informatiile pentru PDF au fost actualizate";
                }
                
                break;
            case "delete":
                
                
                break;
                
            default:
                break;
        }
        
        resp.getWriter().print(result);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
