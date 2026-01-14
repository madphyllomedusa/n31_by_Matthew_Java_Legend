package ru.spb.n31.n31_by_matthew_java_legend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.ExampleRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ExampleResponse;
import ru.spb.n31.n31_by_matthew_java_legend.service.FileStorageService;
import ru.spb.n31.n31_by_matthew_java_legend.service.admin.AdminTypeProjectsService;

@Tag(name = "Admin: Type Projects", description = "CRUD для примеров проектов (examples)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/type-projects")
public class AdminTypeProjectsController {

    private final AdminTypeProjectsService service;
    private final FileStorageService fileStorageService;

    @Operation(summary = "Создать тип проекта")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ExampleResponse create(@RequestBody ExampleRequest request) {
        return service.create(request);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ExampleResponse createMultipart(@RequestParam(required = false) String id,
                                           @RequestParam String typeId,
                                           @RequestParam String title,
                                           @RequestParam String description,
                                           @RequestParam Integer price,
                                           @RequestPart(required = false) MultipartFile image) {
        String imagePath = fileStorageService.store(image);
        return service.create(new ExampleRequest(id, typeId, title, description, price, imagePath));
    }

    @Operation(summary = "Изменить тип проекта")
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ExampleResponse update(@PathVariable String id, @RequestBody ExampleRequest request) {
        return service.update(id, request);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ExampleResponse updateMultipart(@PathVariable String id,
                                           @RequestParam String typeId,
                                           @RequestParam String title,
                                           @RequestParam String description,
                                           @RequestParam Integer price,
                                           @RequestPart(required = false) MultipartFile image) {
        String imagePath = fileStorageService.store(image);
        return service.update(id, new ExampleRequest(id, typeId, title, description, price, imagePath));
    }

    @Operation(summary = "Удалить тип проекта")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
