package com.skywaet.vtbmarket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
public class PurchaseContent extends BaseEntity {
    private Product product;
    private Purchase purchase;
    private Integer amount;

    public PurchaseContent(Long id, Integer amount, Product product, Purchase purchase) {
        super(id);
        this.product = product;
        this.purchase = purchase;
        this.amount = amount;
    }
}
