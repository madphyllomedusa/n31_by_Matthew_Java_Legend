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
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.SubserviceRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.SubserviceTypeRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceTypeResponse;
import ru.spb.n31.n31_by_matthew_java_legend.service.FileStorageService;
import ru.spb.n31.n31_by_matthew_java_legend.service.admin.AdminSubservicesService;

@Tag(name = "Admin: Subservices", description = "Управление subservices и их types")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/subservices")
public class AdminSubservicesController {

    private final AdminSubservicesService service;
    private final FileStorageService fileStorageService;


    @Operation(summary = "Создать subservice (контейнер)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SubserviceResponse createSubservice(@RequestBody SubserviceRequest request) {
        return service.createSubservice(request);
    }

    @PutMapping(path = "/{subserviceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubserviceResponse updateSubservice(@PathVariable String subserviceId,
                                               @RequestBody SubserviceRequest request) {
        return service.updateSubservice(subserviceId, request);
    }

    @Operation(summary = "Удалить subservice")
    @DeleteMapping("/{subserviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubservice(@PathVariable String subserviceId) {
        service.deleteSubservice(subserviceId);
    }


    @Operation(summary = "Добавить type в subservice")
    @PostMapping(path = "/{subserviceId}/types", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SubserviceTypeResponse addType(@PathVariable String subserviceId,
                                          @RequestBody SubserviceTypeRequest request) {
        return service.addType(subserviceId, request);
    }

    @PostMapping(path = "/{subserviceId}/types", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SubserviceTypeResponse addTypeMultipart(@PathVariable String subserviceId,
                                                   @RequestParam(required = false) String id,
                                                   @RequestParam String title,
                                                   @RequestParam String serviceId,
                                                   @RequestPart(required = false) MultipartFile image) {
        String imagePath = fileStorageService.store(image);
        return service.addType(subserviceId, new SubserviceTypeRequest(id, title, imagePath, serviceId));
    }

    @Operation(summary = "Обновить type в subservice")
    @PutMapping(path = "/{subserviceId}/types/{typeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubserviceTypeResponse updateType(@PathVariable String subserviceId,
                                             @PathVariable String typeId,
                                             @RequestBody SubserviceTypeRequest request) {
        return service.updateType(subserviceId, typeId, request);
    }

    @PutMapping(path = "/{subserviceId}/types/{typeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SubserviceTypeResponse updateTypeMultipart(@PathVariable String subserviceId,
                                                      @PathVariable String typeId,
                                                      @RequestParam String title,
                                                      @RequestParam String serviceId,
                                                      @RequestPart(required = false) MultipartFile image) {
        String imagePath = fileStorageService.store(image);
        return service.updateType(subserviceId, typeId, new SubserviceTypeRequest(typeId, title, imagePath, serviceId));
    }

    @Operation(summary = "Удалить type из subservice")
    @DeleteMapping("/{subserviceId}/types/{typeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteType(@PathVariable String subserviceId, @PathVariable String typeId) {
        service.deleteType(subserviceId, typeId);
    }
}

