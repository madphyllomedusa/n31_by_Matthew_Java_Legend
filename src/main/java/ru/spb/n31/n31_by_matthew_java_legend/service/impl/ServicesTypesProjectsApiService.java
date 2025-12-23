package ru.spb.n31.n31_by_matthew_java_legend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ExampleResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ServiceTypesProjectResponse;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceTypeExampleRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceTypeRepository;
import ru.spb.n31.n31_by_matthew_java_legend.util.IdUtils;

import java.util.List;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ServicesTypesProjectsApiService {

    private final SubserviceTypeRepository typeRepo;
    private final ServiceTypeExampleRepository exampleRepo;

    public List<ServiceTypesProjectResponse> getServicesTypesProjects() {

        return typeRepo.findAll().stream()
                .sorted(Comparator.comparing(t -> t.getId(), IdUtils.numericStringComparator()))
                .map(type -> {
                    var examples = exampleRepo.findAllByType(type).stream()
                            .sorted(Comparator.comparing(e -> e.getId(), IdUtils.numericStringComparator()))
                            .map(e -> new ExampleResponse(e.getId(), type.getId(), e.getImage()))
                            .toList();
                    return new ServiceTypesProjectResponse(type.getId(), examples);
                })
                // если хочешь не отдавать пустые блоки — раскомментируй
                //.filter(b -> !b.examples().isEmpty())
                .toList();
    }
}
