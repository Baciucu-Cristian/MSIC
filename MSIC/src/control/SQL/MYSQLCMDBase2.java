package control.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Models.EducationalPlan;
import Models.FormationStudy;
import Models2.Subject;
import Models2.SubjectCategory;
import Models2.TeachingActivity;
import Models2.ViewEducationalPlan;

public class MYSQLCMDBase2 extends MYSQLCMDBase{
	
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
            	EducationalPlan educationalPlan = new EducationalPlan();
            	educationalPlan.materie = rs.getString("DENUMIRE_MATERIE");
            	educationalPlan.denumire_departament = rs.getString("DENUMIRE_DEPARTAMENT");
            	educationalPlan.denumire_formatiune = rs.getString("DENUMIRE_COMPLETA");
            	educationalPlan.denumire_tip_examinare = rs.getString("DENUMIRE_EXAMINARE");
            	educationalPlan.denumire_tip_disciplina = rs.getString("DENUMIRE_DISCIPLINA");
            	educationalPlan.denumire_categorie_disciplina = rs.getString("DENUMIRE_CATEGORIE");
            	educationalPlan.licenta_master = rs.getString("LICENTA_MASTER");
            	educationalPlan.semestru = rs.getInt("SEMESTRU");
            	educationalPlan.an = rs.getInt("AN");
            	educationalPlan.curs = rs.getInt("CURS");
            	educationalPlan.seminar = rs.getInt("SEMINAR");
            	educationalPlan.lucrari_practice = rs.getInt("LUCRARI_PRACTICE");
            	educationalPlan.proiect = rs.getFloat("PROIECT");
            	educationalPlan.numar_credite = rs.getInt("NUMAR_CREDITE");
            	educationalPlan.an_universitar = rs.getString("AN_UNIVERSITAR");
            	mylist.add(educationalPlan);
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
}
