package com.skywaet.vtbmarket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Product extends BaseEntity {
    private Category category;
    private String articleNumber;
    private String name;
    private Double price;

    public Product(Long id, Category category, String articleNumber, String name, Double price) {
        super(id);
        this.category = category;
        this.articleNumber = articleNumber;
        this.name = name;
        this.price = price;
    }
}
