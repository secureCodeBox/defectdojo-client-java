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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Finding implements Model {
  @JsonProperty
  private long id;

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
  private long test;

  @JsonProperty
  private String mitigation;

  @JsonProperty
  private String impact;

  @JsonProperty
  @NonNull
  private boolean active;

  @JsonProperty
  @NonNull
  private boolean verified;

  @JsonProperty("risk_accepted")
  @NonNull
  private boolean riskAccepted;

  @JsonProperty("out_of_scope")
  @NonNull
  private boolean outOfScope;

  @JsonProperty
  @NonNull
  private boolean duplicate;

  @JsonProperty("duplicate_finding")
  private long duplicateFinding;

  @JsonProperty("false_p")
  @NonNull
  private boolean falsePositive;

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
  private List<RiskAcceptance> acceptedRisks;

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
    return queryParams.containsKey("id") && queryParams.get("id").equals(this.id);
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
