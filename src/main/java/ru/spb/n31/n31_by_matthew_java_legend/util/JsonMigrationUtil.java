package ru.spb.n31.n31_by_matthew_java_legend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Transactional
public class JsonMigrationUtil {

    private final ObjectMapper mapper;

    protected JsonNode read(String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            return mapper.readTree(is);
        }
    }
}
