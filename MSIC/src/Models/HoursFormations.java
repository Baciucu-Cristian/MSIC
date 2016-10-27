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

    public HoursFormations(int id, String departament, String formatiune, String materie, String activitate, int ore, int semestru) {
        this.id = id;
        this.departament = departament;
        this.formatiune = formatiune;
        this.materie = materie;
        this.activitate = activitate;
        this.ore = ore;
        this.semestru = semestru;
    }
}
