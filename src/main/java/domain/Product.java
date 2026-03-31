package domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Product {

    private int productNummer;
    private String naam;
    private String beschrijving;
    private int prijs;
    private List<OvChipkaart> ovChipkaarten;


    public Product() {
    }

    public Product(int productNummer, String naam, String beschrijving, int prijs) {
    this.productNummer = productNummer;
    this.naam = naam;
    this.beschrijving = beschrijving;
    this.prijs = prijs;
    }

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public int getPrijs() {
        return prijs;
    }

    public void setPrijs(int prijs) {
        this.prijs = prijs;
    }

    public List<OvChipkaart> getOvChipKaarten() {
        return this.ovChipkaarten;
    }

    public boolean addOvChipkaart(OvChipkaart kaart) {
        if(!ovChipkaarten.contains(kaart)){
            ovChipkaarten.add(kaart);
            kaart.addProduct(this);
            return true;
        }
        return false;
    }

    public boolean removeOvChipkaart(OvChipkaart kaart) {
        if(ovChipkaarten.contains(kaart)){
            ovChipkaarten.remove(kaart);
            return true;
        }
        return false;
    }


    public void setOvChipKaarten(List<OvChipkaart> ovChipKaarten) {
        this.ovChipkaarten = ovChipKaarten;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product other = (Product) o;

        return this.productNummer == other.productNummer &&
                this.prijs == other.prijs &&
                Objects.equals(this.naam, other.naam) &&
                Objects.equals(this.beschrijving, other.beschrijving);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productNummer, naam, beschrijving, prijs);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Productnummer: ").append(productNummer).append("\n");
        string.append("Productnaam: ").append(naam).append("\n");
        string.append("Beschrijving: ").append(beschrijving).append("\n");
        string.append("Prijs: ").append(prijs).append("\n");

        if (ovChipkaarten != null && !ovChipkaarten.isEmpty()) {
            string.append("Ov Chipkaarten: ");
            for (OvChipkaart ovchip : ovChipkaarten) {
                string.append(ovchip.getKaartNummer()).append("\n");
            }
        }
        return string.toString();
    }
}
