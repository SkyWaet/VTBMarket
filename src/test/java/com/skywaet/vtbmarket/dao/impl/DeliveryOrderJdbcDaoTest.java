package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.VtbMarketApplicationTests;
import com.skywaet.vtbmarket.exception.notfound.DeliveryOrderNotFoundException;
import com.skywaet.vtbmarket.model.DeliveryOrder;
import com.skywaet.vtbmarket.model.Purchase;
import com.skywaet.vtbmarket.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static com.skywaet.vtbmarket.helper.TestUtils.getDeliveryOrder;
import static com.skywaet.vtbmarket.helper.TestUtils.getPurchase;

@SpringBootTest
class DeliveryOrderJdbcDaoTest extends VtbMarketApplicationTests {
    @AfterEach
    void clearDb() {
        deliveryOrderDao.deleteAll();
        purchaseDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    void testFindAll() {
        User user = userDao.create(sampleUser);
        Purchase rawPurchase = getPurchase();
        rawPurchase.setClient(user);
        Purchase purchase = purchaseDao.create(rawPurchase);

        for (int i = 0; i < 5; i++) {
            DeliveryOrder deliveryOrder = DeliveryOrder.builder()
                    .addressee("addressee" + i)
                    .status("ok" + i)
                    .dateTime(LocalDateTime.now())
                    .location("location" + i)
                    .contactNumber("number" + i)
                    .build();
            deliveryOrder.setPurchase(purchase);
            deliveryOrderDao.create(deliveryOrder);
        }
        Assertions.assertEquals(5, deliveryOrderDao.findAll().size());
    }

    @Test
    void testCreateAndGet() {
        User user = userDao.create(sampleUser);
        Purchase rawPurchase = getPurchase();
        rawPurchase.setClient(user);
        Purchase purchase = purchaseDao.create(rawPurchase);
        DeliveryOrder order = getDeliveryOrder();
        order.setPurchase(purchase);
        DeliveryOrder saved = deliveryOrderDao.create(order);
        DeliveryOrder fromDb = deliveryOrderDao.findById(saved.getId());
        Assertions.assertEquals(saved, fromDb);
    }

    @Test
    void testFindByNonExistentId() {
        Assertions.assertThrows(DeliveryOrderNotFoundException.class, () -> deliveryOrderDao.findById(1000L));
    }


    @Test
    void testUpdate() {
        User user = userDao.create(sampleUser);
        Purchase rawPurchase = getPurchase();
        rawPurchase.setClient(user);
        Purchase purchase = purchaseDao.create(rawPurchase);
        DeliveryOrder order = getDeliveryOrder();
        order.setPurchase(purchase);
        DeliveryOrder saved = deliveryOrderDao.create(order);
        saved.setAddressee("new add");
        DeliveryOrder updated = deliveryOrderDao.update(saved.getId(), saved);
        DeliveryOrder fromDb = deliveryOrderDao.findById(saved.getId());
        Assertions.assertEquals(updated, fromDb);
    }

    @Test
    void testUpdateByNonExistentId() {
        User user = userDao.create(sampleUser);
        Purchase rawPurchase = getPurchase();
        rawPurchase.setClient(user);
        Purchase purchase = purchaseDao.create(rawPurchase);
        DeliveryOrder order = getDeliveryOrder();
        order.setPurchase(purchase);
        Assertions.assertThrows(DeliveryOrderNotFoundException.class, () -> deliveryOrderDao.update(1000L, order));
    }

    @Test
    void testDelete() {
        User user = userDao.create(sampleUser);
        Purchase rawPurchase = getPurchase();
        rawPurchase.setClient(user);
        Purchase purchase = purchaseDao.create(rawPurchase);
        DeliveryOrder order = getDeliveryOrder();
        order.setPurchase(purchase);
        DeliveryOrder saved = deliveryOrderDao.create(order);
        deliveryOrderDao.delete(saved.getId());
        Assertions.assertThrows(DeliveryOrderNotFoundException.class, () -> deliveryOrderDao.findById(saved.getId()));
    }
}