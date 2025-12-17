package ru.spb.n31.n31_by_matthew_java_legend.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.AboutUsStatRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.AboutUsResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.AboutUsStatResponse;
import ru.spb.n31.n31_by_matthew_java_legend.entity.AboutUsStatsEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.AboutUsTextEntity;
import ru.spb.n31.n31_by_matthew_java_legend.exception.BadRequestException;
import ru.spb.n31.n31_by_matthew_java_legend.exception.NotFoundException;
import ru.spb.n31.n31_by_matthew_java_legend.repository.AboutUsStatsRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.AboutUsTextRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAboutUsService {

    private final AboutUsTextRepository textRepo;
    private final AboutUsStatsRepository statsRepo;

    public AboutUsResponse get() {
        var description = textRepo.findAllByOrderByOrderNumAsc().stream()
                .map(e -> e.getText())
                .toList();

        var stats = statsRepo.findAllByOrderByIdAsc().stream()
                .map(s -> new AboutUsStatResponse(s.getId(), s.getUpper(), s.getLower()))
                .toList();

        return new AboutUsResponse(description, stats);
    }

    public AboutUsResponse replaceDescription(List<String> description) {
        textRepo.deleteAll();
        int i = 0;
        for (String line : description) {
            var e = new AboutUsTextEntity();
            e.setText(line);
            e.setOrderNum(i++);
            textRepo.save(e);
        }
        return get();
    }

    public AboutUsStatResponse createStat(AboutUsStatRequest r) {
        if (statsRepo.existsById(r.id())) throw new BadRequestException("Stat exists: " + r.id());
        var s = new AboutUsStatsEntity();
        s.setId(r.id());
        s.setUpper(r.upper());
        s.setLower(r.lower());
        statsRepo.save(s);
        return new AboutUsStatResponse(s.getId(), s.getUpper(), s.getLower());
    }

    public AboutUsStatResponse updateStat(Long id, AboutUsStatRequest r) {
        var s = statsRepo.findById(id).orElseThrow(() -> new NotFoundException("Stat not found: " + id));
        s.setUpper(r.upper());
        s.setLower(r.lower());
        return new AboutUsStatResponse(s.getId(), s.getUpper(), s.getLower());
    }

    public void deleteStat(Long id) {
        if (!statsRepo.existsById(id)) return;
        statsRepo.deleteById(id);
    }
}

