package ru.spb.n31.n31_by_matthew_java_legend.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.SubserviceRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.SubserviceTypeRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceTypeResponse;
import ru.spb.n31.n31_by_matthew_java_legend.entity.SubserviceEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.SubserviceTypeEntity;
import ru.spb.n31.n31_by_matthew_java_legend.exception.BadRequestException;
import ru.spb.n31.n31_by_matthew_java_legend.exception.NotFoundException;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceTypeExampleRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceTypeRepository;
import ru.spb.n31.n31_by_matthew_java_legend.util.IdUtils;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminSubservicesService {

    private final SubserviceRepository subRepo;
    private final SubserviceTypeRepository typeRepo;
    private final ServiceRepository serviceRepo;
    private final ServiceTypeExampleRepository exampleRepo;

    public SubserviceResponse createSubservice(SubserviceRequest r) {
        String subId = IdUtils.nullIfBlank(r.subserviceId());
        if (subId == null) {
            subId = IdUtils.nextNumericStringId(subRepo.findAll().stream().map(SubserviceEntity::getId));
        }
        subId = Objects.requireNonNull(subId, "subId");
        if (subRepo.existsById(subId))
            throw new BadRequestException("Subservice exists: " + subId);
        var sub = new SubserviceEntity();
        sub.setId(subId);
        subRepo.save(sub);

        // если пришли types — создадим сразу
        if (r.types() != null) {
            for (var t : r.types()) addType(sub.getId(), t);
        }

        return new SubserviceResponse(sub.getId(),
                typeRepo.findAllBySubservice_Id(sub.getId()).stream()
                        .sorted(Comparator.comparing(SubserviceTypeEntity::getId, IdUtils.numericStringComparator()))
                        .map(t -> new SubserviceTypeResponse(t.getId(), t.getTitle(), t.getImage(), t.getService().getId()))
                        .toList()
        );
    }

    public void deleteSubservice(String subserviceId) {
        if (!subRepo.existsById(subserviceId)) return;
        // сначала удаляем types, иначе FK
        var types = typeRepo.findAllBySubservice_Id(subserviceId);
        typeRepo.deleteAll(types);
        subRepo.deleteById(subserviceId);
    }

    public SubserviceTypeResponse addType(String subserviceId, SubserviceTypeRequest r) {
        final String subserviceIdVal = IdUtils.nullIfBlank(subserviceId);
        if (subserviceIdVal == null) throw new BadRequestException("Subservice id is required");
        var sub = subRepo.findById(subserviceIdVal).orElseThrow(() -> new NotFoundException("Subservice not found: " + subserviceIdVal));

        String typeId = IdUtils.nullIfBlank(r.id());
        if (typeId == null) {
            typeId = IdUtils.nextNumericStringId(typeRepo.findAll().stream().map(SubserviceTypeEntity::getId));
        }
        typeId = Objects.requireNonNull(typeId, "typeId");
        if (typeRepo.existsById(typeId)) throw new BadRequestException("Type exists: " + typeId);

        var service = serviceRepo.findById(r.serviceId())
                .orElseThrow(() -> new NotFoundException("Service not found: " + r.serviceId()));

        var type = new SubserviceTypeEntity();
        type.setId(typeId);
        type.setTitle(r.title());
        type.setImage(r.image());
        type.setService(service);
        type.setSubservice(sub);

        typeRepo.save(type);

        return new SubserviceTypeResponse(type.getId(), type.getTitle(), type.getImage(), type.getService().getId());
    }

    public SubserviceTypeResponse updateType(String subserviceId, String typeId, SubserviceTypeRequest r) {
        final String subserviceIdVal = IdUtils.nullIfBlank(subserviceId);
        final String typeIdVal = IdUtils.nullIfBlank(typeId);
        if (subserviceIdVal == null) throw new BadRequestException("Subservice id is required");
        if (typeIdVal == null) throw new BadRequestException("Type id is required");
        var existing = typeRepo.findById(typeIdVal).orElseThrow(() -> new NotFoundException("Type not found: " + typeIdVal));
        if (!existing.getSubservice().getId().equals(subserviceIdVal)) {
            throw new BadRequestException("Type " + typeIdVal + " does not belong to subservice " + subserviceIdVal);
        }

        // serviceId можно менять
        var service = serviceRepo.findById(r.serviceId())
                .orElseThrow(() -> new NotFoundException("Service not found: " + r.serviceId()));

        // Смена id type: сначала создаём новый type, затем обновляем зависимые examples, потом удаляем старый.
        String requestedId = IdUtils.nullIfBlank(r.id());
        if (requestedId != null && !requestedId.equals(typeIdVal)) {
            if (typeRepo.existsById(requestedId)) throw new BadRequestException("Type exists: " + requestedId);

            var replacement = new SubserviceTypeEntity();
            replacement.setId(requestedId);
            replacement.setTitle(r.title());
            replacement.setImage(r.image());
            replacement.setService(service);
            replacement.setSubservice(existing.getSubservice());
            typeRepo.save(replacement);

            exampleRepo.updateTypeId(typeIdVal, requestedId);
            typeRepo.delete(existing);

            return new SubserviceTypeResponse(replacement.getId(), replacement.getTitle(), replacement.getImage(), replacement.getService().getId());
        }

        existing.setTitle(r.title());
        existing.setImage(r.image());
        existing.setService(service);
        typeRepo.save(existing);

        return new SubserviceTypeResponse(existing.getId(), existing.getTitle(), existing.getImage(), existing.getService().getId());
    }

    public void deleteType(String subserviceId, String typeId) {
        final String subserviceIdVal = IdUtils.nullIfBlank(subserviceId);
        final String typeIdVal = IdUtils.nullIfBlank(typeId);
        if (subserviceIdVal == null) throw new BadRequestException("Subservice id is required");
        if (typeIdVal == null) throw new BadRequestException("Type id is required");
        var type = typeRepo.findById(typeIdVal).orElseThrow(() -> new NotFoundException("Type not found: " + typeIdVal));
        if (!type.getSubservice().getId().equals(subserviceIdVal)) {
            throw new BadRequestException("Type " + typeIdVal + " does not belong to subservice " + subserviceIdVal);
        }
        typeRepo.delete(type);
    }
}

