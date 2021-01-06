package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.models.DefectDojoResponse;
import io.securecodebox.persistence.defectdojo.models.User;

public class UserService extends GenericDefectDojoService<User> {
  public UserService(DefectDojoConfig config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "users";
  }

  @Override
  protected Class<User> getModelClass() {
    return User.class;
  }

  @Override
  protected DefectDojoResponse<User> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
