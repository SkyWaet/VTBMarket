package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.VtbMarketApplicationTests;
import com.skywaet.vtbmarket.exception.notfound.UserNotFoundException;
import com.skywaet.vtbmarket.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserJdbcDaoTest extends VtbMarketApplicationTests {

    @AfterEach
    void clearDb() {
        userDao.deleteAll();
    }

    @Test
    void testFindAll() {
        for (int i = 0; i < 5; i++) {
            userDao.create(User.builder()
                    .login("login" + i)
                    .password("pass" + i)
                    .name("name" + i)
                    .surname("surname" + i)
                    .address("address" + i)
                    .build());
        }
        Assertions.assertEquals(5, userDao.findAll().size());
    }

    @Test
    void testCreateAndGet() {
        User created = userDao.create(sampleUser);
        User fromDb = userDao.findById(created.getId());
        Assertions.assertEquals(created, fromDb);
    }


    @Test
    void testUpdate() {

        User created = userDao.create(sampleUser);
        created.setLogin("login1523");
        User updated = userDao.update(created.getId(), created);
        User fromDb = userDao.findById(updated.getId());
        Assertions.assertEquals(updated, fromDb);
    }

    @Test
    void delete() {
        User created = userDao.create(sampleUser);
        userDao.delete(created.getId());
        Assertions.assertThrows(UserNotFoundException.class, () -> userDao.findById(created.getId()));
    }
}