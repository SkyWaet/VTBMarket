package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.config.DBConfig;
import com.skywaet.vtbmarket.dao.JdbcDao;
import com.skywaet.vtbmarket.dao.PurchaseDao;
import com.skywaet.vtbmarket.exception.UnknownException;
import com.skywaet.vtbmarket.exception.notfound.PurchaseContentNotFoundException;
import com.skywaet.vtbmarket.exception.notfound.PurchaseNotFoundException;
import com.skywaet.vtbmarket.model.DeliveryOrder;
import com.skywaet.vtbmarket.model.Purchase;
import com.skywaet.vtbmarket.model.PurchaseContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.skywaet.vtbmarket.util.ResultSetParsers.*;

@Repository
public class PurchaseJdbcDao extends JdbcDao implements PurchaseDao {
    @Autowired
    public PurchaseJdbcDao(DBConfig config) {
        super(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public List<Purchase> findAll() {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT * FROM purchases pur " +
                            "JOIN users u on u.id = pur.client_id WHERE NOT pur.is_deleted");
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<Purchase> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(parsePurchase(resultSet));
            }
            return result;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Purchase findById(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT * FROM purchases pur " +
                            "JOIN users u on u.id = pur.client_id WHERE pur.id = ? AND NOT pur.is_deleted; ");
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new PurchaseNotFoundException(id);
            }
            Purchase purchase = parsePurchase(resultSet);

            final PreparedStatement getDeliveryOrders =
                    getConnection().prepareStatement("SELECT * FROM delivery_orders d " +
                            "WHERE d.purchase_id=? AND NOT d.is_deleted");
            getDeliveryOrders.setLong(1, id);
            List<DeliveryOrder> deliveryOrders = new ArrayList<>();
            final ResultSet deliveryOrdersRs = getDeliveryOrders.executeQuery();
            while (deliveryOrdersRs.next()) {
                deliveryOrders.add(parseDeliveryOrder(deliveryOrdersRs));
            }
            purchase.setDeliveryOrders(deliveryOrders);

            List<PurchaseContent> content = new ArrayList<>();
            final PreparedStatement getContent =
                    getConnection().prepareStatement("SELECT * FROM purchase_contents pc " +
                            "JOIN products p on p.id = pc.product_id WHERE pc.purchase_id = ? " +
                            "AND NOT pc.is_deleted AND NOT p.is_deleted");
            getContent.setLong(1, id);
            final ResultSet contentRs = getContent.executeQuery();
            while (contentRs.next()) {
                PurchaseContent elem = parsePurchaseContent(contentRs);
                elem.setPurchase(purchase);
                content.add(elem);
            }

            purchase.setContent(content);
            return purchase;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Purchase create(Purchase newEntity) {
        try {
            final PreparedStatement createPurchase =
                    getConnection().prepareStatement("INSERT INTO purchases(client_id,sum_of_payment,pur_status) " +
                            "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

            createPurchase.setLong(1, newEntity.getClient().getId());
            createPurchase.setDouble(2, newEntity.getSumOfPayment());
            createPurchase.setString(3, newEntity.getStatus());

            createPurchase.executeUpdate();
            ResultSet rs = createPurchase.getGeneratedKeys();
            if (rs.next()) {
                newEntity.setId(rs.getLong("id"));
                List<PurchaseContent> content = newEntity.getContent();
                for (int i = 0; i < content.size(); i++) {
                    content.get(i).setId(saveContent(content.get(i)));
                }
            } else {
                throw new UnknownException("Purchase was not saved");
            }

            return newEntity;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Purchase update(Long id, Purchase newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE purchases " +
                            "SET client_id = ?, sum_of_payment = ?, pur_status = ?" +
                            "WHERE id=? AND NOT is_deleted");

            preparedStatement.setLong(1, newEntity.getClient().getId());
            preparedStatement.setDouble(2, newEntity.getSumOfPayment());
            preparedStatement.setString(3, newEntity.getStatus());
            preparedStatement.setLong(4, id);

            if (preparedStatement.executeUpdate() == 1L) {
                for (var elem : newEntity.getContent()) {
                    updateContent(elem);
                }
                return findById(id);
            } else {
                throw new PurchaseNotFoundException(id);
            }

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            final PreparedStatement deletePurchases =
                    getConnection().prepareStatement("UPDATE purchases SET is_deleted=True WHERE id=?;");

            deletePurchases.setLong(1, id);
            deletePurchases.executeUpdate();
            deleteContent(id);
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try {
            final PreparedStatement clearPurchases =
                    getConnection().prepareStatement("DELETE FROM purchases;");
            final PreparedStatement clearContent =
                    getConnection().prepareStatement("DELETE FROM purchase_contents");
            clearContent.executeUpdate();
            clearPurchases.executeUpdate();

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    private Long saveContent(PurchaseContent content) {
        try {
            final PreparedStatement saveContent =
                    getConnection().prepareStatement("INSERT INTO purchase_contents(purchase_id, product_id, amount)" +
                            "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            saveContent.setLong(1, content.getPurchase().getId());
            saveContent.setLong(2, content.getProduct().getId());
            saveContent.setInt(3, content.getAmount());
            saveContent.executeUpdate();
            ResultSet savedContent = saveContent.getGeneratedKeys();
            if (savedContent.next()) {
                return savedContent.getLong("id");
            } else {
                throw new UnknownException("Purchase was not saved");
            }
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    private void updateContent(PurchaseContent content) {
        try {
            final PreparedStatement saveContent =
                    getConnection().prepareStatement("UPDATE purchase_contents SET purchase_id=?, product_id=?, amount=? WHERE id = ? AND NOT is_deleted;"
                    );
            saveContent.setLong(1, content.getPurchase().getId());
            saveContent.setLong(2, content.getProduct().getId());
            saveContent.setInt(3, content.getAmount());
            saveContent.setLong(4, content.getId());
            int numRowsChanged = saveContent.executeUpdate();
            if (numRowsChanged == 0) {
                throw new PurchaseContentNotFoundException(content.getId());
            }
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    private void deleteContent(Long purchaseId) {
        try {
            final PreparedStatement deleteContent =
                    getConnection().prepareStatement("UPDATE purchase_contents SET is_deleted=True WHERE purchase_id = ? AND NOT is_deleted;"
                    );
            deleteContent.setLong(1, purchaseId);
            deleteContent.executeUpdate();

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }
}
