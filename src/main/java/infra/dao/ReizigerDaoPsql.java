package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDaoPsql implements IReizigerDao {

    private Connection connection = null;
    private IOvChipkaartDao ovChipkaartDao;
    private IAdresDao adresDao;

    public ReizigerDaoPsql(Connection connection) {
    this.connection = connection;
    }


    @Override
    public void save(Reiziger reiziger) throws SQLException {
        String a = """
        INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum)
        VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement statement = connection.prepareStatement(a)) {

            statement.setInt(1, reiziger.getReizigerId());
            statement.setString(2, reiziger.getVoorletters());
            statement.setString(3, reiziger.getTussenvoegsel());
            statement.setString(4, reiziger.getAchternaam());
            statement.setDate(5, reiziger.getGeboortedatum());
            statement.executeUpdate();
            statement.close();
        }
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        String b = """
                UPDATE reiziger
                SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ?
                WHERE reiziger_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(b)) {
            statement.setString(1, reiziger.getVoorletters());
            statement.setString(2, reiziger.getTussenvoegsel());
            statement.setString(3, reiziger.getAchternaam());
            statement.setDate(4, reiziger.getGeboortedatum());
            statement.setInt(5, reiziger.getReizigerId());
            statement.executeUpdate();
            statement.close();
        }
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        String c = """
                DELETE FROM reiziger
                WHERE reiziger_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(c)){
            statement.setInt(1, reiziger.getReizigerId());
            statement.executeUpdate();
            statement.close();
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String d = """
                SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum
                FROM reiziger WHERE reiziger_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(d)){
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()){
            if (rs.next()) {
                Reiziger reiziger = new Reiziger();
                reiziger.setReizigerId(rs.getInt("reiziger_id"));
                reiziger.setVoorletters(rs.getString("voorletters"));
                reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
                reiziger.setAchternaam(rs.getString("achternaam"));
                reiziger.setGeboortedatum(rs.getDate("geboortedatum"));

                return reiziger;
            }
            else {
                return null;
            }

            }

        }
    }

    @Override
    public List<Reiziger> findByGeboorteDatum(Date date) throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        String e = """
                SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum
                FROM reiziger WHERE geboortedatum = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(e)) {
            statement.setDate(1, date);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    reizigers.add(new Reiziger(rs.getInt("reiziger_id"),
                            rs.getString("voorletters"),
                            rs.getString("tussenvoegsel"),
                            rs.getString("achternaam"),
                            rs.getDate("geboortedatum")));

                }
            }
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        ArrayList<Reiziger> reizigers = new ArrayList<Reiziger>();
        String f = """
                SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum
                FROM reiziger
                """;
        try (PreparedStatement statement = connection.prepareStatement(f);
        ResultSet rs = statement.executeQuery()) {
    while (rs.next()) {
        Reiziger r = new Reiziger(
                rs.getInt("reiziger_id"),
                rs.getString("voorletters"),
                rs.getString("tussenvoegsel"),
                rs.getString("achternaam"),
                rs.getDate("geboortedatum")
        );
        reizigers.add(r);
    }
    }
        return reizigers;
    }

    public void setAdresDao(IAdresDao adresDaoPsql) {
this.adresDao = adresDaoPsql;
    }

    public void setOvChipkaartDao(IOvChipkaartDao ovChipkaartDaoPsql) {
this.ovChipkaartDao = ovChipkaartDaoPsql;
    }
}
