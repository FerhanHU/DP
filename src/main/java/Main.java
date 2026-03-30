import domain.Adres;
import domain.OvChipkaart;
import domain.Product;
import domain.Reiziger;
import globals.Database;
import infra.dao.AdresDaoPsql;
import infra.dao.OvChipkaartDaoPsql;
import infra.dao.ProductDaoPsql;
import infra.dao.ReizigerDaoPsql;

import java.sql.*;
import java.util.List;


public class Main {
    static void main() throws SQLException {

        try {

            String url = "jdbc:postgresql://localhost:5432/ovchip";
            String user = "postgres";
            String password = "postgres";

            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "postgres", "postgres")) {
                ReizigerDaoPsql rdao = new ReizigerDaoPsql(connection);
                AdresDaoPsql adao = new AdresDaoPsql(connection);
                OvChipkaartDaoPsql odao = new OvChipkaartDaoPsql(connection);
                ProductDaoPsql productDao = new ProductDaoPsql(connection);

                rdao.setAdresDao(adao);
                rdao.setOvChipkaartDao(odao);
                odao.setProductDao(productDao);


                Reiziger r1 = new Reiziger();
                r1.setReizigerId(99);
                r1.setVoorletters("K");
                r1.setTussenvoegsel("van");
                r1.setAchternaam("Maxima");
                r1.setGeboortedatum(Date.valueOf("1990-05-15"));

                Adres adres = new Adres();
                adres.setAdresId(111);
                adres.setPostcode("1055JH");
                adres.setHuisnummer("12A");
                adres.setStraat("Willemstraat");
                adres.setWoonplaats("Utrecht");

                r1.setAdres(adres);
                adres.setReiziger(r1);

                rdao.save(r1);

                System.out.println("Reiziger en adres opgeslagen:");

                System.out.println(rdao.findById(99));
                System.out.println(adao.findById(111));


                OvChipkaart ov1 = new OvChipkaart();
                ov1.setKaartNummer(5555);
                ov1.setGeldigTot(Date.valueOf("2026-12-31"));
                ov1.setKlasse(2);
                ov1.setSaldo(100);
                ov1.setReiziger(r1);
//
                OvChipkaart ov2 = new OvChipkaart();
                ov2.setKaartNummer(5556);
                ov2.setGeldigTot(Date.valueOf("2026-06-30"));
                ov2.setKlasse(1);
                ov2.setSaldo(50);
                ov2.setReiziger(r1);


                r1.addOvChipkaart(ov1);
                r1.addOvChipkaart(ov2);

                odao.save(ov1);
                odao.save(ov2);

                System.out.println("OV-chipkaarten gekoppeld aan reiziger:");
                List<OvChipkaart> ovLijst = rdao.findById(1).getOvChipkaart();
                for (OvChipkaart ov : ovLijst) {
                    System.out.println(ov);
                }


                Product p1 = new Product(201, "Weekend Vrij", "Gratis reizen in weekend", 50);
                Product p2 = new Product(202, "Dal Voordeel", "Korting buiten de spits", 30);
                Product p3 = new Product(203, "NS Flex", "Flex abonnement", 75);

                productDao.save(p1);
                productDao.save(p2);
                productDao.save(p3);

                System.out.println("Producten opgeslagen:");
                List<Product> allProducts = productDao.findAll();
                for (Product p : allProducts) {
                    System.out.println(p);
                }


                ov1.addProduct(p1);
                ov1.addProduct(p2);

                ov2.addProduct(p2);
                ov2.addProduct(p3);

                odao.update(ov1);
                odao.update(ov2);

                System.out.println("Products gekoppeld aan OV-chipkaarten:");
                for (Product p : productDao.findAll()) {
                    System.out.println(p.getNaam() + " - gekoppeld aan OV-kaarten: " + p.getOvChipKaarten().size());
                }


                ov1.setSaldo(150);
                ov1.removeProduct(p2);
                odao.update(ov1);

                System.out.println("OV1 na update:");
                System.out.println(odao.findById(5555));


                odao.delete(ov1);
                odao.delete(ov2);
                productDao.delete(p1);
                productDao.delete(p2);
                productDao.delete(p3);
                adao.delete(adres);
                rdao.delete(r1);

                System.out.println("Alle testdata verwijderd. Database opgeschoond.");


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
//




//