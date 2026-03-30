package infra.dao;

import domain.Adres;
import domain.IAdresDao;
import domain.IReizigerDao;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdresDaoPsql implements IAdresDao {

    private Connection conn;
    private IReizigerDao rdao;

    public AdresDaoPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Adres adres) throws SQLException {
         String a = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?) ";
        try (PreparedStatement statement = conn.prepareStatement(a)) {

            statement.setInt(1, adres.getAdresId());
            statement.setString(2, adres.getPostcode());
            statement.setString(3, adres.getHuisnummer());
            statement.setString(4, adres.getStraat());
            statement.setString(5, adres.getWoonplaats());
            statement.setInt(6, adres.getReiziger().getReizigerId());

            statement.executeUpdate();
            statement.close();
        }
    }

    @Override
    public void update(Adres adres) throws SQLException {
        String b = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ? WHERE adres_id =? ";
        try (PreparedStatement statement = conn.prepareStatement(b)) {
            statement.setString(1, adres.getPostcode());
            statement.setString(2, adres.getHuisnummer());
            statement.setString(3, adres.getStraat());
            statement.setString(4, adres.getWoonplaats());
            statement.setInt(5, adres.getAdresId());
            statement.executeUpdate();
            statement.close();
        }
    }

    @Override
    public void delete(Adres adres) throws SQLException {
        String c = "DELETE FROM adres WHERE adres_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(c)) {
            statement.setInt(1, adres.getAdresId());
            statement.executeUpdate();
            statement.close();
        }
    }

    @Override
    public Adres findById(int id) throws SQLException {
        String d = "SELECT adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id FROM adres WHERE adres_id = ?";
        Adres adres = null;
        try (PreparedStatement statement = conn.prepareStatement(d)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    adres = new Adres();
                    adres.setAdresId(rs.getInt("adres_id"));
                    adres.setPostcode(rs.getString("postcode"));
                    adres.setHuisnummer(rs.getString("huisnummer"));
                    adres.setStraat(rs.getString("straat"));
                    adres.setWoonplaats(rs.getString("woonplaats"));

                    if (this.rdao != null) {
                        Reiziger reiziger = this.rdao.findById(rs.getInt("reiziger_id"));
                        adres.setReiziger(reiziger);
}
                }
            }
        }
        return adres;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String e = "SELECT adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id FROM adres WHERE reiziger_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(e)) {
            statement.setInt(1, reiziger.getReizigerId());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Adres adres = new Adres();
                    adres.setAdresId(rs.getInt("adres_id"));
                    adres.setPostcode(rs.getString("postcode"));
                    adres.setHuisnummer(rs.getString("huisnummer"));
                    adres.setStraat(rs.getString("straat"));
                    adres.setWoonplaats(rs.getString("woonplaats"));
                    adres.setReiziger(reiziger);

                    return adres;

                }
            }
        }
        return null;
    }


    @Override
    public List<Adres> findAll() throws SQLException {
        ArrayList<Adres> adressen = new ArrayList<>();
        List<Reiziger> reizigers = this.rdao.findAll();

        for (Reiziger reiziger : reizigers) {
            if (reiziger.getAdres() != null) {
                adressen.add(reiziger.getAdres());
            }
        }
        return adressen;
    }

    public void setReizigerDao(IReizigerDao rdao){
        this.rdao = rdao;
    }
}