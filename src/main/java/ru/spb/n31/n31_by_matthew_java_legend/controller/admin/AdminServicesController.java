package ru.spb.n31.n31_by_matthew_java_legend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.ServiceRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ServiceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.service.admin.AdminServicesService;

@Tag(name = "Admin: Services", description = "CRUD для services")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/services")
public class AdminServicesController {

    private final AdminServicesService service;

    @Operation(summary = "Получить service по id")
    @ApiResponse(responseCode = "200", description = "Найдено")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    @GetMapping("/{id}")
    public ServiceResponse get(@PathVariable String id) {
        return service.get(id);
    }

    @Operation(summary = "Создать service")
    @ApiResponse(responseCode = "201", description = "Создано")
    @ApiResponse(responseCode = "400", description = "Конфликт id")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse create(@RequestBody ServiceRequest request) {
        return service.create(request);
    }

    @Operation(summary = "Обновить service по id")
    @ApiResponse(responseCode = "200", description = "Обновлено")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    @PutMapping("/{id}")
    public ServiceResponse update(@PathVariable String id, @RequestBody ServiceRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Удалить service по id")
    @ApiResponse(responseCode = "204", description = "Удалено")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}

