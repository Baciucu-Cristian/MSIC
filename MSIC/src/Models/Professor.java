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

    public Professor(String nume, String CNP, Department departament, Function functie, int curs, int seminar, int lucrari_practice, int proiect) {
        this.nume = nume;
        this.CNP = CNP;
        this.departament = departament;
        this.functie = functie;
        this.curs = curs;
        this.seminar = seminar;
        this.lucrari_practice = lucrari_practice;
        this.proiect = proiect;
    }
}
