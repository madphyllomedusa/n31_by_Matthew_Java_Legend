package ru.spb.n31.n31_by_matthew_java_legend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ServiceTypeExampleEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.SubserviceTypeEntity;

import java.util.List;

public interface ServiceTypeExampleRepository
        extends JpaRepository<ServiceTypeExampleEntity, String> {
    List<ServiceTypeExampleEntity> findAllByType(SubserviceTypeEntity type);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update service_type_examples set type_id = :newTypeId where type_id = :oldTypeId", nativeQuery = true)
    int updateTypeId(@Param("oldTypeId") String oldTypeId, @Param("newTypeId") String newTypeId);
}
