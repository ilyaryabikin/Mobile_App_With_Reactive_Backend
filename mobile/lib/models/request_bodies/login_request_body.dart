import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'login_request_body.g.dart';

@JsonSerializable(createToJson: true)
class LoginRequestBody extends Equatable {
  final String username;
  final String password;

  LoginRequestBody({
    required this.password,
    required this.username,
  });

  Map<String, dynamic> toJson() => _$LoginRequestBodyToJson(this);

  @override
  List<Object?> get props => [
        username,
        password,
      ];
}
