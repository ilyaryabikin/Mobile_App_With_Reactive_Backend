package se.ifmo.mobile.server.dtos;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "login")
public class LoginDto implements Dto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Уникальный глобальный идентификатор пользователя",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String id;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Email(message = "User's username should be a correctly formatted email")
  @NotBlank(message = "User's username should not be blank")
  @Schema(
      description = "Имя пользователя (адрес электронной почты)",
      accessMode = Schema.AccessMode.WRITE_ONLY,
      required = true)
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank(message = "User's password should not be blank")
  @Size(min = 6, message = "User's password should be at leas 6 characters long")
  @Schema(
      description = "Пароль пользователя",
      accessMode = Schema.AccessMode.WRITE_ONLY,
      required = true)
  private String password;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(description = "Токен авторизации пользователя", accessMode = Schema.AccessMode.READ_ONLY)
  private String token;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(description = "Список прав пользователя", accessMode = Schema.AccessMode.READ_ONLY)
  private List<String> authorities;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Дата истечения срока действия токена авторизации пользователя",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Date expiresAt;
}
