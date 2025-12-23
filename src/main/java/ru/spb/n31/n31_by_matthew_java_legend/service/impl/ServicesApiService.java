package ru.spb.n31.n31_by_matthew_java_legend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ServiceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.util.IdUtils;

import java.util.List;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ServicesApiService {

    private final ServiceRepository serviceRepo;

    public List<ServiceResponse> getServices() {
        return serviceRepo.findAll().stream()
                .sorted(Comparator.comparing(s -> s.getId(), IdUtils.numericStringComparator()))
                .map(s -> new ServiceResponse(s.getId(), s.getTitle(), s.getPrice(), s.getImage()))
                .toList();
    }
}

