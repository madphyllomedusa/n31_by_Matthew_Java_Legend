package ru.spb.n31.n31_by_matthew_java_legend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "subservice_types")
@Getter
@Setter
public class SubserviceTypeEntity {

    @Id
    private String id;

    private String title;
    private String image;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @ManyToOne
    @JoinColumn(name = "subservice_id")
    private SubserviceEntity subservice;
}
