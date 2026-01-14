package ru.spb.n31.n31_by_matthew_java_legend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.ServiceRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ServiceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.service.FileStorageService;
import ru.spb.n31.n31_by_matthew_java_legend.service.admin.AdminServicesService;

@Tag(name = "Admin: Services", description = "CRUD для services")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/services")
public class AdminServicesController {

    private final AdminServicesService service;
    private final FileStorageService fileStorageService;

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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse create(@RequestBody ServiceRequest request) {
        return service.create(request);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createMultipart(@RequestParam(required = false) String id,
                                           @RequestParam String title,
                                           @RequestParam Integer price,
                                           @RequestPart(required = false) MultipartFile image) {
        String imagePath = fileStorageService.store(image);
        return service.create(new ServiceRequest(id, title, price, imagePath));
    }

    @Operation(summary = "Обновить service по id")
    @ApiResponse(responseCode = "200", description = "Обновлено")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResponse update(@PathVariable String id, @RequestBody ServiceRequest request) {
        return service.update(id, request);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ServiceResponse updateMultipart(@PathVariable String id,
                                           @RequestParam String title,
                                           @RequestParam Integer price,
                                           @RequestPart(required = false) MultipartFile image) {
        String imagePath = fileStorageService.store(image);
        return service.update(id, new ServiceRequest(id, title, price, imagePath));
    }

    @Operation(summary = "Удалить service по id")
    @ApiResponse(responseCode = "204", description = "Удалено")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}

