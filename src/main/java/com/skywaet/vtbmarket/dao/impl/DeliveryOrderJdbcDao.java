package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.config.DBConfig;
import com.skywaet.vtbmarket.dao.DeliveryOrderDao;
import com.skywaet.vtbmarket.dao.JdbcDao;
import com.skywaet.vtbmarket.exception.UnknownException;
import com.skywaet.vtbmarket.exception.notfound.DeliveryOrderNotFoundException;
import com.skywaet.vtbmarket.model.DeliveryOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.skywaet.vtbmarket.util.ResultSetParsers.parseDeliveryOrder;

@Repository
public class DeliveryOrderJdbcDao extends JdbcDao implements DeliveryOrderDao {
    @Autowired
    public DeliveryOrderJdbcDao(DBConfig config) {
        super(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public List<DeliveryOrder> findAll() {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT d.id AS d_id, " +
                            "d.date_time, d.location, d.addressee, d.contact_number, d.status AS d_status," +
                            "pur.id AS pur_id, pur.sum_of_payment, pur.status AS pur_status, " +
                            "u.id AS user_id, u.login, u.password, u.user_name, u.surname, u.address, u.phone_number" +
                            " FROM delivery_orders AS d " +
                            "JOIN purchases AS pur on pur.id = d.purchase_id " +
                            "JOIN users AS u on pur.client_id = u.id " +
                            "WHERE NOT d.is_deleted;");
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<DeliveryOrder> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(parseDeliveryOrder(resultSet));
            }
            return result;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public DeliveryOrder findById(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT d.id AS d_id, " +
                            "d.date_time, d.location, d.addressee, d.contact_number, d.status AS d_status," +
                            "pur.id AS pur_id, pur.sum_of_payment, pur.status AS pur_status, " +
                            "u.id AS user_id, u.login, u.password, u.user_name, u.surname, u.address, u.phone_number" +
                            " FROM delivery_orders AS d " +
                            "JOIN purchases AS pur on pur.id = d.purchase_id " +
                            "JOIN users AS u on pur.client_id = u.id " +
                            "WHERE d.id = ? AND NOT d.is_deleted;");
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new DeliveryOrderNotFoundException(id);
            }
            return parseDeliveryOrder(resultSet);
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public DeliveryOrder create(DeliveryOrder newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("INSERT INTO delivery_orders (purchase_id, date_time, location, addressee, contact_number, status) " +
                            "VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, newEntity.getPurchase().getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(newEntity.getDateTime()));
            preparedStatement.setString(3, newEntity.getLocation());
            preparedStatement.setString(4, newEntity.getAddressee());
            preparedStatement.setString(5, newEntity.getContactNumber());
            preparedStatement.setString(6, newEntity.getStatus());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                newEntity.setId(rs.getLong("id"));
            } else {
                throw new UnknownException("Delivery order was not saved");
            }

            return newEntity;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public DeliveryOrder update(Long id, DeliveryOrder newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE delivery_orders " +
                            "SET date_time = ?, location = ?, addressee = ?, contact_number = ?, status = ?" +
                            "WHERE id=? AND NOT is_deleted");

            preparedStatement.setTimestamp(1, Timestamp.valueOf(newEntity.getDateTime()));
            preparedStatement.setString(2, newEntity.getLocation());
            preparedStatement.setString(3, newEntity.getAddressee());
            preparedStatement.setString(4, newEntity.getContactNumber());
            preparedStatement.setString(5, newEntity.getStatus());
            preparedStatement.setLong(6, id);


            if (preparedStatement.executeUpdate() == 1L) {
                return findById(id);
            } else {
                throw new DeliveryOrderNotFoundException(id);
            }

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE delivery_orders SET is_deleted=True WHERE id=? AND NOT is_deleted;");

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("DELETE FROM delivery_orders");

            preparedStatement.executeUpdate();
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

}
