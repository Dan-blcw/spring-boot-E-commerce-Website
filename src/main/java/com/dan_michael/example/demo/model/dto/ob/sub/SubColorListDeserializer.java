package com.dan_michael.example.demo.model.dto.ob.sub;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

public class SubColorListDeserializer extends JsonDeserializer<List<SubColor>> {

    @Override
    public List<SubColor> deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        return mapper.readValue(p, mapper.getTypeFactory().constructCollectionType(List.class, SubColor.class));
    }
}
