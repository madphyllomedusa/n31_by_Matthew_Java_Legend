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
import ru.spb.n31.n31_by_matthew_java_legend.util.IdUtils;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminTypeProjectsService {
    private final ServiceTypeExampleRepository exampleRepo;
    private final SubserviceTypeRepository typeRepo;

    public ExampleResponse create(ExampleRequest r) {
        String exampleId = IdUtils.nullIfBlank(r.id());
        if (exampleId == null) {
            exampleId = IdUtils.nextNumericStringId(exampleRepo.findAll().stream().map(ServiceTypeExampleEntity::getId));
        }
        exampleId = Objects.requireNonNull(exampleId, "exampleId");
        if (exampleRepo.existsById(exampleId)) throw new BadRequestException("Example exists: " + exampleId);
        var type = typeRepo.findById(r.typeId()).orElseThrow(() -> new NotFoundException("Type not found: " + r.typeId()));

        var e = new ServiceTypeExampleEntity();
        e.setId(exampleId);
        e.setImage(r.image());
        e.setType(type);

        exampleRepo.save(e);
        return new ExampleResponse(e.getId(), type.getId(), e.getImage());
    }

    public ExampleResponse update(String id, ExampleRequest r) {
        final String idVal = IdUtils.nullIfBlank(id);
        if (idVal == null) throw new BadRequestException("Example id is required");
        var existing = exampleRepo.findById(idVal).orElseThrow(() -> new NotFoundException("Example not found: " + idVal));
        var type = typeRepo.findById(r.typeId()).orElseThrow(() -> new NotFoundException("Type not found: " + r.typeId()));

        String requestedId = IdUtils.nullIfBlank(r.id());
        if (requestedId != null && !requestedId.equals(idVal)) {
            if (exampleRepo.existsById(requestedId)) throw new BadRequestException("Example exists: " + requestedId);

            var replacement = new ServiceTypeExampleEntity();
            replacement.setId(requestedId);
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
        final String idVal = IdUtils.nullIfBlank(id);
        if (idVal == null) return;
        if (!exampleRepo.existsById(idVal)) return;
        exampleRepo.deleteById(idVal);
    }
}
