package infra.dao;

import domain.*;
import domain.IReizigerDao;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OvChipkaartDaoPsql implements IOvChipkaartDao {

    private Connection connection;
    private IReizigerDao rdao;

    public OvChipkaartDaoPsql(Connection connection) {
        this.connection = connection;
        ReizigerDaoPsql reizigerDaoPsql = new ReizigerDaoPsql(connection);
        reizigerDaoPsql.setOvChipkaartDao(this);
        this.rdao = reizigerDaoPsql;
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
            statement.close();

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
    }

    @Override
    public void delete(OvChipkaart ovChipkaart) throws SQLException {
        String c = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement statement = connection.prepareStatement(c)){
            statement.setInt(1, ovChipkaart.getKaartNummer());
            statement.executeUpdate();
            statement.close();
        }
    }

    @Override
    public OvChipkaart findById(int id) throws SQLException {
        String d = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id  FROM ov_chipkaart WHERE kaart_nummer = ?";
        OvChipkaart ovChipkaart = null;
        try (PreparedStatement statement = connection.prepareStatement(d)){
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()){
                    ovChipkaart = new OvChipkaart();
                    ovChipkaart.setKaartNummer(rs.getInt("kaart_nummer"));
                    ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
                    ovChipkaart.setKlasse(rs.getInt("klasse"));
                    ovChipkaart.setSaldo(rs.getDouble("saldo"));

                    if (rdao != null){
                        ovChipkaart.setReiziger(rdao.findById(rs.getInt("reiziger_id")));
                    }
                }
            }
        }
        return ovChipkaart;
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        List<OvChipkaart> kaarten = new ArrayList<>();
        String e = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id from ov_chipkaart WHERE reiziger_id = ?";

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
            List<OvChipkaart> kaarten = reiziger.getOvChipkaart();
            if(kaarten !=null) {
                alleKaarten.addAll(kaarten);
            }
        }
        return alleKaarten;
    }

    public void setProductDao(IProductDao productDao) {

    }
}
