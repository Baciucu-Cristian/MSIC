package Models;

public class FormationStudy {
    public int id_formatiune;
    public String denumire;
    public String denumire_completa;
    public int parinte;

    public FormationStudy(int id_formatiune, String denumire, String denumire_completa, int parinte) {
        this.id_formatiune = id_formatiune;
        this.denumire = denumire;
        this.denumire_completa = denumire_completa;
        this.parinte = parinte;
    }
}
