package ru.spb.n31.n31_by_matthew_java_legend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.entity.SubserviceTypeEntity;

import java.util.List;

public interface SubserviceTypeRepository extends JpaRepository<SubserviceTypeEntity, String> {
    List<SubserviceTypeEntity> findAllBySubservice_Id(String subserviceId);
}