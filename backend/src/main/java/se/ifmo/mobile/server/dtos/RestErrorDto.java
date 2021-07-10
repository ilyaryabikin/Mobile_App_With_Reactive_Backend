package se.ifmo.mobile.server.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "error", accessMode = Schema.AccessMode.READ_ONLY)
public class RestErrorDto implements Dto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Дата возникновения ошибки",
      example = "2021-05-08T16:43:58.932+00:00",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Date timestamp;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Путь запроса, по которому произошла ошибка",
      example = "/api/users/current",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String path;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "HTTP-статус ошибки",
      example = "406",
      accessMode = Schema.AccessMode.READ_ONLY)
  private int status;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Расшифровка HTTP-статуса ошибки",
      example = "Not Acceptable",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String error;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Описание ошибки",
      example = "Could not find acceptable representation",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String message;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(
      description = "Идентификатор запроса с ошибкой",
      example = "978f7996-1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String requestId;
}
