package se.ifmo.mobile.server.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "user")
public class UserDto implements Dto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Уникальный глобальный идентификатор пользователя",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String id;

  @Email(message = "User's username should be a correctly formatted email")
  @NotBlank(message = "User's username should not be blank")
  @Schema(
      description = "Имя пользователя (адрес электронной почты). Нельзя изменить после регистрации")
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank(message = "User's password should not be blank")
  @Size(min = 6, message = "User's password should be at leas 6 characters long")
  @Schema(
      description = "Пароль пользователя. Нельзя изменить после регистрации",
      accessMode = Schema.AccessMode.WRITE_ONLY)
  private String password;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(description = "Список прав пользователя", accessMode = Schema.AccessMode.READ_ONLY)
  private List<String> authorities;
}
