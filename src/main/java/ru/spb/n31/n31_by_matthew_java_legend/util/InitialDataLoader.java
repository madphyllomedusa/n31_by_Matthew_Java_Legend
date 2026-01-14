package ru.spb.n31.n31_by_matthew_java_legend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.n31.n31_by_matthew_java_legend.entity.AboutUsStatsEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.AboutUsTextEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ContactEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ContactMetaEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ServiceEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.ServiceTypeExampleEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.SubserviceEntity;
import ru.spb.n31.n31_by_matthew_java_legend.entity.SubserviceTypeEntity;
import ru.spb.n31.n31_by_matthew_java_legend.repository.AboutUsStatsRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.AboutUsTextRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ContactMetaRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ContactRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.ServiceTypeExampleRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceRepository;
import ru.spb.n31.n31_by_matthew_java_legend.repository.SubserviceTypeRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialDataLoader {

    private final ObjectMapper mapper;

    private final ServiceRepository serviceRepo;
    private final SubserviceRepository subserviceRepo;
    private final SubserviceTypeRepository typeRepo;
    private final ServiceTypeExampleRepository exampleRepo;
    private final AboutUsTextRepository aboutTextRepo;
    private final AboutUsStatsRepository aboutStatsRepo;
    private final ContactRepository contactRepo;
    private final ContactMetaRepository contactMetaRepo;

    @PostConstruct
    @Transactional
    public void load() throws Exception {
        // Важно: грузим по-таблично, чтобы после падения на середине можно было перезапустить и догрузить.
        if (serviceRepo.count() == 0) {
            loadServices();
        } else {
            log.info("Skip services: already loaded (count={})", serviceRepo.count());
        }

        // subservices/types - связанная история, но проверяем отдельно
        if (subserviceRepo.count() == 0 || typeRepo.count() == 0) {
            loadSubservicesAndTypes();
        } else {
            log.info("Skip subservices/types: already loaded (subservices={}, types={})", subserviceRepo.count(), typeRepo.count());
        }

        if (exampleRepo.count() == 0) {
            loadExamples();
        } else {
            log.info("Skip examples: already loaded (count={})", exampleRepo.count());
        }

        if (aboutTextRepo.count() == 0 || aboutStatsRepo.count() == 0) {
            loadAboutUs();
        } else {
            log.info("Skip about-us: already loaded (text={}, stats={})", aboutTextRepo.count(), aboutStatsRepo.count());
        }

        if (contactRepo.count() == 0 || contactMetaRepo.count() == 0) {
            loadContacts();
        } else {
            log.info("Skip contacts: already loaded (contacts={}, meta={})", contactRepo.count(), contactMetaRepo.count());
        }

        log.info("Initial data load finished.");
    }

    /* ===================== helpers ===================== */

    private JsonNode read(String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) throw new IllegalStateException("JSON not found: " + path);
            return mapper.readTree(is);
        }
    }

    private long requireLong(JsonNode node, String field, String context) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) {
            throw new IllegalStateException("Missing field '" + field + "' in " + context + ": " + node);
        }
        if (!v.canConvertToLong()) {
            throw new IllegalStateException("Field '" + field + "' is not a number in " + context + ": " + node);
        }
        long val = v.asLong();
        if (val <= 0) {
            throw new IllegalStateException("Field '" + field + "' must be > 0 in " + context + ": " + node);
        }
        return val;
    }

    private int requireInt(JsonNode node, String field, String context) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) {
            throw new IllegalStateException("Missing field '" + field + "' in " + context + ": " + node);
        }
        if (!v.canConvertToInt()) {
            throw new IllegalStateException("Field '" + field + "' is not an int in " + context + ": " + node);
        }
        return v.asInt();
    }

    private String requireText(JsonNode node, String field, String context) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) {
            throw new IllegalStateException("Missing field '" + field + "' in " + context + ": " + node);
        }
        String val = v.asText();
        if (val == null || val.isBlank()) {
            throw new IllegalStateException("Field '" + field + "' is blank in " + context + ": " + node);
        }
        return val;
    }

    private String optText(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v == null || v.isNull()) ? null : v.asText(null);
    }

    private Integer optInt(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v == null || v.isNull() || !v.canConvertToInt()) ? null : v.asInt();
    }

    /* ===================== loaders ===================== */

    private void loadServices() throws Exception {
        JsonNode arr = read("/json/services.json").path("services");
        if (!arr.isArray()) throw new IllegalStateException("services.json: expected 'services' array");

        int inserted = 0;

        for (JsonNode n : arr) {
            String id = requireText(n, "id", "services");

            if (serviceRepo.existsById(id)) continue;

            ServiceEntity s = new ServiceEntity();
            s.setId(id);
            s.setTitle(requireText(n, "title", "services[id=" + id + "]"));
            s.setPrice(requireInt(n, "price", "services[id=" + id + "]"));
            s.setImage(optText(n, "image"));

            serviceRepo.save(s);
            inserted++;
        }

        log.info("Loaded services: inserted={}", inserted);
    }

    private void loadSubservicesAndTypes() throws Exception {
        JsonNode arr = read("/json/subservices.json").path("subservices");
        if (!arr.isArray()) throw new IllegalStateException("subservices.json: expected 'subservices' array");

        int subInserted = 0;
        int typeInserted = 0;

        for (JsonNode s : arr) {
            String subId = requireText(s, "subserviceId", "subservices");
            SubserviceEntity sub = subserviceRepo.findById(subId).orElse(null);
            if (sub == null) {
                sub = new SubserviceEntity();
                sub.setId(subId);
                sub.setTitle(optText(s, "title"));
                sub.setDescription(optText(s, "description"));
                sub.setWorkHours(optText(s, "workHours"));
                sub.setAveragePrice(optInt(s, "averagePrice"));
                subserviceRepo.save(sub);
                subInserted++;
            }

            JsonNode types = s.path("types");
            if (!types.isArray()) continue;

            for (JsonNode t : types) {
                String typeId = requireText(t, "id", "subservices[subserviceId=" + subId + "].types");
                if (typeRepo.existsById(typeId)) continue;

                String serviceId = requireText(t, "serviceId", "type[id=" + typeId + "]");

                ServiceEntity service = serviceRepo.findById(serviceId)
                        .orElseThrow(() -> new IllegalStateException(
                                "Type id=" + typeId + " references missing serviceId=" + serviceId));

                SubserviceTypeEntity type = new SubserviceTypeEntity();
                type.setId(typeId);
                type.setTitle(optText(t, "title"));
                type.setImage(optText(t, "image"));
                type.setService(service);
                type.setSubservice(sub);

                typeRepo.save(type);
                typeInserted++;
            }
        }

        log.info("Loaded subservices/types: subservicesInserted={}, typesInserted={}", subInserted, typeInserted);
    }

    private void loadExamples() throws Exception {
        JsonNode root = read("/json/servicesTypesProjects.json").path("servicesTypesProjects");
        if (!root.isArray())
            throw new IllegalStateException("servicesTypesProjects.json: expected 'servicesTypesProjects' array");

        int inserted = 0;
        Set<String> missingTypeIds = new LinkedHashSet<>();

        for (JsonNode block : root) {
            JsonNode examples = block.path("examples");
            if (!examples.isArray()) continue;

            for (JsonNode ex : examples) {
                String exId = requireText(ex, "id", "examples");
                if (exampleRepo.existsById(exId)) continue;

                String typeId = requireText(ex, "typeId", "examples[id=" + exId + "]");

                SubserviceTypeEntity type = typeRepo.findById(typeId).orElse(null);
                if (type == null) {
                    // Собираем все отсутствующие typeId и валимся одним понятным исключением
                    missingTypeIds.add(typeId);
                    continue;
                }

                ServiceTypeExampleEntity e = new ServiceTypeExampleEntity();
                e.setId(exId);
                e.setTitle(optText(ex, "title"));
                e.setDescription(optText(ex, "description"));
                e.setPrice(optInt(ex, "price"));
                e.setImage(optText(ex, "image"));
                e.setType(type);

                exampleRepo.save(e);
                inserted++;
            }
        }

        if (!missingTypeIds.isEmpty()) {
            throw new IllegalStateException("Examples reference missing subservice_types ids: " + missingTypeIds
                    + ". Проверь, что эти typeId реально есть в subservices.json -> types[].id");
        }

        log.info("Loaded examples: inserted={}", inserted);
    }

    private void loadAboutUs() throws Exception {
        JsonNode root = read("/json/aboutUs.json");

        JsonNode desc = root.path("description");
        if (aboutTextRepo.count() == 0 && desc.isArray()) {
            int order = 0;
            for (JsonNode text : desc) {
                AboutUsTextEntity e = new AboutUsTextEntity();
                e.setText(text.asText());
                e.setOrderNum(order++);
                aboutTextRepo.save(e);
            }
            log.info("Loaded about_us_text: inserted={}", order);
        }

        JsonNode stats = root.path("stats");
        int insertedStats = 0;
        if (aboutStatsRepo.count() == 0 && stats.isArray()) {
            for (JsonNode s : stats) {
                long id = requireLong(s, "id", "aboutUs.stats");
                if (aboutStatsRepo.existsById(id)) continue;

                AboutUsStatsEntity e = new AboutUsStatsEntity();
                e.setId(id);
                e.setUpper(optText(s, "upper"));
                e.setLower(optText(s, "lower"));
                aboutStatsRepo.save(e);
                insertedStats++;
            }
            log.info("Loaded about_us_stats: inserted={}", insertedStats);
        }
    }

    private void loadContacts() throws Exception {
        JsonNode root = read("/json/contacts.json");

        if (contactMetaRepo.count() == 0) {
            String description = root.path("description").asText("");
            if (!description.isBlank()) {
                ContactMetaEntity meta = new ContactMetaEntity();
                meta.setDescription(description);
                contactMetaRepo.save(meta);
                log.info("Loaded contacts_meta: inserted=1");
            }
        }

        JsonNode contacts = root.path("contacts");
        if (contacts.isArray()) {
            int inserted = 0;
            for (JsonNode c : contacts) {
                long id = requireLong(c, "id", "contacts");
                if (contactRepo.existsById(id)) continue;

                ContactEntity e = new ContactEntity();
                e.setId(id);
                e.setIcon(optText(c, "icon"));
                e.setTitle(optText(c, "title"));
                e.setSubtitle(optText(c, "subtitle"));

                contactRepo.save(e);
                inserted++;
            }
            log.info("Loaded contacts: inserted={}", inserted);
        }
    }
}
