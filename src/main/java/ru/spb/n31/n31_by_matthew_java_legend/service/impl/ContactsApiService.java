package ru.spb.n31.n31_by_matthew_java_legend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ContactResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ContactsResponse;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ContactMetaEntity;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ContactMetaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ContactRepository;

@Service
@RequiredArgsConstructor
public class ContactsApiService {

    private final ContactMetaRepository metaRepo;
    private final ContactRepository contactRepo;

    public ContactsResponse getContacts() {
        var description = metaRepo.findAll().stream()
                .findFirst()
                .map(ContactMetaEntity::getDescription)
                .orElse("");

        var contacts = contactRepo.findAllByOrderByIdAsc().stream()
                .map(c -> new ContactResponse(c.getId(), c.getIcon(), c.getTitle(), c.getSubtitle()))
                .toList();

        return new ContactsResponse(description, contacts);
    }
}
