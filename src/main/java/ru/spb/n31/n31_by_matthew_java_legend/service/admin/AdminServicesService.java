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

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServicesService {

    private final ServiceRepository repo;
    private final SubserviceTypeRepository typeRepo;


    @Transactional
    public ServiceResponse get(String id) {
        var e = repo.findById(id).orElseThrow(() -> new NotFoundException("Service not found: " + id));
        return new ServiceResponse(e.getId(), e.getTitle(), e.getPrice(), e.getImage());
    }

    public ServiceResponse create(ServiceRequest r) {
        if (r.id() == null) throw new BadRequestException("Service id is required");
        if (repo.existsById(r.id())) throw new BadRequestException("Service already exists: " + r.id());
        var e = new ServiceEntity();
        e.setId(r.id());
        e.setTitle(r.title());
        e.setPrice(r.price());
        e.setImage(r.image());
        repo.save(e);
        return new ServiceResponse(e.getId(), e.getTitle(), e.getPrice(), e.getImage());
    }

    public ServiceResponse update(String id, ServiceRequest r) {
        var existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Service not found: " + id));

        // Смена id: обновляем зависимые FK (subservice_types.service_id), затем удаляем старую запись.
        if (r.id() != null && !r.id().equals(id)) {
            if (repo.existsById(r.id())) throw new BadRequestException("Service already exists: " + r.id());

            var replacement = new ServiceEntity();
            replacement.setId(r.id());
            replacement.setTitle(r.title());
            replacement.setPrice(r.price());
            replacement.setImage(r.image());
            repo.save(replacement);

            typeRepo.updateServiceId(id, r.id());
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
        if (!repo.existsById(id)) return;
        repo.deleteById(id);
    }
}
