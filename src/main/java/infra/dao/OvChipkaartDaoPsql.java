package infra.dao;

import domain.*;
import domain.IReizigerDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OvChipkaartDaoPsql implements IOvChipkaartDao {

    private Connection connection;
    private IReizigerDao rdao;
    private IProductDao productDao;

    public OvChipkaartDaoPsql(Connection connection) {
        this.connection = connection;
    }

    public void setReizigerDao(IReizigerDao rdao) {
        this.rdao = rdao;
    }

    @Override
    public void save(OvChipkaart ovChipkaart) throws SQLException {
        String a = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?) ";
        try (PreparedStatement statement = connection.prepareStatement(a)) {
            statement.setInt(1, ovChipkaart.getKaartNummer());
            statement.setDate(2, ovChipkaart.getGeldigTot());
            statement.setInt(3, ovChipkaart.getKlasse());
            statement.setDouble(4, ovChipkaart.getSaldo());
            statement.setInt(5, ovChipkaart.getReiziger().getReizigerId());

            statement.executeUpdate();
        }
        if (ovChipkaart.getProducten() != null) {
            String relQuery = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
            for (Product p : ovChipkaart.getProducten()) {
                try (PreparedStatement relStmt = connection.prepareStatement(relQuery)) {
                    relStmt.setInt(1, ovChipkaart.getKaartNummer());
                    relStmt.setInt(2, p.getProductNummer());
                    relStmt.executeUpdate();
                }
            }
        }
    }

    @Override
    public void update(OvChipkaart ovChipkaart) throws SQLException {
        String b = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?";
        try (PreparedStatement statement = connection.prepareStatement(b)) {
            statement.setDate(1, ovChipkaart.getGeldigTot());
            statement.setInt(2, ovChipkaart.getKlasse());
            statement.setDouble(3, ovChipkaart.getSaldo());
            statement.setInt(4, ovChipkaart.getReiziger().getReizigerId());
            statement.setInt(5, ovChipkaart.getKaartNummer());
            statement.executeUpdate();
            statement.close();
        }

        if (ovChipkaart.getProducten() != null) {
            String insertRel = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
            for (Product p : ovChipkaart.getProducten()) {
                try (PreparedStatement stmt = connection.prepareStatement(insertRel)) {
                    stmt.setInt(1, ovChipkaart.getKaartNummer());
                    stmt.setInt(2, p.getProductNummer());
                    stmt.executeUpdate();
                }
            }

        }
    }

    @Override
    public void delete(OvChipkaart ovChipkaart) throws SQLException {
        String relQuery = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(relQuery)) {
            stmt.setInt(1, ovChipkaart.getKaartNummer());
            stmt.executeUpdate();
        }
        String c = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement statement = connection.prepareStatement(c)) {
            statement.setInt(1, ovChipkaart.getKaartNummer());
            statement.executeUpdate();
            statement.close();
        }

    }

    @Override
    public OvChipkaart findById(int id) throws SQLException {
        String d = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id  FROM ov_chipkaart WHERE kaart_nummer = ?";
        OvChipkaart ovChipkaart = null;
        try (PreparedStatement statement = connection.prepareStatement(d)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    ovChipkaart = new OvChipkaart();
                    ovChipkaart.setKaartNummer(rs.getInt("kaart_nummer"));
                    ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
                    ovChipkaart.setKlasse(rs.getInt("klasse"));
                    ovChipkaart.setSaldo(rs.getDouble("saldo"));

                    if (rdao != null) {
                        ovChipkaart.setReiziger(rdao.findById(rs.getInt("reiziger_id")));
                    }

                    if (productDao != null) {
                        List<Product> producten = productDao.findByOvChipkaart(ovChipkaart);
                        ovChipkaart.setProducten(producten);

                        for (Product p : producten) {
                            p.addOvChipkaart(ovChipkaart);
                        }
                    }
                }
            }
        }
        return ovChipkaart;
    }


    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        List<OvChipkaart> kaarten = new ArrayList<>();
        String e = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart WHERE reiziger_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(e)) {
            statement.setInt(1, reiziger.getReizigerId());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    OvChipkaart a = new OvChipkaart();
                    a.setKaartNummer(rs.getInt("kaart_nummer"));
                    a.setGeldigTot(rs.getDate("geldig_tot"));
                    a.setKlasse(rs.getInt("klasse"));
                    a.setSaldo(rs.getDouble("saldo"));
                    a.setReiziger(reiziger);


                    if (productDao != null) {
                        List<Product> producten = productDao.findByOvChipkaart(a);
                        a.setProducten(producten);

                        for (Product p : producten) {
                            p.addOvChipkaart(a);
                        }
                    }
                    kaarten.add(a);
                }
            }
        }
        return kaarten;
    }

    @Override
    public List<OvChipkaart> findAll() throws SQLException {
        ArrayList<OvChipkaart> alleKaarten = new ArrayList<>();
        List<Reiziger> reizigers = this.rdao.findAll();

        for (Reiziger reiziger : reizigers) {
            List<OvChipkaart> kaarten = findByReiziger(reiziger);
                alleKaarten.addAll(kaarten);
            }
        return alleKaarten;
    }

    public void setProductDao(IProductDao productDao) {
        this.productDao = productDao;
    }
}
