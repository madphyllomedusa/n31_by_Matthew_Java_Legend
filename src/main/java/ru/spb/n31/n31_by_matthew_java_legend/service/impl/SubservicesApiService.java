package ru.spb.n31.n31_by_matthew_java_legend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceTypeResponse;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceTypeRepository;
import ru.spb.n31.n31_by_matthew_java_legend.util.IdUtils;

import java.util.List;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class SubservicesApiService {

    private final SubserviceRepository subserviceRepo;
    private final SubserviceTypeRepository typeRepo;

    public List<SubserviceResponse> getSubservices() {

        return subserviceRepo.findAll().stream()
                .sorted(Comparator.comparing(sub -> sub.getId(), IdUtils.numericStringComparator()))
                .map(sub -> {
                    var types = typeRepo.findAllBySubservice_Id(sub.getId()).stream()
                            .sorted(Comparator.comparing(t -> t.getId(), IdUtils.numericStringComparator()))
                            .map(t -> new SubserviceTypeResponse(
                                    t.getId(),
                                    t.getTitle(),
                                    t.getImage(),
                                    t.getService().getId()
                            ))
                            .toList();
                    return new SubserviceResponse(
                            sub.getId(),
                            sub.getTitle(),
                            sub.getDescription(),
                            sub.getWorkHours(),
                            sub.getAveragePrice(),
                            types
                    );
                })
                .toList();
    }
}

