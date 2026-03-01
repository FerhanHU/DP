package domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Reiziger {

    private int reizigerId;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;

    public Reiziger() {}

    public Reiziger(int reizigerId, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.reizigerId = reizigerId;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
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

    public String ToString() {
        return "Reiziger ID: "+reizigerId+" /nVoorletters: " +voorletters+" /nTussenvoegsel: " +tussenvoegsel+"/nAchternaam: "+achternaam+"/nGeboortedatum: "+geboortedatum;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;


    }

    public domain.Adres getAdres() {
        return null;
    }

    public void setAdres(domain.Adres adres) {

    }

    public List<OvChipkaart> getOvChipkaart() {
        return null;
    }

    public void setOvChipkaart(List<OvChipkaart> ovChipkaart) {
    }
}
