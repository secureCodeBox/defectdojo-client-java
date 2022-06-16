package io.securecodebox.persistence.defectdojo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductGroup extends DefectDojoModel {
    @JsonProperty
    Long id;

    @JsonProperty
    Long product;

    @JsonProperty
    Long group;

    @JsonProperty
    Long role;

    @Override
    public boolean equalsQueryString(Map<String, Object> queryParams) {
        if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
            return true;
        }
        if (queryParams.containsKey("product") && queryParams.get("product").equals(this.product) && queryParams.containsKey("group") && queryParams.get("group").equals(this.group)) {
            return true;
        }
        if (queryParams.containsKey("product") && queryParams.get("product").equals(this.product) && !queryParams.containsKey("group")) {
            return true;
        }
        if (queryParams.containsKey("group") && queryParams.get("group").equals(this.group) && !queryParams.containsKey("product")) {
            return true;
        }
        return false;
    }
}
