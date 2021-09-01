package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.config.DBConfig;
import com.skywaet.vtbmarket.dao.CategoryDao;
import com.skywaet.vtbmarket.dao.JdbcDao;
import com.skywaet.vtbmarket.exception.UnknownException;
import com.skywaet.vtbmarket.exception.notfound.CategoryNotFoundException;
import com.skywaet.vtbmarket.model.Category;
import com.skywaet.vtbmarket.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.skywaet.vtbmarket.util.ResultSetParsers.parseCategory;
import static com.skywaet.vtbmarket.util.ResultSetParsers.parseProduct;

@Repository
public class CategoryJdbcDao extends JdbcDao implements CategoryDao {
    @Autowired
    public CategoryJdbcDao(DBConfig config) {
        super(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public List<Category> findAll() {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("SELECT * FROM categories WHERE NOT is_deleted");
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<Category> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(parseCategory(resultSet));
            }
            return result;
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Category findById(Long id) {
        try {
            final PreparedStatement categoryQuery =
                    getConnection().prepareStatement("SELECT * FROM categories WHERE id = ? AND NOT is_deleted;");
            categoryQuery.setLong(1, id);
            final ResultSet categoryRaw = categoryQuery.executeQuery();
            categoryRaw.next();
            Category category = parseCategory(categoryRaw);

            final PreparedStatement productListQuery
                    = getConnection().prepareStatement("SELECT * FROM products WHERE category_id=? AND NOT is_deleted");
            productListQuery.setLong(1, id);
            final ResultSet productsRaw = productListQuery.executeQuery();
            List<Product> products = new ArrayList<>();
            while (productsRaw.next()) {
                products.add(parseProduct(productsRaw));
            }

            category.setProductList(products);
            return category;
        } catch (SQLException err) {
            throw new CategoryNotFoundException(id);
        }
    }

    @Override
    public Category create(Category newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("INSERT INTO categories (name) " +
                            "VALUES (?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, newEntity.getName());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return findById(rs.getLong("id"));
            } else {
                throw new UnknownException("Category was not saved");
            }
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public Category update(Long id, Category newEntity) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE categories " +
                            "SET name=?" +
                            "WHERE id=? AND NOT is_deleted");

            preparedStatement.setString(1, newEntity.getName());
            preparedStatement.setLong(2, id);

            if (preparedStatement.executeUpdate() == 1L) {
                return findById(id);
            } else {
                throw new CategoryNotFoundException(id);
            }

        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            final PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE categories SET is_deleted=True WHERE id = ? AND NOT is_deleted;");

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
                    getConnection().prepareStatement("DELETE FROM categories");

            preparedStatement.executeUpdate();
        } catch (SQLException err) {
            throw new UnknownException(err.getMessage());
        }
    }


}
