import 'package:json_annotation/json_annotation.dart';

part 'change_user_request_body.g.dart';

@JsonSerializable(createToJson: true)
class ChangeUserRequestBody {
  // final String password;
  // final String username;
  @JsonKey(includeIfNull: false)
  final String? name;
  @JsonKey(includeIfNull: false)
  final String? surname;
  @JsonKey(includeIfNull: false)
  final String? phoneNumber;
  @JsonKey(includeIfNull: false)
  final String? city;

  ChangeUserRequestBody({
    // required this.password,
    // required this.username,
    this.name,
    this.surname,
    this.phoneNumber,
    this.city,
  });

  Map<String, dynamic> toJson() => _$ChangeUserRequestBodyToJson(this);

  List<Object?> get props => [
        // password,
        // username,
        name,
        surname,
        phoneNumber,
        city,
      ];
}
