package ru.spb.n31.n31_by_matthew_java_legend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.AboutUsStatRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.AboutUsResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.AboutUsStatResponse;
import ru.spb.n31.n31_by_matthew_java_legend.service.admin.AdminAboutUsService;

import java.util.List;

@Tag(name = "Admin: About Us", description = "Редактирование about-us")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/about-us")
public class AdminAboutUsController {

    private final AdminAboutUsService service;

    @Operation(summary = "Заменить description целиком")
    @PutMapping("/description")
    public AboutUsResponse replaceDescription(@RequestBody List<String> description) {
        return service.replaceDescription(description);
    }

    @Operation(summary = "Создать стату")
    @PostMapping("/stats")
    @ResponseStatus(HttpStatus.CREATED)
    public AboutUsStatResponse createStat(@RequestBody AboutUsStatRequest request) {
        return service.createStat(request);
    }

    @Operation(summary = "Обновить стату")
    @PutMapping("/stats/{id}")
    public AboutUsStatResponse updateStat(@PathVariable Long id, @RequestBody AboutUsStatRequest request) {
        return service.updateStat(id, request);
    }

    @Operation(summary = "Удалить стату")
    @DeleteMapping("/stats/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStat(@PathVariable Long id) {
        service.deleteStat(id);
    }
}

