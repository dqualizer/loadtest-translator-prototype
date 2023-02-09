package poc.loadtest.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import poc.dqlang.request.Request;

public interface k6Mapper {

    String newLine = System.lineSeparator();
    ObjectMapper objectMapper = new ObjectMapper();

    String map(Request request);
}