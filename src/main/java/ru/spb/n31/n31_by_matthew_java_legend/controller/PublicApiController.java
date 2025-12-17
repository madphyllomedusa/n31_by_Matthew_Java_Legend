package ru.spb.n31.n31_by_matthew_java_legend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.AboutUsResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ContactsResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ServiceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ServiceTypesProjectResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.SubserviceResponse;
import ru.spb.n31.n31_by_matthew_java_legend.service.impl.AboutUsApiService;
import ru.spb.n31.n31_by_matthew_java_legend.service.impl.ContactsApiService;
import ru.spb.n31.n31_by_matthew_java_legend.service.impl.ServicesApiService;
import ru.spb.n31.n31_by_matthew_java_legend.service.impl.ServicesTypesProjectsApiService;
import ru.spb.n31.n31_by_matthew_java_legend.service.impl.SubservicesApiService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(
        name = "Public API",
        description = "Публичные read-only эндпоинты для фронта"
)
public class PublicApiController {

    private final ServicesApiService servicesApiService;
    private final SubservicesApiService subservicesApiService;
    private final ServicesTypesProjectsApiService servicesTypesProjectsApiService;
    private final AboutUsApiService aboutUsApiService;
    private final ContactsApiService contactsApiService;

    @Operation(
            summary = "Получить список сервисов",
            description = "Возвращает список сервисов в формате, совпадающем с services.json",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список сервисов",
                            content = @Content(schema = @Schema(implementation = ServiceResponse.class))
                    )
            }
    )
    @GetMapping("/services")
    public List<ServiceResponse> services() {
        return servicesApiService.getServices();
    }

    @Operation(
            summary = "Получить subservices",
            description = "Возвращает subservices и их types в формате subservices.json",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список subservices",
                            content = @Content(schema = @Schema(implementation = SubserviceResponse.class))
                    )
            }
    )
    @GetMapping("/subservices")
    public List<SubserviceResponse> subservices() {
        return subservicesApiService.getSubservices();
    }

    @Operation(
            summary = "Получить проекты по типам сервисов",
            description = "Возвращает примеры работ, сгруппированные по typeId (servicesTypesProjects.json)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Примеры работ",
                            content = @Content(schema = @Schema(implementation = ServiceTypesProjectResponse.class))
                    )
            }
    )
    @GetMapping("/services-types-projects")
    public List<ServiceTypesProjectResponse> servicesTypesProjects() {
        return servicesTypesProjectsApiService.getServicesTypesProjects();
    }

    @Operation(
            summary = "О нас",
            description = "Описание компании и статистика (aboutUs.json)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о компании",
                            content = @Content(schema = @Schema(implementation = AboutUsResponse.class))
                    )
            }
    )
    @GetMapping("/about-us")
    public AboutUsResponse aboutUs() {
        return aboutUsApiService.getAboutUs();
    }

    @Operation(
            summary = "Контакты",
            description = "Контактная информация и описание (contacts.json)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Контакты",
                            content = @Content(schema = @Schema(implementation = ContactsResponse.class))
                    )
            }
    )
    @GetMapping("/contacts")
    public ContactsResponse contacts() {
        return contactsApiService.getContacts();
    }
}

