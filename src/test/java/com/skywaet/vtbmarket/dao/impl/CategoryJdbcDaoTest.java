package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.VtbMarketApplicationTests;
import com.skywaet.vtbmarket.exception.notfound.CategoryNotFoundException;
import com.skywaet.vtbmarket.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.skywaet.vtbmarket.helper.TestUtils.getCategory;

@SpringBootTest
class CategoryJdbcDaoTest extends VtbMarketApplicationTests {
    @AfterEach
    void clearDb() {
        categoryDao.deleteAll();
    }

    @Test
    void testFindAll() {
        for (int i = 0; i < 5; i++) {
            categoryDao.create(Category.builder().name("cat_name" + i).build());
        }
        Assertions.assertEquals(5, categoryDao.findAll().size());
    }

    @Test
    void testCreateAndGet() {
        Category created = categoryDao.create(getCategory());
        Category fromDb = categoryDao.findById(created.getId());
        Assertions.assertEquals(created, fromDb);
    }

    @Test
    void testFindByNotExistentId() {
        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryDao.findById(1000L));
    }


    @Test
    void testUpdate() {
        Category created = categoryDao.create(getCategory());
        created.setName("new name");
        Category updated = categoryDao.update(created.getId(), created);
        Category fromDb = categoryDao.findById(updated.getId());
        Assertions.assertEquals(updated, fromDb);
    }

    @Test
    void testUpdateByNonExistentId() {
        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryDao.update(1000L, getCategory()));
    }

    @Test
    void delete() {
        Category created = categoryDao.create(getCategory());
        categoryDao.delete(created.getId());
        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryDao.findById(created.getId()));
    }
}