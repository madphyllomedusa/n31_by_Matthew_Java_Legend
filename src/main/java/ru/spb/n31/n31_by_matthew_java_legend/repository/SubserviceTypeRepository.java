package ru.spb.n31.n31_by_matthew_java_legend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.spb.n31.n31_by_matthew_java_legend.entity.SubserviceTypeEntity;

import java.util.List;

public interface SubserviceTypeRepository extends JpaRepository<SubserviceTypeEntity, String> {
    List<SubserviceTypeEntity> findAllBySubservice_Id(String subserviceId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update subservice_types set service_id = :newId where service_id = :oldId", nativeQuery = true)
    int updateServiceId(@Param("oldId") String oldId, @Param("newId") String newId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update subservice_types set subservice_id = :newId where subservice_id = :oldId", nativeQuery = true)
    int updateSubserviceId(@Param("oldId") String oldId, @Param("newId") String newId);
}