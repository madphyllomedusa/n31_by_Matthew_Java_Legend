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
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.ContactRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ContactResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ContactsResponse;
import ru.spb.n31.n31_by_matthew_java_legend.service.admin.AdminContactsService;

@Tag(name = "Admin: Contacts", description = "Редактирование contacts и description")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/contacts")
public class AdminContactsController {

    private final AdminContactsService service;

    @Operation(summary = "Обновить description")
    @PutMapping("/description")
    public ContactsResponse setDescription(@RequestBody String description) {
        return service.setDescription(description);
    }

    @Operation(summary = "Создать контакт")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponse create(@RequestBody ContactRequest request) {
        return service.create(request);
    }

    @Operation(summary = "Обновить контакт")
    @PutMapping("/{id}")
    public ContactResponse update(@PathVariable Long id, @RequestBody ContactRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Удалить контакт")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

