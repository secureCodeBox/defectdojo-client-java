package io.securecodebox.persistence.defectdojo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Endpoint extends DefectDojoModel {
  @JsonProperty
  Long id;

  @JsonProperty
  String protocol;

  @JsonProperty
  String host;

  @JsonProperty("fqdm")
  String fullyQualifiedDomainName;

  @JsonProperty
  Long port;

  @JsonProperty
  String path;

  @JsonProperty
  String query;

  @JsonProperty
  String fragment;

  @JsonProperty
  Long product;

  @JsonProperty
  Boolean mitigated;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
      return true;
    }
    return false;
  }
}
