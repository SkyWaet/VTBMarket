package com.skywaet.vtbmarket.util;

import com.skywaet.vtbmarket.model.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ResultSetParsers {
    public static Category parseCategory(ResultSet rs) throws SQLException {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        return new Category(id, name);
    }

    public static User parseUser(ResultSet resultSet) throws SQLException {
        final long id = resultSet.getLong("id");
        final String login = resultSet.getString("login");
        final String password = resultSet.getString("password");
        final String name = resultSet.getString("user_name");
        final String surname = resultSet.getString("surname");
        final String address = resultSet.getString("address");
        final String phoneNumber = resultSet.getString("phone_number");
        return new User(id, login, password, name, surname, address, phoneNumber);
    }


    public static DeliveryOrder parseDeliveryOrder(ResultSet rs) throws SQLException {
        printRs(rs);
        final Long id = rs.getLong("id");
        final LocalDateTime dateTime = rs.getTimestamp("date_time").toLocalDateTime();
        final String location = rs.getString("location");
        final String addressee = rs.getString("addressee");
        final String contactNumber = rs.getString("contact_number");
        final String status = rs.getString("status");
        final String purId = rs.getString("purchase_id");
        final String clientId = rs.getString("client_id");
        final Double sumOfPayment =rs.getDouble("sum_of_payment");
        return new DeliveryOrder(id, dateTime, location, addressee, contactNumber, status,
                parsePurchase(rs));
    }

    public static Product parseProduct(ResultSet rs) throws SQLException {
        final Long productId = rs.getLong("id");
        final String articleNumber = rs.getString("article_number");
        final String name = rs.getString("name");
        final Double price = rs.getDouble("price");
        final Long categoryId = rs.getLong("category_id");
        final String cat_name = rs.getString("cat_name");

        return new Product(productId, new Category(categoryId, cat_name), articleNumber, name, price);
    }

    public static Purchase parsePurchase(ResultSet rs) throws SQLException {
        printRs(rs);
        final Long id = rs.getLong("id");
        final Double sumOfPayment = rs.getDouble("sum_of_payment");
        final String status = rs.getString("pur_status");
        return new Purchase(id, sumOfPayment, status, parseUser(rs));
    }

    public static PurchaseContent parsePurchaseContent(ResultSet rs) throws SQLException {
        final Long id = rs.getLong("pc.id");
        final Integer amount = rs.getInt("pc.amount");
        return new PurchaseContent(id, amount, parseProduct(rs), null);
    }

    public static Ticket parseTicket(ResultSet rs) throws SQLException {
        final Long id = rs.getLong("id");
        final String message = rs.getString("message");
        final Integer priority = rs.getInt("priority");
        final String status = rs.getString("status");
        return new Ticket(id, message, priority, status, parseUser(rs));
    }

    private static void printRs(ResultSet rs) throws SQLException {
        ResultSetMetaData data = rs.getMetaData();
        for (int i = 1; i <= data.getColumnCount(); i++) {
            System.out.println(data.getColumnLabel(i) + " " + data.getColumnName(i));
        }
    }
}
