package Models;

import java.util.ArrayList;

public class Professor {
    public String nume;
    public String CNP;
    public Department departament;
    public Function functie;
    public int curs;
    public int seminar;
    public int lucrari_practice;
    public int proiect;
    public ArrayList<Professor_Subject> materii;
    public ArrayList<Professor_FormationStudy> formatiuni;
    public AllocatedHours allocatedHours;
    public int totalhours;


	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCNP() {
		return CNP;
	}

	public void setCNP(String cNP) {
		CNP = cNP;
	}

	public Department getDepartament() {
		return departament;
	}

	public void setDepartament(Department departament) {
		this.departament = departament;
	}

	public Function getFunctie() {
		return functie;
	}

	public void setFunctie(Function functie) {
		this.functie = functie;
	}

	public int getCurs() {
		return curs;
	}

	public void setCurs(int curs) {
		this.curs = curs;
	}

	public int getSeminar() {
		return seminar;
	}

	public void setSeminar(int seminar) {
		this.seminar = seminar;
	}

	public int getLucrari_practice() {
		return lucrari_practice;
	}

	public void setLucrari_practice(int lucrari_practice) {
		this.lucrari_practice = lucrari_practice;
	}

	public int getProiect() {
		return proiect;
	}

	public void setProiect(int proiect) {
		this.proiect = proiect;
	}

	public ArrayList<Professor_Subject> getMaterii() {
		return materii;
	}

	public void setMaterii(ArrayList<Professor_Subject> materii) {
		this.materii = materii;
	}

	public ArrayList<Professor_FormationStudy> getFormatiuni() {
		return formatiuni;
	}

	public void setFormatiuni(ArrayList<Professor_FormationStudy> formatiuni) {
		this.formatiuni = formatiuni;
	}

	public AllocatedHours getAllocatedHours() {
		return allocatedHours;
	}

	public void setAllocatedHours(AllocatedHours allocatedHours) {
		this.allocatedHours = allocatedHours;
	}

	public int getTotalhours() {
		return totalhours;
	}

	public void setTotalhours(int totalhours) {
		this.totalhours = totalhours;
	}
    
    
}
