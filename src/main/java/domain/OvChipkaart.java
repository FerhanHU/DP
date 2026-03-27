package domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OvChipkaart {

    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;

    public OvChipkaart() {
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
    this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
            this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return null;
    }

    public void setProducten(List<Product> producten) {
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Kaartnummer: ").append(kaartNummer).append("\n");
        string.append("Geldig tot: ").append(geldigTot).append("\n");
        string.append("Klasse: ").append(klasse).append("\n");
        string.append("Saldo: €").append(saldo).append("\n");

        if (reiziger != null){
            string.append("Reiziger Id: ").append(reiziger.getReizigerId());
        }
        return string.toString();
    }
}


