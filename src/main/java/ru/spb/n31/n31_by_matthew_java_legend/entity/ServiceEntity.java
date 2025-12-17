package ru.spb.n31.n31_by_matthew_java_legend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "services")
@Getter
@Setter
public class ServiceEntity {

    @Id
    private String id;

    private String title;
    private Integer price;
    private String image;
}
