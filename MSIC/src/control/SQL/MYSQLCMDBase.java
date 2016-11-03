package control.SQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Models.Department;
import Models.ExaminationType;
import Models.Function;
import Models.Professor;
import Models.Professor_FormationStudy;
import Models.Professor_Subject;
import Models2.SubjectCategory;
import Models2.SubjectType;
import control.SiteSettings.OracleSettings;

public class MYSQLCMDBase {
	public Statement statement = null;
    public Connection dbConnection = null;
    public String schemaName;
    OracleSettings oracleSettings;
    
    public void openconnection() throws SQLException
    {
        MyConnection mc = new MyConnection();
        mc.sqlServer = oracleSettings.sqlserver;
        mc.hostname = oracleSettings.hostname;
        mc.port = oracleSettings.port;
        mc.SID = oracleSettings.sid;
        mc.user = oracleSettings.user;
        mc.password = oracleSettings.password;
        dbConnection = mc.getConnection();
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
            	Professor professor = new Professor();
            	professor.nume = rs.getString("NUME");
            	professor.CNP = rs.getString("CNP");
            	professor.departament = new Department(rs.getString("ID_DEPARTAMENT"), rs.getString("DENUMIRE_DEPARTAMENT"));
            	professor.functie = new Function(rs.getInt("ID_FUNCTIE"), rs.getString("DENUMIRE_FUNCTIE"), rs.getInt("PRIORITATE"));
            	professor.curs = rs.getInt("CURS");
            	professor.seminar = rs.getInt("SEMINAR");
            	professor.lucrari_practice = rs.getInt("LUCRARI_PRACTICE");
            	professor.proiect = rs.getInt("PROIECT");
                mylist.add(professor);
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
}
