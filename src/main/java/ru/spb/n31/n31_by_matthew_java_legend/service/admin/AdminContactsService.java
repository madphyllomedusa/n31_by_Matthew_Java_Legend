package ru.spb.n31.n31_by_matthew_java_legend.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.dto.request.ContactRequest;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ContactResponse;
import ru.spb.n31.n31_by_matthew_java_legend.dto.response.ContactsResponse;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ContactEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ContactMetaEntity;
import ru.spb.n31.n31_by_matthew_java_legend.exception.BadRequestException;
import ru.spb.n31.n31_by_matthew_java_legend.exception.NotFoundException;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ContactMetaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ContactRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminContactsService {

    private final ContactMetaRepository metaRepo;
    private final ContactRepository contactRepo;

    public ContactsResponse get() {
        String description = metaRepo.findAll().stream()
                .findFirst()
                .map(ContactMetaEntity::getDescription)
                .orElse("");

        var contacts = contactRepo.findAllByOrderByIdAsc().stream()
                .map(c -> new ContactResponse(c.getId(), c.getIcon(), c.getTitle(), c.getSubtitle()))
                .toList();

        return new ContactsResponse(description, contacts);
    }

    public ContactsResponse setDescription(String description) {
        var meta = metaRepo.findAll().stream().findFirst().orElseGet(() -> {
            var m = new ContactMetaEntity();
            m.setDescription("");
            return metaRepo.save(m);
        });

        meta.setDescription(description);
        return get();
    }

    public ContactResponse create(ContactRequest r) {
        if (contactRepo.existsById(r.id())) throw new BadRequestException("Contact exists: " + r.id());
        var c = new ContactEntity();
        c.setId(r.id());
        c.setIcon(r.icon());
        c.setTitle(r.title());
        c.setSubtitle(r.subtitle());
        contactRepo.save(c);
        return new ContactResponse(c.getId(), c.getIcon(), c.getTitle(), c.getSubtitle());
    }

    public ContactResponse update(Long id, ContactRequest r) {
        var c = contactRepo.findById(id).orElseThrow(() -> new NotFoundException("Contact not found: " + id));
        c.setIcon(r.icon());
        c.setTitle(r.title());
        c.setSubtitle(r.subtitle());
        return new ContactResponse(c.getId(), c.getIcon(), c.getTitle(), c.getSubtitle());
    }

    public void delete(Long id) {
        if (!contactRepo.existsById(id)) return;
        contactRepo.deleteById(id);
    }
}

