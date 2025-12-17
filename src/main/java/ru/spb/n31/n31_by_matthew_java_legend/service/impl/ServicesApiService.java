package ru.spb.n31.n31_by_matthew_java_legend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ServiceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicesApiService {

    private final ServiceRepository serviceRepo;

    public List<ServiceResponse> getServices() {
        return serviceRepo.findAll().stream()
                .map(s -> new ServiceResponse(s.getId(), s.getTitle(), s.getPrice(), s.getImage()))
                .toList();
    }
}

