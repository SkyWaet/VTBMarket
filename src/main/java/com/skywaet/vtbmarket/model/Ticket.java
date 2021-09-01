package com.skywaet.vtbmarket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Ticket extends BaseEntity {
    private User author;
    private String message;
    private Integer priority;
    private String status;

    public Ticket(Long id, String message, Integer priority, String status, User author) {
        super(id);
        this.author = author;
        this.message = message;
        this.priority = priority;
        this.status = status;
    }
}
