package ru.spb.n31.n31_by_matthew_java_legend.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.ServiceRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ServiceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ServiceEntity;
import ru.spb.n31.n31_by_matthew_java_legend.exception.BadRequestException;
import ru.spb.n31.n31_by_matthew_java_legend.exception.NotFoundException;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceTypeRepository;
import ru.spb.n31.n31_by_matthew_java_legend.util.IdUtils;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServicesService {

    private final ServiceRepository repo;
    private final SubserviceTypeRepository typeRepo;


    @Transactional
    public ServiceResponse get(String id) {
        final String idVal = IdUtils.nullIfBlank(id);
        if (idVal == null) throw new BadRequestException("Service id is required");
        var e = repo.findById(idVal).orElseThrow(() -> new NotFoundException("Service not found: " + idVal));
        return new ServiceResponse(e.getId(), e.getTitle(), e.getPrice(), e.getImage());
    }

    public ServiceResponse create(ServiceRequest r) {
        String newId = IdUtils.nullIfBlank(r.id());
        if (newId == null) {
            newId = IdUtils.nextNumericStringId(repo.findAll().stream().map(ServiceEntity::getId));
        }
        newId = Objects.requireNonNull(newId, "newId");
        if (repo.existsById(newId)) throw new BadRequestException("Service already exists: " + newId);
        var e = new ServiceEntity();
        e.setId(newId);
        e.setTitle(r.title());
        e.setPrice(r.price());
        e.setImage(r.image());
        repo.save(e);
        return new ServiceResponse(e.getId(), e.getTitle(), e.getPrice(), e.getImage());
    }

    public ServiceResponse update(String id, ServiceRequest r) {
        final String idVal = IdUtils.nullIfBlank(id);
        if (idVal == null) throw new BadRequestException("Service id is required");
        var existing = repo.findById(idVal).orElseThrow(() -> new NotFoundException("Service not found: " + idVal));

        // Смена id: обновляем зависимые FK (subservice_types.service_id), затем удаляем старую запись.
        String requestedId = IdUtils.nullIfBlank(r.id());
        if (requestedId != null && !requestedId.equals(idVal)) {
            if (repo.existsById(requestedId)) throw new BadRequestException("Service already exists: " + requestedId);

            var replacement = new ServiceEntity();
            replacement.setId(requestedId);
            replacement.setTitle(r.title());
            replacement.setPrice(r.price());
            replacement.setImage(r.image());
            repo.save(replacement);

            typeRepo.updateServiceId(idVal, requestedId);
            repo.delete(existing);

            return new ServiceResponse(replacement.getId(), replacement.getTitle(), replacement.getPrice(), replacement.getImage());
        }

        existing.setTitle(r.title());
        existing.setPrice(r.price());
        existing.setImage(r.image());
        repo.save(existing);
        return new ServiceResponse(existing.getId(), existing.getTitle(), existing.getPrice(), existing.getImage());
    }

    public void delete(String id) {
        final String idVal = IdUtils.nullIfBlank(id);
        if (idVal == null) return;
        if (!repo.existsById(idVal)) return;
        repo.deleteById(idVal);
    }
}
