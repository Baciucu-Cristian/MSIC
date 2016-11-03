package Models;

import java.util.ArrayList;

public class ProfessorBase {
	public String nume;
    public String CNP;
    public Department departament;
    public Function functie;
    public ArrayList<Professor_Subject> materii;
    public ArrayList<Professor_FormationStudy> formatiuni;
}
