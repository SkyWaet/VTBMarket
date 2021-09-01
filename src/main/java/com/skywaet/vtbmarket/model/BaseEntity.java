package com.skywaet.vtbmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public abstract class BaseEntity {
    private Long id;

    private boolean isDeleted;

    public BaseEntity(Long id) {
        this.id = id;
    }


}
