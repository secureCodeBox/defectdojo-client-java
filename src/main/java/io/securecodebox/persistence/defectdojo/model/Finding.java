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
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Finding extends BaseModel {
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
    OffsetDateTime createdAt;

    @JsonProperty("mitigated")
    OffsetDateTime mitigatedAt;

    @JsonProperty("accepted_risks")
    List<RiskAcceptance> acceptedRisks;

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

        long severity;

        Severity(long severity) {
            this.severity = severity;
        }

        public long getNumericRepresentation() {
            return severity;
        }
    }
}
