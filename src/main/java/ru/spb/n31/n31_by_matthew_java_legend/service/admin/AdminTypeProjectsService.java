package ru.spb.n31.n31_by_matthew_java_legend.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.ExampleRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ExampleResponse;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ServiceTypeExampleEntity;
import ru.spb.n31.n31_by_matthew_java_legend.exception.BadRequestException;
import ru.spb.n31.n31_by_matthew_java_legend.exception.NotFoundException;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceTypeExampleRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceTypeRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminTypeProjectsService {
    private final ServiceTypeExampleRepository exampleRepo;
    private final SubserviceTypeRepository typeRepo;

    public ExampleResponse create(ExampleRequest r) {
        if (r.id() == null) throw new BadRequestException("Example id is required");
        if (exampleRepo.existsById(r.id())) throw new BadRequestException("Example exists: " + r.id());
        var type = typeRepo.findById(r.typeId()).orElseThrow(() -> new NotFoundException("Type not found: " + r.typeId()));

        var e = new ServiceTypeExampleEntity();
        e.setId(r.id());
        e.setImage(r.image());
        e.setType(type);

        exampleRepo.save(e);
        return new ExampleResponse(e.getId(), type.getId(), e.getImage());
    }

    public ExampleResponse update(String id, ExampleRequest r) {
        var existing = exampleRepo.findById(id).orElseThrow(() -> new NotFoundException("Example not found: " + id));
        var type = typeRepo.findById(r.typeId()).orElseThrow(() -> new NotFoundException("Type not found: " + r.typeId()));

        if (r.id() != null && !r.id().equals(id)) {
            if (exampleRepo.existsById(r.id())) throw new BadRequestException("Example exists: " + r.id());

            var replacement = new ServiceTypeExampleEntity();
            replacement.setId(r.id());
            replacement.setImage(r.image());
            replacement.setType(type);
            exampleRepo.save(replacement);

            exampleRepo.delete(existing);
            return new ExampleResponse(replacement.getId(), type.getId(), replacement.getImage());
        }

        existing.setImage(r.image());
        existing.setType(type);
        exampleRepo.save(existing);

        return new ExampleResponse(existing.getId(), type.getId(), existing.getImage());
    }

    public void delete(String id) {
        if (!exampleRepo.existsById(id)) return;
        exampleRepo.deleteById(id);
    }
}
