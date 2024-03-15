package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import io.securecodebox.persistence.defectdojo.model.Engagement;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Provides pre configured Jackson mappers
 */
@Getter
@Accessors(fluent = true)
final class Mappers {
  private final ObjectMapper modelObjectMapper = new ObjectMapper();
  private final ObjectMapper searchStringMapper = new ObjectMapper();

  Mappers() {
    super();
    configureModelObjectMapper();
    configureSearchStringMapper();
  }

  private void configureModelObjectMapper() {
    modelObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    modelObjectMapper.coercionConfigFor(Engagement.Status.class)
      .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
    modelObjectMapper.findAndRegisterModules();
  }

  private void configureSearchStringMapper() {
    searchStringMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    searchStringMapper.coercionConfigFor(Engagement.Status.class)
      .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
    searchStringMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
  }
}
