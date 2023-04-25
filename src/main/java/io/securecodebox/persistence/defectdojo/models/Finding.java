/*
 *  secureCodeBox (SCB)
 *  Copyright 2021 iteratec GmbH
 *  https://www.iteratec.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.securecodebox.persistence.defectdojo.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.securecodebox.persistence.defectdojo.exceptions.DefectDojoPersistenceException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Finding extends DefectDojoModel {
  @JsonProperty
  Long id;

  @JsonProperty
  @NonNull
  String title;

  @JsonProperty
  @NonNull
  String description;

  @JsonProperty("found_by")
  @NonNull
  List<Long> foundBy;

  @JsonProperty
  @NonNull
  Severity severity;

  @JsonProperty
  @NonNull
  Long test;

  @JsonProperty
  String mitigation;

  @JsonProperty
  String impact;

  @JsonProperty
  @NonNull
  @Builder.Default
  Boolean active = true;

  @JsonProperty
  @NonNull
  @Builder.Default
  Boolean verified = true;

  @JsonProperty("risk_accepted")
  @NonNull
  @Builder.Default
  Boolean riskAccepted = false;

  @JsonProperty("out_of_scope")
  @NonNull
  @Builder.Default
  Boolean outOfScope = false;

  @JsonProperty
  @NonNull
  @Builder.Default
  Boolean duplicate = false;

  @JsonProperty("duplicate_finding")
  @Builder.Default
  Long duplicateFinding = null;

  @JsonProperty("false_p")
  @NonNull
  @Builder.Default
  Boolean falsePositive = false;

  @JsonProperty("component_name")
  String componentName;

  @JsonProperty("component_version")
  String componentVersion;

  @JsonProperty("file_path")
  String filePath;  
  
  @JsonProperty
  @NonNull
  @Builder.Default
  List<Long> endpoints = new LinkedList<>();

  @JsonProperty("created")
  LocalDateTime createdAt;

  @JsonProperty("mitigated")
  LocalDateTime mitigatedAt;

  @JsonProperty("accepted_risks")
  List<RiskAcceptance> acceptedRisks;

  @JsonProperty("numerical_severity")
  public String getNumericalSeverity() {
    switch (this.severity) {
      case Critical:
        return "S0";
      case High:
        return "S1";
      case Medium:
        return "S2";
      case Low:
        return "S3";
      case Informational:
        return "S4";
      default:
        throw new DefectDojoPersistenceException("Unknown severity: '" + this.severity + "'");
    }
  }

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    return queryParams.containsKey("id") && queryParams.get("id").equals(this.id);
  }

  public enum Severity {
    @JsonProperty("Critical")
    Critical(5),
    @JsonProperty("High")
    High(4),
    @JsonProperty("Medium")
    Medium(3),
    @JsonProperty("Low")
    Low(2),
    // Depending on the Scanner DefectDojo uses either Info or Informational
    // E.g. Nmap uses Info, Zap uses Informational
    @JsonProperty("Info")
    @JsonAlias("Informational")
    Informational(1),
    ;

    long severity;

    Severity(long severity) {
      this.severity = severity;
    }

    public long getNumericRepresentation() {
      return severity;
    }
  }
}
