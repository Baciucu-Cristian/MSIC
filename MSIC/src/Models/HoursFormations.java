package Models;

import java.util.ArrayList;

public class HoursFormations {
    public int id;
    public String departament;
    public String formatiune;
    public String materie;
    public String activitate;
    public int ore;
    public int semestru;
    public ArrayList<HoursFormations> materii_comune;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDepartament() {
		return departament;
	}
	public void setDepartament(String departament) {
		this.departament = departament;
	}
	public String getFormatiune() {
		return formatiune;
	}
	public void setFormatiune(String formatiune) {
		this.formatiune = formatiune;
	}
	public String getMaterie() {
		return materie;
	}
	public void setMaterie(String materie) {
		this.materie = materie;
	}
	public String getActivitate() {
		return activitate;
	}
	public void setActivitate(String activitate) {
		this.activitate = activitate;
	}
	public int getOre() {
		return ore;
	}
	public void setOre(int ore) {
		this.ore = ore;
	}
	public int getSemestru() {
		return semestru;
	}
	public void setSemestru(int semestru) {
		this.semestru = semestru;
	}
	public ArrayList<HoursFormations> getMaterii_comune() {
		return materii_comune;
	}
	public void setMaterii_comune(ArrayList<HoursFormations> materii_comune) {
		this.materii_comune = materii_comune;
	}

    
}
