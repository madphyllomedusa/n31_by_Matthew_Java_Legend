package ru.spb.n31.n31_by_matthew_java_legend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ContactMetaEntity;

public interface ContactMetaRepository
        extends JpaRepository<ContactMetaEntity, Long> {
}
