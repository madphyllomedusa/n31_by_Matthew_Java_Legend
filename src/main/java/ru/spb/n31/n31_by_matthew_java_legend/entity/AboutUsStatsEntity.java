package ru.spb.n31.n31_by_matthew_java_legend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "about_us_stats")
@Getter
@Setter
public class AboutUsStatsEntity {

    @Id
    private Long id;

    private String upper;
    private String lower;
}
