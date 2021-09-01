package com.skywaet.vtbmarket.model;

import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrder extends BaseEntity {
    private LocalDateTime dateTime;
    private String location;
    private String addressee;
    private String contactNumber;
    private String status;
    private Purchase purchase;

    public DeliveryOrder(Long id, LocalDateTime dateTime,
                         String location,
                         String addressee,
                         String contactNumber,
                         String status,
                         Purchase purchase) {
        super(id);
        this.dateTime = dateTime;
        this.location = location;
        this.addressee = addressee;
        this.contactNumber = contactNumber;
        this.status = status;
        this.purchase = purchase;
    }
}
