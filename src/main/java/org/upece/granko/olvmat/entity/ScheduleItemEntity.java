package org.upece.granko.olvmat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "schedule_item")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleItemEntity {
    @Id
    private UUID id;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "order_number")
    private int order;

    private int length; // in minutes
    private String color;
    private String text;
}
