import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'create_user_request_body.g.dart';

@JsonSerializable(createToJson: true)
class CreateUserRequestBody extends Equatable {
  final String password;
  final String username;
  final String? name;
  final String? surname;
  final String? phoneNumber;
  final String? city;

  CreateUserRequestBody({
    required this.password,
    required this.username,
    this.name,
    this.surname,
    this.phoneNumber,
    this.city,
  });

  Map<String, dynamic> toJson() => _$CreateUserRequestBodyToJson(this);

  @override
  List<Object?> get props => [
        password,
        username,
        name,
        surname,
        phoneNumber,
        city,
      ];
}
