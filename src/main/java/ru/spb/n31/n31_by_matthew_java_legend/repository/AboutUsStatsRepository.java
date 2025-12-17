package ru.spb.n31.n31_by_matthew_java_legend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.entity.AboutUsStatsEntity;

import java.util.List;

public interface AboutUsStatsRepository
        extends JpaRepository<AboutUsStatsEntity, Long> {
    List<AboutUsStatsEntity> findAllByOrderByIdAsc();
}
