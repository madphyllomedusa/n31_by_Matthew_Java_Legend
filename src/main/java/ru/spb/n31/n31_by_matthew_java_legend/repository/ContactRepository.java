package ru.spb.n31.n31_by_matthew_java_legend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ContactEntity;

import java.util.List;

public interface ContactRepository
        extends JpaRepository<ContactEntity, Long> {
    List<ContactEntity> findAllByOrderByIdAsc();
}
