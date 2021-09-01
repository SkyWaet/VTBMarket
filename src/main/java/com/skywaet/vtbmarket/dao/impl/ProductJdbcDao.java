package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.config.DBConfig;
import com.skywaet.vtbmarket.dao.JdbcDao;
import com.skywaet.vtbmarket.dao.ProductDao;
import com.skywaet.vtbmarket.exception.UnknownException;
import com.skywaet.vtbmarket.exception.notfound.CategoryNotFoundException;
import com.skywaet.vtbmarket.exception.notfound.ProductNotFoundException;
import com.skywaet.vtbmarket.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.skywaet.vtbmarket.util.ResultSetParsers.parseProduct;

@Repository
public class ProductJdbcDao extends JdbcDao implements ProductDao {
    @Autowired
    public ProductJdbcDao(DBConfig config) {
        super(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public List<Product> findAll() {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT p.*,c.name as cat_name FROM products p " +
                            "JOIN categories c on c.id = p.category_id WHERE NOT p.is_deleted");
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<Product> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(parseProduct(resultSet));
            }
            return result;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }


    @Override
    public Product findById(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT p.*, c.name as cat_name FROM products p " +
                            "JOIN categories c on c.id = p.category_id WHERE p.id = ? AND NOT p.is_deleted;");
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new ProductNotFoundException(id);
            }
            return parseProduct(resultSet);
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Product create(Product newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("INSERT INTO products (category_id, article_number, name, price) " +
                            "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, newEntity.getCategory().getId());
            preparedStatement.setString(2, newEntity.getArticleNumber());
            preparedStatement.setString(3, newEntity.getName());
            preparedStatement.setDouble(4, newEntity.getPrice());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                newEntity.setId(rs.getLong("id"));
            } else {
                throw new UnknownException("Product was not saved");
            }

            return newEntity;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Product update(Long id, Product newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE products " +
                            "SET category_id=?, article_number=?, name=?, price=?" +
                            "WHERE id=? AND NOT is_deleted");


            preparedStatement.setLong(1, newEntity.getCategory().getId());
            preparedStatement.setString(2, newEntity.getArticleNumber());
            preparedStatement.setString(3, newEntity.getName());
            preparedStatement.setDouble(4, newEntity.getPrice());
            preparedStatement.setLong(5, id);


            if (preparedStatement.executeUpdate() == 1L) {
                return findById(id);
            } else {
                throw new ProductNotFoundException(id);
            }

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE products SET is_deleted=True WHERE id = ? AND NOT is_deleted;");

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
                    getConnection().prepareStatement("DELETE FROM products");

            preparedStatement.executeUpdate();
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }


}
