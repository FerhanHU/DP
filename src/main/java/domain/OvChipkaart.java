package domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OvChipkaart {

    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
    private List<Product> producten;


    public OvChipkaart() {
    }


    public OvChipkaart(int kaartNummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
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
        return producten;
    }



    public boolean addProduct(Product p) {
        if (!producten.contains(p)){
            producten.add(p);
            p.addOvChipkaart(this);
            return true;
        }
        return false;
    }

    public boolean removeProduct(Product p) {
        if (producten.contains(p)){
            producten.remove(p);
            p.removeOvChipkaart(this);
            return true;
        }
        return false;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OvChipkaart)) return false;

        OvChipkaart other = (OvChipkaart) o;

        return kaartNummer == other.kaartNummer &&
                klasse == other.klasse &&
                Double.compare(saldo, other.saldo) == 0 &&
                Objects.equals(geldigTot, other.geldigTot) &&
                Objects.equals(reiziger, other.reiziger) &&
                Objects.equals(producten, other.producten);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kaartNummer, klasse, saldo, geldigTot, reiziger, producten);
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

        if (producten != null && !producten.isEmpty()) {
            string.append("Producten: ");
            for (Product p : producten) {
                string.append(p).append("\n");
            }
        }
        return string.toString();
    }
}


