package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.VtbMarketApplicationTests;
import com.skywaet.vtbmarket.exception.notfound.PurchaseNotFoundException;
import com.skywaet.vtbmarket.exception.notfound.TicketNotFoundException;
import com.skywaet.vtbmarket.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.skywaet.vtbmarket.helper.TestUtils.*;

@SpringBootTest
class PurchaseJdbcDaoTest extends VtbMarketApplicationTests {

    @AfterEach
    void clearDb() {
        deliveryOrderDao.deleteAll();
        purchaseDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    void testFindAll() {
        User user = userDao.create(sampleUser);
        Category category = categoryDao.create(getCategory());
        Product sampleProduct = getProduct();
        sampleProduct.setCategory(category);
        Product product = productDao.create(sampleProduct);
        for (int i = 0; i < 5; i++) {

            Purchase purchase = Purchase.builder()
                    .status("status")
                    .sumOfPayment(1000.0)
                    .build();
            purchase.setClient(user);
            PurchaseContent content = PurchaseContent.builder().amount(100).build();
            content.setPurchase(purchase);
            content.setProduct(product);
            purchase.setContent(List.of(content));
            purchaseDao.create(purchase);
        }

        List<Purchase> result = purchaseDao.findAll();
        Assertions.assertEquals(5, result.size());
        Assertions.assertEquals(0, result.get(0).getContent().size());
    }

    @Test
    void testCreateAndGet() {
        User user = userDao.create(sampleUser);
        Purchase purchase = getPurchase();
        purchase.setClient(user);
        Purchase saved = purchaseDao.create(purchase);
        Purchase fromDb = purchaseDao.findById(saved.getId());
        Assertions.assertEquals(saved, fromDb);
    }

    @Test
    void testFindByNonExistentId() {
        Assertions.assertThrows(PurchaseNotFoundException.class, () -> purchaseDao.findById(1000L));
    }


    @Test
    void testUpdate() {
        User user = userDao.create(sampleUser);
        Purchase purchase = getPurchase();
        purchase.setClient(user);
        Purchase saved = purchaseDao.create(purchase);
        saved.setStatus("active");
        Purchase updated = purchaseDao.update(saved.getId(), saved);
        Purchase fromDb = purchaseDao.findById(saved.getId());
        Assertions.assertEquals(saved, fromDb);
    }

    @Test
    void testUpdateByNonExistentId() {
        User user = userDao.create(sampleUser);
        Purchase purchase = getPurchase();
        purchase.setClient(user);
        Assertions.assertThrows(PurchaseNotFoundException.class, () -> purchaseDao.update(1000L, purchase));
    }

    @Test
    void testDelete() {
        User user = userDao.create(sampleUser);
        Purchase purchase = getPurchase();
        purchase.setClient(user);
        Purchase saved = purchaseDao.create(purchase);
        purchaseDao.delete(saved.getId());
        Assertions.assertThrows(PurchaseNotFoundException.class, () -> purchaseDao.findById(saved.getId()));
    }
}