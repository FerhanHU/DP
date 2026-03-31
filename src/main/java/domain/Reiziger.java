package domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

public class Reiziger {

    private int reizigerId;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OvChipkaart> kaarten;

    public Reiziger() {}

    public Reiziger(int reizigerId, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.reizigerId = reizigerId;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
    this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {return achternaam; }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }


    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public domain.Adres getAdres() {
        return adres;
    }

    public void setAdres(domain.Adres adres) {
        this.adres = adres;
    }


    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Reiziger ID: ").append(reizigerId).append("\n");
        string.append("Voorletters: ").append(voorletters).append("\n");
        string.append("Tussenvoegsel: ").append(tussenvoegsel).append("\n");
        string.append("Achternaam: ").append(achternaam).append("\n");
        string.append("Geboortedatum: ").append(geboortedatum).append("\n");

        if(this.getAdres() != null){
            string.append("\nAdres van ").append(this.adres);
        }
        else {
            string.append("\nHeeft geen adres");
        }

        if (this.getOvChipkaart() != null && !this.getOvChipkaart().isEmpty()) {
            for (OvChipkaart kaart : this.getOvChipkaart()) {
                string.append("\nHeeft een OvChipkaart").append(kaart).append("\n");
            }
        }
        else{
            string.append("\nHeeft geen OV-Chipkaart\n");
        }
        return string.toString();
    }


    public List<OvChipkaart> getOvChipkaart() {
        return this.kaarten;
    }

    public boolean addOvChipkaart(OvChipkaart kaart){ // deze functie voegt een kaart toe
        if (!kaarten.contains(kaart))  { // controleert of de lijst met kaarten deze kaart heeft
            return kaarten.add(kaart); // voegt kaart toe aan de lijst.
        }
        return false; // als het toevoegen van de kaart niet lukt
    }


    public boolean removeOvChipkaart(OvChipkaart kaart){
        if (kaarten.contains(kaart)){ //checkt of de lijst met kaarten deze kaart bevat
            return kaarten.remove(kaart); // haal kaart uit de lijst met kaarten
        }
        return false; //
    }


    public void setOvChipkaart(List<OvChipkaart> ovChipkaart) {
        this.kaarten = ovChipkaart;
    }
}