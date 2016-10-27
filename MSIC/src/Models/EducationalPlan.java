package Models;

public class EducationalPlan {
    public String materie;
    public String cod_disciplina;
    public String denumire_departament;
    public String denumire_formatiune;
    public String denumire_tip_examinare;
    public String denumire_tip_disciplina;
    public String denumire_categorie_disciplina;
    public String licenta_master;
    public int semestru;
    public int an;
    public int curs;
    public int seminar;
    public int lucrari_practice;
    public float proiect;
    public int numar_credite;
    public String an_universitar;  

    public EducationalPlan(String materie, String denumire_departament, String denumire_formatiune, String denumire_tip_examinare, String denumire_tip_disciplina, String denumire_categorie_disciplina, String licenta_master, int semestru, int an, int curs, int seminar, int lucrari_practice, float proiect, int numar_credite, String an_universitar) {
        this.materie = materie;
        this.denumire_departament = denumire_departament;
        this.denumire_formatiune = denumire_formatiune;
        this.denumire_tip_examinare = denumire_tip_examinare;
        this.denumire_tip_disciplina = denumire_tip_disciplina;
        this.denumire_categorie_disciplina = denumire_categorie_disciplina;
        this.licenta_master = licenta_master;
        this.semestru = semestru;
        this.an = an;
        this.curs = curs;
        this.seminar = seminar;
        this.lucrari_practice = lucrari_practice;
        this.proiect = proiect;
        this.numar_credite = numar_credite;
        this.an_universitar = an_universitar;
    }
}
