package control.SQL;

import Models.Admin;
import Models.Department;
import Models.EducationalPlan;
import Models.ExaminationType;
import Models.FormationStudy;
import Models.Function;
import Models.HoursFormations;
import Models.Professor;
import Models.Professor_FormationStudy;
import Models.Professor_Subject;
import Models2.Subject;
import Models2.SubjectCategory;
import Models2.SubjectType;
import Models2.TeachingActivity;
import Models2.User;
import Models2.ViewEducationalPlan;
import control.SiteSettings.OracleSettings;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MYSQLCMD extends MYSQLCMDBase2{

	public MYSQLCMD(String webSiteLocation)
    {
        oracleSettings = new OracleSettings(webSiteLocation);
        oracleSettings.readData();
        schemaName = oracleSettings.schemaName;
    }
    
    public ArrayList <HoursFormations> getHoursFormations(String select) throws SQLException
    {
        ArrayList <HoursFormations> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
            	HoursFormations hoursFormations = new HoursFormations();
            	hoursFormations.id = rs.getInt("ID");
            	hoursFormations.departament  = rs.getString("DENUMIRE_DEPARTAMENT");
            	hoursFormations.formatiune = rs.getString("DENUMIRE_COMPLETA");
            	hoursFormations.materie = rs.getString("DENUMIRE_MATERIE");
            	hoursFormations.activitate = rs.getString("DENUMIRE_ACTIVITATE");
            	hoursFormations.ore = rs.getInt("ORE");
            	hoursFormations.semestru = rs.getInt("SEMESTRU");
            	
                mylist.add(hoursFormations);
            }
        }
        for (HoursFormations hoursFormations : mylist)
        {
            ArrayList<HoursFormations> commonHoursFormation = new ArrayList<>();
            ResultSet rs = statement.executeQuery("select id, " + schemaName + ".DEPARTAMENT.denumire as denumire_departament, denumire_completa, "
                        + schemaName + ".MATERIE.denumire as denumire_materie, " + schemaName + ".ACTIVITATE_DE_PREDARE.denumire as denumire_activitate, ore, semestru from " + 
                        schemaName + ".FORMATIUNE_MATERIE, " + schemaName + ".DEPARTAMENT, " + schemaName + ".FORMATIUNE_DE_STUDIU, " + schemaName + 
                        ".MATERIE, " + schemaName + ".ACTIVITATE_DE_PREDARE, " + schemaName + ".MATERII_COMUNE where " + schemaName + ".FORMATIUNE_MATERIE.id_departament=" + 
                        schemaName + ".DEPARTAMENT.id_departament and " + schemaName + ".FORMATIUNE_MATERIE.id_formatiune=" + schemaName + ".FORMATIUNE_DE_STUDIU.id_formatiune and "
                        + schemaName + ".FORMATIUNE_MATERIE.id_materie=" + schemaName + ".MATERIE.id_materie and " + schemaName + ".FORMATIUNE_MATERIE.id_activitate=" + 
                        schemaName + ".ACTIVITATE_DE_PREDARE.id_activitate and id_materie_2=id and id_materie_1=" + hoursFormations.id + 
                        " order by denumire_departament, denumire_completa, denumire_materie, denumire_activitate");

            while (rs.next())
            {
            	
            	HoursFormations comHoursFormations = new HoursFormations();
            	comHoursFormations.id = rs.getInt("ID");
            	comHoursFormations.departament  = rs.getString("DENUMIRE_DEPARTAMENT");
            	comHoursFormations.formatiune = rs.getString("DENUMIRE_COMPLETA");
            	comHoursFormations.materie = rs.getString("DENUMIRE_MATERIE");
            	comHoursFormations.activitate = rs.getString("DENUMIRE_ACTIVITATE");
            	comHoursFormations.ore = rs.getInt("ORE");
            	comHoursFormations.semestru = rs.getInt("SEMESTRU");
            	
                commonHoursFormation.add(comHoursFormations);
            }
            
            hoursFormations.materii_comune = commonHoursFormation;
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <User> getUsers(String select) throws SQLException
    {
        ArrayList <User> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new User(rs.getString("EMAIL"), ""));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <Admin> getAdmins(String select) throws SQLException
    {
        ArrayList <Admin> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new Admin(rs.getString("EMAIL"), ""));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public String getPassword(String select) throws SQLException
    {
        String password = null;
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                password = rs.getString("PAROLA");
            }
        }
        
        closeconnection();
        
        
        return password;
    }
    
    public void execute(String select) throws SQLException //insert, delete, update
    {
        openconnection();
        
        if (dbConnection != null)
        {
            statement.executeUpdate(select);
        }
        
        closeconnection();
    }
    
    public int countRows(String select) throws SQLException
    {
        int rows = 0; 
        {
            openconnection();
            if (dbConnection != null)
            {
                ResultSet rs = statement.executeQuery(select);
                
                if (rs.next())
                {
                    rows = rs.getInt("RANDURI");
                }
            }
        }
        
        closeconnection();
        
        return rows;
    }
}