// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Finding implements Model, HasId {
  /**
   * Uniq id of model type
   * <p>
   * May be {@code null} for newly created objects because in DefectDojo's Open API specification i.
   * It is mandatory to use a boxed object type instead of a native type. A native type would result in 0 by
   * default which is a valid id for DefectDojo. Thus creating this type via POST request would try to create
   * one with id 0. Instead the id must be {@code null}, so that DefectDojo uses a newly generated uniq id.
   * </p>
   */
  @JsonProperty
  private Long id;

  @JsonProperty
  @NonNull
  private String title;

  @JsonProperty
  @NonNull
  private String description;

  @JsonProperty("found_by")
  @NonNull
  private List<Long> foundBy;

  @JsonProperty
  @NonNull
  private Severity severity;

  @JsonProperty
  @NonNull
  private Long test;// FIXME: Use native type here.

  @JsonProperty
  private String mitigation;

  @JsonProperty
  private String impact;

  @JsonProperty
  @NonNull
  @Builder.Default
  private Boolean active = true;// FIXME: Use native type here.

  @JsonProperty
  @NonNull
  @Builder.Default
  private Boolean verified = true;// FIXME: Use native type here.

  @JsonProperty("risk_accepted")
  @NonNull
  @Builder.Default
  private Boolean riskAccepted = false;// FIXME: Use native type here.

  @JsonProperty("out_of_scope")
  @NonNull
  @Builder.Default
  private Boolean outOfScope = false;// FIXME: Use native type here.

  @JsonProperty
  @NonNull
  @Builder.Default
  private Boolean duplicate = false;// FIXME: Use native type here.

  @JsonProperty("duplicate_finding")
  @Builder.Default
  private Long duplicateFinding = null;// FIXME: Use native type here.

  @JsonProperty("false_p")
  @NonNull
  @Builder.Default
  private Boolean falsePositive = false;// FIXME: Use native type here.

  @JsonProperty("component_name")
  private String componentName;

  @JsonProperty("component_version")
  private String componentVersion;

  @JsonProperty("file_path")
  private String filePath;

  @JsonProperty
  @NonNull
  @Builder.Default
  private List<Long> endpoints = new LinkedList<>();

  @JsonProperty("created")
  private OffsetDateTime createdAt;

  @JsonProperty("mitigated")
  private OffsetDateTime mitigatedAt;

  @JsonProperty("accepted_risks")
  @Builder.Default
  private List<RiskAcceptance> acceptedRisks = new ArrayList<>();

  @JsonProperty("numerical_severity")
  public String getNumericalSeverity() {
    switch (this.severity) {
      case CRITICAL:
        return "S0";
      case HIGH:
        return "S1";
      case MEDIUM:
        return "S2";
      case LOW:
        return "S3";
      case INFORMATIONAL:
        return "S4";
      default:
        throw new PersistenceException("Unknown severity: '" + this.severity + "'");
    }
  }

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (QueryParamsComparator.isNull(queryParams)) {
      return false;
    }

    return QueryParamsComparator.isIdEqual(this, queryParams);
  }

  public enum Severity {
    @JsonProperty("Critical")
    CRITICAL(5),
    @JsonProperty("High")
    HIGH(4),
    @JsonProperty("Medium")
    MEDIUM(3),
    @JsonProperty("Low")
    LOW(2),
    // Depending on the Scanner DefectDojo uses either Info or Informational
    // E.g. Nmap uses Info, Zap uses Informational
    @JsonProperty("Info")
    @JsonAlias("Informational")
    INFORMATIONAL(1);

    final long severity;

    Severity(long severity) {
      this.severity = severity;
    }

    public long getNumericRepresentation() {
      return severity;
    }
  }
}
