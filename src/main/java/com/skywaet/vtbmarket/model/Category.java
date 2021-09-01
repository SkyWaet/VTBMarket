package com.skywaet.vtbmarket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Category extends BaseEntity {
    private String name;

    public Category(Long id, String name) {
        super(id);
        this.name = name;
    }

    private List<Product> productList = new ArrayList<>();

    public void setProductList(List<Product> productList) {
        this.productList = Objects.isNull(productList) ? new ArrayList<>() : productList;
    }
}
