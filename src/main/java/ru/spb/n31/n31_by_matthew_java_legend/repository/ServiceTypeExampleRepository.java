package ru.spb.n31.n31_by_matthew_java_legend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ServiceTypeExampleEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.SubserviceTypeEntity;

import java.util.List;

public interface ServiceTypeExampleRepository
        extends JpaRepository<ServiceTypeExampleEntity, String> {
    List<ServiceTypeExampleEntity> findAllByType(SubserviceTypeEntity type);
}
