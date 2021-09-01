package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.config.DBConfig;
import com.skywaet.vtbmarket.dao.JdbcDao;
import com.skywaet.vtbmarket.dao.UserDao;
import com.skywaet.vtbmarket.exception.UnknownException;
import com.skywaet.vtbmarket.exception.notfound.UserNotFoundException;
import com.skywaet.vtbmarket.model.Purchase;
import com.skywaet.vtbmarket.model.Ticket;
import com.skywaet.vtbmarket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.skywaet.vtbmarket.util.ResultSetParsers.*;

@Repository
public class UserJdbcDao extends JdbcDao implements UserDao {

    @Autowired
    public UserJdbcDao(DBConfig config) {
        super(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public List<User> findAll() {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT * FROM users u WHERE NOT is_deleted");
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<User> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(parseUser(resultSet));
            }
            return result;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public User findById(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT * FROM users u WHERE id = ? AND NOT is_deleted;");
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new UserNotFoundException(id);
            }
            User user = parseUser(resultSet);
            List<Purchase> purchases = new ArrayList<>();
            final PreparedStatement getPurchases =
                    getConnection().prepareStatement("SELECT * FROM purchases pur WHERE pur.client_id=? AND NOT is_deleted;");
            getPurchases.setLong(1, id);
            final ResultSet purchasesRs = getPurchases.executeQuery();
            while (purchasesRs.next()) {
                purchases.add(parsePurchase(purchasesRs));
            }
            user.setPurchases(purchases);

            List<Ticket> tickets = new ArrayList<>();
            final PreparedStatement getTickets =
                    getConnection().prepareStatement("SELECT * FROM tickets t WHERE t.author_id=? AND NOT is_deleted");
            getTickets.setLong(1, id);
            final ResultSet ticketsRs = getTickets.executeQuery();
            while (ticketsRs.next()) {
                tickets.add(parseTicket(ticketsRs));
            }
            user.setTickets(tickets);
            return user;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
            //throw new UserNotFoundException(id);
        }
    }

    @Override
    public User create(User newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("INSERT INTO users (login, password, user_name, surname, address, phone_number) " +
                            "VALUES (?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, newEntity.getLogin());
            preparedStatement.setString(2, newEntity.getPassword());
            preparedStatement.setString(3, newEntity.getName());
            preparedStatement.setString(4, newEntity.getSurname());
            preparedStatement.setString(5, newEntity.getAddress());
            preparedStatement.setString(6, newEntity.getPhoneNumber());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return findById(rs.getLong("id"));
            } else {
                throw new UnknownException("User was not saved");
            }

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public User update(Long id, User newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE users " +
                            "SET login = ?, password = ?, user_name = ?, surname = ?, address = ?, phone_number = ?" +
                            "WHERE id=? AND NOT is_deleted");

            preparedStatement.setString(1, newEntity.getLogin());
            preparedStatement.setString(2, newEntity.getPassword());
            preparedStatement.setString(3, newEntity.getName());
            preparedStatement.setString(4, newEntity.getSurname());
            preparedStatement.setString(5, newEntity.getAddress());
            preparedStatement.setString(6, newEntity.getPhoneNumber());
            preparedStatement.setLong(7, id);


            if (preparedStatement.executeUpdate() == 1L) {
                return findById(id);
            } else {
                throw new UserNotFoundException(id);
            }

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }

    }

    @Override
    public void delete(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE users SET is_deleted=True WHERE id = ? AND NOT is_deleted;");

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
                    getConnection().prepareStatement("DELETE FROM users");

            preparedStatement.executeUpdate();
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }


}
