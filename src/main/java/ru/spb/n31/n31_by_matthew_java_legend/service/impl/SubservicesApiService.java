package ru.spb.n31.n31_by_matthew_java_legend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceTypeResponse;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubservicesApiService {

    private final SubserviceRepository subserviceRepo;
    private final SubserviceTypeRepository typeRepo;

    public List<SubserviceResponse> getSubservices() {

        return subserviceRepo.findAll().stream()
                .map(sub -> {
                    var types = typeRepo.findAllBySubservice_Id(sub.getId()).stream()
                            .map(t -> new SubserviceTypeResponse(
                                    t.getId(),
                                    t.getTitle(),
                                    t.getImage(),
                                    t.getService().getId()
                            ))
                            .toList();
                    return new SubserviceResponse(sub.getId(), types);
                })
                .toList();
    }
}

