package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoPsql implements IProductDao {

    private Connection connection;
    private IOvChipkaartDao odao;

    public ProductDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Product product) throws SQLException {
        String query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, product.getProductNummer());
            statement.setString(2, product.getNaam());
            statement.setString(3, product.getBeschrijving());
            statement.setInt(4, product.getPrijs());

            statement.executeUpdate();
            statement.close();
        }
        if (product.getOvChipKaarten() != null) {
            String relQuery = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
            for (OvChipkaart ov : product.getOvChipKaarten()) {
                try (PreparedStatement relStmt = connection.prepareStatement(relQuery)) {
                    relStmt.setInt(1, ov.getKaartNummer());
                    relStmt.setInt(2, product.getProductNummer());
                    relStmt.executeUpdate();
                }
            }
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        String query = "UPDATE product SET naam =?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getNaam());
            statement.setString(2, product.getBeschrijving());
            statement.setInt(3, product.getPrijs());
            statement.executeUpdate();
            statement.close();
        }

        String deleteRel = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteRel)) {
            stmt.setInt(1, product.getProductNummer());
            stmt.executeUpdate();
        }

        if (product.getOvChipKaarten() != null) {
            String insertRel = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
            for (OvChipkaart ov : product.getOvChipKaarten()) {
                try (PreparedStatement stmt = connection.prepareStatement(insertRel)) {
                    stmt.setInt(1, ov.getKaartNummer());
                    stmt.setInt(2, product.getProductNummer());
                    stmt.executeUpdate();
                }
            }
        }

    }

    @Override
    public void delete(Product product) throws SQLException {
        String relQuery = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        try (PreparedStatement relStmt = connection.prepareStatement(relQuery)) {
            relStmt.setInt(1, product.getProductNummer());
            relStmt.executeUpdate();
        }

        String query = "DELETE FROM product WHERE product_nummer = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, product.getProductNummer());
            statement.executeUpdate();
            statement.close();
        }

    }

    @Override
    public Product findById(int id) throws SQLException {
        String query = "SELECT product_nummer, naam, beschrijving, prijs FROM product WHERE product_nummer = ? ";
        Product product = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setProductNummer(rs.getInt("product_nummer"));
                    product.setNaam(rs.getString("naam"));
                    product.setBeschrijving(rs.getString("beschrijving"));
                    product.setPrijs(rs.getInt("prijs"));

                }}}
        if (product != null && odao != null) {
            String ovQuery = "SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?";
            try (PreparedStatement ovStatement = connection.prepareStatement(ovQuery)) {
                ovStatement.setInt(1, product.getProductNummer());
                try (ResultSet ovRs = ovStatement.executeQuery()) {
                    while (ovRs.next()) {
                        int kaartNummer = ovRs.getInt("kaart_nummer");
                        OvChipkaart ov = odao.findById(kaartNummer);
                        if (ov != null) {
                            product.addOvChipkaart(ov);
                            if (!ov.getProducten().contains(product)){
                                ov.addProduct(product);
                            }}}}}}

        return product;
    }

    @Override
    public List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) throws SQLException {
        List<Product> producten = new ArrayList<>();
        String query = "SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs FROM product p JOIN ov_chipkaart_product op ON p.product_nummer = op.product_nummer WHERE op.kaart_nummer = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ovChipkaart.getKaartNummer());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductNummer(rs.getInt("product_nummer"));
                    product.setNaam(rs.getString("naam"));
                    product.setBeschrijving(rs.getString("beschrijving"));
                    product.setPrijs(rs.getInt("prijs"));
                    product.addOvChipkaart(ovChipkaart);
                    producten.add(product);
                }
            }
        }

        return producten;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> alleProducten = new ArrayList<>();
        String query = "SELECT product_nummer, naam, beschrijving, prijs FROM product";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setProductNummer(rs.getInt("product_nummer"));
                product.setNaam(rs.getString("naam"));
                product.setBeschrijving(rs.getString("beschrijving"));
                product.setPrijs(rs.getInt("prijs"));
                alleProducten.add(product);
            }
        }

        if (odao != null) {

            List<OvChipkaart> alleKaarten = odao.findAll();
            for (OvChipkaart ov : alleKaarten) {
                for (Product p : ov.getProducten()) {
                    if (!alleProducten.contains(p)) {
                        alleProducten.add(p);
                    }
                    p.addOvChipkaart(ov);
                }
            }
        }
        return alleProducten;
    }
}
