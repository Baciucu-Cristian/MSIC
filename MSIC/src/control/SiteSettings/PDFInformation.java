package control.SiteSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class PDFInformation {
    public String facultate;
    public String rector;
    public String director_departament;
    public String decan;
    public Properties adminProperties;
    public String error;
    public String webSiteLocation;
    String localPath;
    
    public PDFInformation(String webSiteLocation)
    {
        this.webSiteLocation = webSiteLocation;
        localPath = "\\Config\\pdfinfo.txt";
        adminProperties = new Properties();
    }
    
    public void init()
    {
        facultate = "facultate";
        rector = "rector";
        director_departament = "director_departament";
        decan = "decan";
        if(!readData())
        {
            writeData();
        }
    }
    
    public void writeData()
    {
        error = "no error";
        adminProperties.setProperty("facultate", facultate);
        adminProperties.setProperty("rector", rector);
        adminProperties.setProperty("director_departament", director_departament);
        adminProperties.setProperty("decan", decan);
        try
        {
            FileOutputStream fileOut = new FileOutputStream(webSiteLocation + localPath);
            adminProperties.store(fileOut, "PDF Data");
            fileOut.close();
        }
        catch (IOException e)
        {
            error = "error";
        }
        
        File source = new File(webSiteLocation + localPath);
        
        Path path = Paths.get(webSiteLocation);
        path = path.getParent().getParent();
        
        String tempPath = path.toAbsolutePath().toString() + "\\web" + localPath;
        File target = new File(tempPath);
        
        try
        {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ex)
        {
        }
    }
    
    void setData()
    {
        facultate = adminProperties.getProperty("facultate");
        rector = adminProperties.getProperty("rector");
        director_departament = adminProperties.getProperty("director_departament");
        decan = adminProperties.getProperty("decan");
    }
    
    public boolean readData()
    {
        error = "no error";
        try
        {
            FileInputStream fileIn = new FileInputStream(webSiteLocation + localPath);
            adminProperties.load(fileIn);
            setData();
            fileIn.close();
        }
        catch(IOException e)
        {
            error = "error";
            return false;
        }
        
        return true;
    }
}
