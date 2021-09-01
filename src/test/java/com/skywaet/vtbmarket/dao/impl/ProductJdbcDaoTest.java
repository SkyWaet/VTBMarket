package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.VtbMarketApplicationTests;
import com.skywaet.vtbmarket.exception.notfound.ProductNotFoundException;
import com.skywaet.vtbmarket.model.Category;
import com.skywaet.vtbmarket.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.skywaet.vtbmarket.helper.TestUtils.getCategory;
import static com.skywaet.vtbmarket.helper.TestUtils.getProduct;

@SpringBootTest
class ProductJdbcDaoTest extends VtbMarketApplicationTests {
    @AfterEach
    void clearDb() {
        productDao.deleteAll();
        categoryDao.deleteAll();
    }

    @Test
    void testFindAll() {
        Category category = categoryDao.create(getCategory());
        for (int i = 0; i < 5; i++) {
            Product product = Product.builder()
                    .category(category)
                    .articleNumber("abc123")
                    .price(10.0)
                    .name("name")
                    .build();
            productDao.create(product);
        }
        Assertions.assertEquals(5, productDao.findAll().size());
    }

    @Test
    void testCreateAndGet() {
        Category category = categoryDao.create(getCategory());
        Product product = getProduct();
        product.setCategory(category);
        Product saved = productDao.create(product);
        Product fromDb = productDao.findById(saved.getId());
        Assertions.assertEquals(saved, fromDb);
    }

    @Test
    void testFindByNonExistentId() {
        Assertions.assertThrows(ProductNotFoundException.class, () -> productDao.findById(1000L));
    }


    @Test
    void testUpdate() {
        Category category = categoryDao.create(getCategory());
        Product product = getProduct();
        product.setCategory(category);
        Product saved = productDao.create(product);
        saved.setName("new name");
        Product updated = productDao.update(saved.getId(), saved);
        Product fromDb = productDao.findById(saved.getId());
        Assertions.assertEquals(saved, fromDb);
        Assertions.assertEquals(updated, fromDb);
    }

    @Test
    void testUpdateByNonExistentId() {
        Category category = categoryDao.create(getCategory());
        Product product = getProduct();
        product.setCategory(category);
        Assertions.assertThrows(ProductNotFoundException.class, () -> productDao.update(1000L, product));
    }

    @Test
    void testDelete() {
        Category category = categoryDao.create(getCategory());
        Product product = getProduct();
        product.setCategory(category);
        Product saved = productDao.create(product);
        productDao.delete(saved.getId());
        Assertions.assertThrows(ProductNotFoundException.class, () -> productDao.findById(saved.getId()));
    }

}