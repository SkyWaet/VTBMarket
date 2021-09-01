package com.skywaet.vtbmarket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Purchase extends BaseEntity {
    private User client;
    private Double sumOfPayment;
    private String status;
    private List<PurchaseContent> content = new ArrayList<>();
    private List<DeliveryOrder> deliveryOrders = new ArrayList<>();

    public Purchase(Long id, Double sumOfPayment, String status, User client) {
        super(id);
        this.client = client;
        this.sumOfPayment = sumOfPayment;
        this.status = status;
    }

    public void setContent(List<PurchaseContent> content) {
        this.content = Objects.isNull(content) ? new ArrayList<>() : content;
    }

    public void setDeliveryOrders(List<DeliveryOrder> deliveryOrders) {
        this.deliveryOrders = Objects.isNull(deliveryOrders) ? new ArrayList<>() : deliveryOrders;
    }
}
