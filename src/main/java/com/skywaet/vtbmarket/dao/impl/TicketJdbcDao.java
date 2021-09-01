package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.config.DBConfig;
import com.skywaet.vtbmarket.dao.JdbcDao;
import com.skywaet.vtbmarket.dao.TicketDao;
import com.skywaet.vtbmarket.exception.UnknownException;
import com.skywaet.vtbmarket.exception.notfound.CategoryNotFoundException;
import com.skywaet.vtbmarket.exception.notfound.TicketNotFoundException;
import com.skywaet.vtbmarket.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.skywaet.vtbmarket.util.ResultSetParsers.parseTicket;

@Repository
public class TicketJdbcDao extends JdbcDao implements TicketDao {
    @Autowired
    public TicketJdbcDao(DBConfig config) {
        super(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public List<Ticket> findAll() {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT * FROM tickets t " +
                            "JOIN users u on u.id = t.author_id WHERE NOT t.is_deleted");
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<Ticket> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(parseTicket(resultSet));
            }
            return result;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Ticket findById(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT * FROM tickets t " +
                            "JOIN users u on u.id = t.author_id WHERE t.id = ? AND NOT t.is_deleted;");
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new TicketNotFoundException(id);
            }
            return parseTicket(resultSet);
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Ticket create(Ticket newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("INSERT INTO tickets (author_id, message, priority, status) " +
                            "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, newEntity.getAuthor().getId());
            preparedStatement.setString(2, newEntity.getMessage());
            preparedStatement.setInt(3, newEntity.getPriority());
            preparedStatement.setString(4, newEntity.getStatus());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                newEntity.setId(rs.getLong("id"));
            } else {
                throw new UnknownException("Ticket was not saved");
            }

            return newEntity;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Ticket update(Long id, Ticket newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE tickets " +
                            "SET author_id=?, message=?, priority=?, status=?" +
                            "WHERE id=? AND NOT is_deleted");


            preparedStatement.setLong(1, newEntity.getAuthor().getId());
            preparedStatement.setString(2, newEntity.getMessage());
            preparedStatement.setInt(3, newEntity.getPriority());
            preparedStatement.setString(4, newEntity.getStatus());
            preparedStatement.setLong(5, id);


            if (preparedStatement.executeUpdate() == 1L) {
                return findById(id);
            } else {
                throw new TicketNotFoundException(id);
            }

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE tickets SET is_deleted=True WHERE id = ? AND NOT is_deleted;");

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
                    getConnection().prepareStatement("DELETE FROM tickets;");

            preparedStatement.executeUpdate();
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }
}
