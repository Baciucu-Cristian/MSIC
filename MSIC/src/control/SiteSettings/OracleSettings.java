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

public class OracleSettings {
    public int port;
    public String hostname;
    public String sid;
    public String sqlserver;
    public String user;
    public String password;
    public String schemaName;
    public Properties oracleProperties;
    public String error;
    public String webSiteLocation;
    String localPath;
    
    public OracleSettings(String webSiteLocation)
    {
        this.webSiteLocation = webSiteLocation;
        localPath = "\\Config\\oraclesetting.txt";
        oracleProperties = new Properties();
    }
    
    public void init()
    {
        port = 0;
        hostname = "hostname";
        sid = "sid";
        sqlserver = "sqlServer";
        user = "user";
        password = "password";
        schemaName = "schema";

        if(!readData())
        {
            writeData();
        }
    }
    
    public void writeData()
    {
        error = "no error";
        oracleProperties.setProperty("port", Integer.toString(port));
        oracleProperties.setProperty("hostname", hostname);
        oracleProperties.setProperty("sid", sid);
        oracleProperties.setProperty("sqlserver", sqlserver);
        oracleProperties.setProperty("user", user);
        oracleProperties.setProperty("password", password);
        oracleProperties.setProperty("schema", schemaName);
        
        try
        {
            FileOutputStream fileOut = new FileOutputStream(webSiteLocation + localPath);
            oracleProperties.store(fileOut, "Oracle Data");
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
        port = Integer.parseInt(oracleProperties.getProperty("port"));
        hostname = oracleProperties.getProperty("hostname");
        sid = oracleProperties.getProperty("sid");
        sqlserver = oracleProperties.getProperty("sqlserver");
        user = oracleProperties.getProperty("user");
        password = oracleProperties.getProperty("password");
        schemaName = oracleProperties.getProperty("schema");
    }
    
    public boolean readData()
    {
        error = "no error";
        try
        {
            FileInputStream fileIn = new FileInputStream(webSiteLocation + localPath);
            oracleProperties.load(fileIn);
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