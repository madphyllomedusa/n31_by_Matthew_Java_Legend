package ru.spb.n31.n31_by_matthew_java_legend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "service_type_examples")
@Getter
@Setter
public class ServiceTypeExampleEntity {

    @Id
    private String id;

    private String title;

    private String description;

    private Integer price;

    private String image;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private SubserviceTypeEntity type;
}
