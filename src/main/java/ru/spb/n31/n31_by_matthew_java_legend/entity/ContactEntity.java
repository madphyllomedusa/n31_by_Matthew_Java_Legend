package ru.spb.n31.n31_by_matthew_java_legend.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contacts")
@Getter
@Setter
public class ContactEntity {

    @Id
    private Long id;

    private String icon;
    private String title;
    private String subtitle;
}
