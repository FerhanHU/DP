package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDaoPsql implements IReizigerDao {

    private Connection conn;
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

            if (reiziger.getAdres() != null && this.adao != null) {
                this.adao.save(reiziger.getAdres());
            }

            if (reiziger.getOvChipkaart() != null && !reiziger.getOvChipkaart().isEmpty() && ovChipkaartDao != null) {
                for (OvChipkaart kaart : reiziger.getOvChipkaart()) {
                    this.ovChipkaartDao.save(kaart);
                }
            }
        }

    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        String b = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(b)) {
            statement.setString(1, reiziger.getVoorletters());
            statement.setString(2, reiziger.getTussenvoegsel());
            statement.setString(3, reiziger.getAchternaam());
            statement.setDate(4, reiziger.getGeboortedatum());
            statement.setInt(5, reiziger.getReizigerId());

            if (reiziger.getAdres() != null && this.adao != null) {
                this.adao.update(reiziger.getAdres());
            }

            if (!reiziger.getOvChipkaart().isEmpty() && ovChipkaartDao != null) {
                for (OvChipkaart kaart : reiziger.getOvChipkaart()) {
                    ovChipkaartDao.update(kaart);
                }
            }
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        String c = "DELETE FROM reiziger WHERE reiziger_id = ?";

        if (reiziger.getAdres() != null && this.adao != null) {
            this.adao.delete(reiziger.getAdres());
        }

        if (this.ovChipkaartDao != null && !reiziger.getOvChipkaart().isEmpty()) {
            for (OvChipkaart kaart : reiziger.getOvChipkaart()) {
                this.ovChipkaartDao.delete(kaart);
                reiziger.removeOvChipkaart(kaart);
            }
        }
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

                    if (this.ovChipkaartDao != null) {
                        List<OvChipkaart> kaarten = this.ovChipkaartDao.findByReiziger(reiziger);
                        if (kaarten != null && !kaarten.isEmpty()) {
                            for (OvChipkaart kaart : kaarten) {
                                kaart.setReiziger(reiziger);
                            }
                            reiziger.setOvChipkaart(kaarten);
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
        String e = "SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger WHERE geboortedatum = ? ";
        try (PreparedStatement statement = conn.prepareStatement(e)) {
            statement.setDate(1, date);
            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
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

                    if (ovChipkaartDao != null) {
                        reiziger.setOvChipkaart(ovChipkaartDao.findByReiziger(reiziger));
                    }

                    reizigers.add(reiziger);
                }
            }
        }

        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        String f = "SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger";
        try (PreparedStatement statement = conn.prepareStatement(f);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {

                Reiziger r = new Reiziger();
                r.setReizigerId(rs.getInt("reiziger_id"));
                r.setVoorletters(rs.getString("voorletters"));
                r.setTussenvoegsel(rs.getString("tussenvoegsel"));
                r.setAchternaam(rs.getString("achternaam"));
                r.setGeboortedatum(rs.getDate("geboortedatum"));

                Adres adres = adao.findByReiziger(r);
                if (adres != null) {
                    adres.setReiziger(r);
                }
                r.setAdres(adres);

                if (this.ovChipkaartDao != null) {
                    r.setOvChipkaart(this.ovChipkaartDao.findByReiziger(r));
                }

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

