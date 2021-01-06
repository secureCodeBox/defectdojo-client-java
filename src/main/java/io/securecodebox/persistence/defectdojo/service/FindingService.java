package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.models.DefectDojoResponse;
import io.securecodebox.persistence.defectdojo.models.Finding;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FindingService extends GenericDefectDojoService<Finding> {
  public FindingService(DefectDojoConfig config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "findings";
  }

  @Override
  protected Class<Finding> getModelClass() {
    return Finding.class;
  }

  @Override
  protected DefectDojoResponse<Finding> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }

  public List<Finding> getUnhandledFindingsForProduct(long productId, Finding.Severity minimumSeverity) throws URISyntaxException, JsonProcessingException {
    return this.search(Map.of("test__engagement__product", productId, "active", true)).stream().filter((finding -> {
      return finding.getSeverity().getNumericRepresentation() >= minimumSeverity.getNumericRepresentation();
    })).collect(Collectors.toList());
  }
  public List<Finding> getUnhandledFindingsForEngagement(long engagementId, Finding.Severity minimumSeverity) throws URISyntaxException, JsonProcessingException {
    return this.search(Map.of("test__engagement", engagementId, "active", true)).stream().filter((finding -> {
      return finding.getSeverity().getNumericRepresentation() >= minimumSeverity.getNumericRepresentation();
    })).collect(Collectors.toList());
  }
}
