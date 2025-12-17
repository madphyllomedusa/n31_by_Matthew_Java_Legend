package ru.spb.n31.n31_by_matthew_java_legend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.entity.AboutUsTextEntity;

import java.util.List;

public interface AboutUsTextRepository extends JpaRepository<AboutUsTextEntity, Long> {
    List<AboutUsTextEntity> findAllByOrderByOrderNumAsc();
}
