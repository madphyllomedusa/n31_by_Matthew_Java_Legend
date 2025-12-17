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
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.SubserviceRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.SubserviceTypeRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceTypeResponse;
import ru.spb.n31.n31_by_matthew_java_legend.service.admin.AdminSubservicesService;

@Tag(name = "Admin: Subservices", description = "Управление subservices и их types")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/subservices")
public class AdminSubservicesController {

    private final AdminSubservicesService service;


    @Operation(summary = "Создать subservice (контейнер)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubserviceResponse createSubservice(@RequestBody SubserviceRequest request) {
        return service.createSubservice(request);
    }

    @Operation(summary = "Удалить subservice")
    @DeleteMapping("/{subserviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubservice(@PathVariable String subserviceId) {
        service.deleteSubservice(subserviceId);
    }


    @Operation(summary = "Добавить type в subservice")
    @PostMapping("/{subserviceId}/types")
    @ResponseStatus(HttpStatus.CREATED)
    public SubserviceTypeResponse addType(@PathVariable String subserviceId,
                                          @RequestBody SubserviceTypeRequest request) {
        return service.addType(subserviceId, request);
    }

    @Operation(summary = "Обновить type в subservice")
    @PutMapping("/{subserviceId}/types/{typeId}")
    public SubserviceTypeResponse updateType(@PathVariable String subserviceId,
                                             @PathVariable String typeId,
                                             @RequestBody SubserviceTypeRequest request) {
        return service.updateType(subserviceId, typeId, request);
    }

    @Operation(summary = "Удалить type из subservice")
    @DeleteMapping("/{subserviceId}/types/{typeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteType(@PathVariable String subserviceId, @PathVariable String typeId) {
        service.deleteType(subserviceId, typeId);
    }
}

