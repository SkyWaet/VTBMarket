package com.skywaet.vtbmarket.dao;

import lombok.Getter;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public abstract class JdbcDao {
    private Connection connection;

    private final String dbUrl;
    private final String user;
    private final String pass;

    public JdbcDao(String dbUrl, String user, String pass) {
        this.dbUrl = dbUrl;
        this.user = user;
        this.pass = pass;
    }

    @PostConstruct
    public void connectToDb() throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, user, pass);
    }
}
