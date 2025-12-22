package ru.spb.n31.n31_by_matthew_java_legend.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public AboutUsStatResponse createStat(AboutUsStatRequest r) {
        if (r.id() == null) throw new BadRequestException("Stat id is required");
        if (statsRepo.existsById(r.id())) throw new BadRequestException("Stat exists: " + r.id());
        var s = new AboutUsStatsEntity();
        s.setId(r.id());
        s.setUpper(r.upper());
        s.setLower(r.lower());
        statsRepo.save(s);
        return new AboutUsStatResponse(s.getId(), s.getUpper(), s.getLower());
    }

    @Transactional
    public AboutUsStatResponse updateStat(Long id, AboutUsStatRequest r) {
        var existing = statsRepo.findById(id).orElseThrow(() -> new NotFoundException("Stat not found: " + id));

        // Если в реквесте пришёл другой id — делаем "переименование" PK через insert+delete (JPA не поддерживает смену @Id у managed entity).
        if (r.id() != null && !r.id().equals(id)) {
            if (statsRepo.existsById(r.id())) throw new BadRequestException("Stat exists: " + r.id());

            var replacement = new AboutUsStatsEntity();
            replacement.setId(r.id());
            replacement.setUpper(r.upper());
            replacement.setLower(r.lower());
            statsRepo.save(replacement);

            statsRepo.delete(existing);
            return new AboutUsStatResponse(replacement.getId(), replacement.getUpper(), replacement.getLower());
        }

        existing.setUpper(r.upper());
        existing.setLower(r.lower());
        statsRepo.save(existing);
        return new AboutUsStatResponse(existing.getId(), existing.getUpper(), existing.getLower());
    }

    @Transactional
    public void deleteStat(Long id) {
        if (!statsRepo.existsById(id)) return;
        statsRepo.deleteById(id);
    }
}

