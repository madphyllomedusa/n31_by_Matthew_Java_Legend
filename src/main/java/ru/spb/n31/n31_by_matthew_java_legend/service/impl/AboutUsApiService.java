package ru.spb.n31.n31_by_matthew_java_legend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.AboutUsResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.AboutUsStatResponse;
import ru.spb.n31.n31_by_matthew_java_legend.entity.AboutUsTextEntity;
import ru.spb.n31.n31_by_matthew_java_legend.repository.AboutUsStatsRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.AboutUsTextRepository;

@Service
@RequiredArgsConstructor
public class AboutUsApiService {

    private final AboutUsTextRepository textRepo;
    private final AboutUsStatsRepository statsRepo;

    public AboutUsResponse getAboutUs() {
        var description = textRepo.findAllByOrderByOrderNumAsc().stream()
                .map(AboutUsTextEntity::getText)
                .toList();

        var stats = statsRepo.findAllByOrderByIdAsc().stream()
                .map(s -> new AboutUsStatResponse(s.getId(), s.getUpper(), s.getLower()))
                .toList();

        return new AboutUsResponse(description, stats);
    }
}

