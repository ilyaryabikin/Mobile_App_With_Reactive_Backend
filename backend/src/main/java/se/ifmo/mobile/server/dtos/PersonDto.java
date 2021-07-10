package se.ifmo.mobile.server.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.ifmo.mobile.server.constraints.PhoneNumber;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "person")
public class PersonDto implements Dto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Уникальный глобальный идентификатор пользователя",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String id;

  @Email(message = "User's username should be a correctly formatted email")
  @Schema(
      description = "Имя пользователя (адрес электронной почты). Нельзя изменить после регистрации")
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(min = 6, message = "User's password should be at least 6 characters long")
  @Schema(
      description = "Пароль пользователя. Нельзя изменить после регистрации",
      accessMode = Schema.AccessMode.WRITE_ONLY)
  private String password;

  @Schema(description = "Имя человека")
  private String name;

  @Schema(description = "Фамилия человека")
  private String surname;

  @PhoneNumber(message = "User's phone number should be valid")
  @Schema(description = "Номер телефона человека")
  private String phoneNumber;

  @Schema(description = "Город человека")
  private String city;
}
