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
import Models.Subject;
import Models.SubjectCategory;
import Models.SubjectType;
import Models.TeachingActivity;
import Models.User;
import Models.ViewEducationalPlan;
import control.SiteSettings.OracleSettings;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MYSQLCMD {

    public Statement statement = null;
    public Connection dbConnection = null;
    public String schemaName;
    OracleSettings oracleSettings;
    public MYSQLCMD(String webSiteLocation)
    {
        oracleSettings = new OracleSettings(webSiteLocation);
        oracleSettings.readData();
        schemaName = oracleSettings.schemaName;
    }
    public void openconnection() throws SQLException
    {
        MyConnection mc = new MyConnection();
        dbConnection = mc.getConnection(oracleSettings.sqlserver, oracleSettings.hostname, oracleSettings.port, oracleSettings.sid, oracleSettings.user, oracleSettings.password);
        statement = dbConnection.createStatement();
    }
    public void closeconnection() throws SQLException
    {
        if (statement != null)
        {
            statement.close();
        }
        if (dbConnection != null)
        {
            dbConnection.close();
        }
    }
    
    public ArrayList <Department> getDepartments(String select) throws SQLException
    {
        ArrayList <Department> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new Department(rs.getString("ID_DEPARTAMENT"), rs.getString("DENUMIRE")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <Function> getFunctions(String select) throws SQLException
    {
        ArrayList <Function> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new Function(rs.getInt("ID_FUNCTIE"), rs.getString("DENUMIRE"), rs.getInt("PRIORITATE")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <Professor> getProfessors(String select) throws SQLException
    {
        ArrayList <Professor> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new Professor(rs.getString("NUME"), rs.getString("CNP"), new Department(rs.getString("ID_DEPARTAMENT"), rs.getString("DENUMIRE_DEPARTAMENT")),
                    new Function(rs.getInt("ID_FUNCTIE"), rs.getString("DENUMIRE_FUNCTIE"), rs.getInt("PRIORITATE")), rs.getInt("CURS"), rs.getInt("SEMINAR"), 
                    rs.getInt("LUCRARI_PRACTICE"), rs.getInt("PROIECT")));
            }
        }
        
        for (Professor professor : mylist)
        {
            ArrayList<Professor_Subject> subjectsList = new ArrayList<>();
            ResultSet rs = statement.executeQuery("select " + schemaName + ".MATERIE.id_materie, denumire, prioritate from " + schemaName + ".PROFESOR_MATERIE, " + schemaName
                    + ".MATERIE where " + schemaName + ".PROFESOR_MATERIE.id_materie=" + schemaName + ".MATERIE.id_materie and cnp='" + professor.CNP + "' order by prioritate");

            while (rs.next())
            {
                subjectsList.add(new Professor_Subject(rs.getInt("ID_MATERIE"), rs.getString("DENUMIRE"), rs.getInt("PRIORITATE")));
            }
            
            professor.materii = subjectsList;
            
            ArrayList<Professor_FormationStudy> formationStuddyList = new ArrayList<>();
            rs = statement.executeQuery("select " + schemaName + ".FORMATIUNE_DE_STUDIU.id_formatiune, denumire, prioritate from " + schemaName + ".PROFESOR_FORMATIUNE, "
                    + schemaName + ".FORMATIUNE_DE_STUDIU where " + schemaName + ".PROFESOR_FORMATIUNE.id_formatiune=" + schemaName +
                    ".FORMATIUNE_DE_STUDIU.id_formatiune and cnp='" + professor.CNP + "' order by prioritate");

            while (rs.next())
            {
                formationStuddyList.add(new Professor_FormationStudy(rs.getInt("ID_FORMATIUNE"), rs.getString("DENUMIRE"), rs.getInt("PRIORITATE")));
            }
            
            professor.formatiuni = formationStuddyList;
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <ExaminationType> getExaminationTypes(String select) throws SQLException
    {
        ArrayList <ExaminationType> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new ExaminationType(rs.getInt("ID_TIP_EXAMINARE"), rs.getString("DENUMIRE")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <SubjectType> getSubjectTypes(String select) throws SQLException
    {
        ArrayList <SubjectType> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new SubjectType(rs.getInt("ID_TIP_DISCIPLINA"), rs.getString("DENUMIRE"), rs.getInt("PRIORITATE")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <SubjectCategory> getSubjectCategories(String select) throws SQLException
    {
        ArrayList <SubjectCategory> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new SubjectCategory(rs.getInt("ID_CATEGORIE_DISCIPLINA"), rs.getString("DENUMIRE"), rs.getInt("PRIORITATE")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <TeachingActivity> getTeachingActivities(String select) throws SQLException
    {
        ArrayList <TeachingActivity> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new TeachingActivity(rs.getInt("ID_ACTIVITATE"), rs.getString("DENUMIRE")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <Subject> getSubjects(String select) throws SQLException
    {
        ArrayList <Subject> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new Subject(rs.getInt("ID_MATERIE"), rs.getString("DENUMIRE")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <FormationStudy> getFormationStudy(String select) throws SQLException
    {
        ArrayList <FormationStudy> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new FormationStudy(rs.getInt("ID_FORMATIUNE"), rs.getString("DENUMIRE"), rs.getString("DENUMIRE_COMPLETA"), rs.getInt("PARINTE")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public void updatesDerivedFormationStudy(String completeName) throws SQLException
    {
        ArrayList <FormationStudy> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery("select * from " + schemaName + ".FORMATIUNE_DE_STUDIU where parinte is null and denumire='" 
                + completeName + "'");

            while (rs.next())
            {
                mylist.add(new FormationStudy(rs.getInt("ID_FORMATIUNE"), rs.getString("DENUMIRE"), rs.getString("DENUMIRE_COMPLETA"), rs.getInt("PARINTE")));
            }
        }
        String updates = "";

        for (int i = 0; i < mylist.size(); i++)
        {
            ResultSet rs = statement.executeQuery("select * from " + schemaName + ".FORMATIUNE_DE_STUDIU where parinte=" + Integer.toString(mylist.get(i).id_formatiune));

            while (rs.next())
            {
                mylist.add(new FormationStudy(rs.getInt("ID_FORMATIUNE"), rs.getString("DENUMIRE"), mylist.get(i).denumire_completa + " -> " +rs.getString("DENUMIRE"),
                    rs.getInt("PARINTE")));
                updates += "update " + schemaName + ".FORMATIUNE_DE_STUDIU set denumire_completa='" + mylist.get(i).denumire_completa + " -> " +rs.getString("DENUMIRE") +
                        "' where id_formatiune=" + rs.getInt("ID_FORMATIUNE") + ";\n";
            }
        }
        
        if (!updates.equals(""))
        {
            statement.executeUpdate("BEGIN\n" + updates + "END;\n");
        }
        
        closeconnection();
        
    }
    
    public ArrayList <EducationalPlan> getEducationalPlans(String select) throws SQLException
    {
        ArrayList <EducationalPlan> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);
            while (rs.next())
            {   
                mylist.add(new EducationalPlan(rs.getString("DENUMIRE_MATERIE"), rs.getString("DENUMIRE_DEPARTAMENT"), rs.getString("DENUMIRE_COMPLETA"), 
                        rs.getString("DENUMIRE_EXAMINARE"), rs.getString("DENUMIRE_DISCIPLINA"), rs.getString("DENUMIRE_CATEGORIE"), rs.getString("LICENTA_MASTER"), 
                        rs.getInt("SEMESTRU"), rs.getInt("AN"), rs.getInt("CURS"), rs.getInt("SEMINAR"), rs.getInt("LUCRARI_PRACTICE"), rs.getFloat("PROIECT"), 
                        rs.getInt("NUMAR_CREDITE"), rs.getString("AN_UNIVERSITAR")));
            }
        }
        
        closeconnection();
        
        return mylist;
    }
    
    public ArrayList <ViewEducationalPlan> getViewEducationalPlans(String select) throws SQLException
    {
        ArrayList <ViewEducationalPlan> mylist = new ArrayList();
        
        openconnection();
        
        if (dbConnection != null)
        {
            ResultSet rs = statement.executeQuery(select);

            while (rs.next())
            {
                mylist.add(new ViewEducationalPlan(rs.getString("DENUMIRE"), rs.getString("DENUMIRE_COMPLETA"), rs.getString("AN_UNIVERSITAR"), rs.getString("NUME_PDF")));
            }
        }
        
        closeconnection();
        
        return mylist;
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
                mylist.add(new HoursFormations(rs.getInt("ID"), rs.getString("DENUMIRE_DEPARTAMENT"), rs.getString("DENUMIRE_COMPLETA"), rs.getString("DENUMIRE_MATERIE"),
                    rs.getString("DENUMIRE_ACTIVITATE"), rs.getInt("ORE"), rs.getInt("SEMESTRU")));
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
                commonHoursFormation.add(new HoursFormations(rs.getInt("ID"), rs.getString("DENUMIRE_DEPARTAMENT"), rs.getString("DENUMIRE_COMPLETA"), rs.getString("DENUMIRE_MATERIE"),
                    rs.getString("DENUMIRE_ACTIVITATE"), rs.getInt("ORE"), rs.getInt("SEMESTRU")));
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