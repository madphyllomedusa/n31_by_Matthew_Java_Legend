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
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceTypeRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminSubservicesService {

    private final SubserviceRepository subRepo;
    private final SubserviceTypeRepository typeRepo;
    private final ServiceRepository serviceRepo;

    public SubserviceResponse createSubservice(SubserviceRequest r) {
        if (subRepo.existsById(r.subserviceId()))
            throw new BadRequestException("Subservice exists: " + r.subserviceId());
        var sub = new SubserviceEntity();
        sub.setId(r.subserviceId());
        subRepo.save(sub);

        // если пришли types — создадим сразу
        if (r.types() != null) {
            for (var t : r.types()) addType(sub.getId(), t);
        }

        return new SubserviceResponse(sub.getId(),
                typeRepo.findAllBySubservice_Id(sub.getId()).stream()
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
        var sub = subRepo.findById(subserviceId).orElseThrow(() -> new NotFoundException("Subservice not found: " + subserviceId));
        if (typeRepo.existsById(r.id())) throw new BadRequestException("Type exists: " + r.id());

        var service = serviceRepo.findById(r.serviceId())
                .orElseThrow(() -> new NotFoundException("Service not found: " + r.serviceId()));

        var type = new SubserviceTypeEntity();
        type.setId(r.id());
        type.setTitle(r.title());
        type.setImage(r.image());
        type.setService(service);
        type.setSubservice(sub);

        typeRepo.save(type);

        return new SubserviceTypeResponse(type.getId(), type.getTitle(), type.getImage(), type.getService().getId());
    }

    public SubserviceTypeResponse updateType(String subserviceId, String typeId, SubserviceTypeRequest r) {
        var type = typeRepo.findById(typeId).orElseThrow(() -> new NotFoundException("Type not found: " + typeId));
        if (!type.getSubservice().getId().equals(subserviceId)) {
            throw new BadRequestException("Type " + typeId + " does not belong to subservice " + subserviceId);
        }

        // serviceId можно менять
        var service = serviceRepo.findById(r.serviceId())
                .orElseThrow(() -> new NotFoundException("Service not found: " + r.serviceId()));

        type.setTitle(r.title());
        type.setImage(r.image());
        type.setService(service);

        return new SubserviceTypeResponse(type.getId(), type.getTitle(), type.getImage(), type.getService().getId());
    }

    public void deleteType(String subserviceId, String typeId) {
        var type = typeRepo.findById(typeId).orElseThrow(() -> new NotFoundException("Type not found: " + typeId));
        if (!type.getSubservice().getId().equals(subserviceId)) {
            throw new BadRequestException("Type " + typeId + " does not belong to subservice " + subserviceId);
        }
        typeRepo.delete(type);
    }
}

