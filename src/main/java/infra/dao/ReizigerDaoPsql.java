package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDaoPsql implements IReizigerDao {

    private Connection conn = null;
    private IOvChipkaartDao ovChipkaartDao;
    private IAdresDao adao;

    public ReizigerDaoPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Reiziger reiziger) throws SQLException {
        String a = """
                INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = conn.prepareStatement(a)) {

            statement.setInt(1, reiziger.getReizigerId());
            statement.setString(2, reiziger.getVoorletters());
            statement.setString(3, reiziger.getTussenvoegsel());
            statement.setString(4, reiziger.getAchternaam());
            statement.setDate(5, reiziger.getGeboortedatum());

            statement.executeUpdate();
            statement.close();

            if (reiziger.getAdres() != null && this.adao != null) {
                this.adao.save(reiziger.getAdres());
            }
        }

    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        String b = """
                UPDATE reiziger
                SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ?
                WHERE reiziger_id = ?
                """;
        try (PreparedStatement statement = conn.prepareStatement(b)) {
            statement.setString(1, reiziger.getVoorletters());
            statement.setString(2, reiziger.getTussenvoegsel());
            statement.setString(3, reiziger.getAchternaam());
            statement.setDate(4, reiziger.getGeboortedatum());
            statement.setInt(5, reiziger.getReizigerId());
            statement.executeUpdate();
        }
        if (reiziger.getAdres() != null && this.adao != null) {
            this.adao.update(reiziger.getAdres());
        }
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        if (reiziger.getAdres() != null && this.adao != null) {
            this.adao.delete(reiziger.getAdres());
        }
        String c = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(c)) {
            statement.setInt(1, reiziger.getReizigerId());
            statement.executeUpdate();
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String d = "SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(d)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Reiziger reiziger = new Reiziger();
                    reiziger.setReizigerId(rs.getInt("reiziger_id"));
                    reiziger.setVoorletters(rs.getString("voorletters"));
                    reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
                    reiziger.setAchternaam(rs.getString("achternaam"));
                    reiziger.setGeboortedatum(rs.getDate("geboortedatum"));

                    if (this.adao != null) {
                        Adres a = adao.findByReiziger(reiziger);
                        if (a != null) {
                            a.setReiziger(reiziger);
                            reiziger.setAdres(a);
                        }
                    }
                return reiziger;
            }
        }
    }
    return null;
}

@Override
public List<Reiziger> findByGeboorteDatum(Date date) throws SQLException {
    List<Reiziger> reizigers = new ArrayList<>();
    String e = """
            SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum
            FROM reiziger WHERE geboortedatum = ?
            """;
    try (PreparedStatement statement = conn.prepareStatement(e)) {
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
    try (PreparedStatement statement = conn.prepareStatement(f);
         ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {

            Reiziger r = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboortedatum")
            );
            Adres adres = adao.findByReiziger(r);
            if (adres != null) {
                adres.setReiziger(r);
            }
            r.setAdres(adres);
            reizigers.add(r);
        }
    }
    return reizigers;
}

public void setAdresDao(IAdresDao adao) {
    this.adao = adao;
}

public void setOvChipkaartDao(IOvChipkaartDao ovChipkaartDaoPsql) {
    this.ovChipkaartDao = ovChipkaartDaoPsql;
}
    }

