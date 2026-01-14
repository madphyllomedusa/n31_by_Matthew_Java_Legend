package ru.spb.n31.n31_by_matthew_java_legend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subservices")
@Getter
@Setter
public class SubserviceEntity {

    @Id
    private String id;

    private String title;

    private String description;

    @Column(name = "work_hours")
    private String workHours;

    @Column(name = "average_price")
    private Integer averagePrice;
}

