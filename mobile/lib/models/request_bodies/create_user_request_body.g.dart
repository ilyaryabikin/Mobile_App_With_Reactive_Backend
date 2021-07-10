// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'create_user_request_body.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CreateUserRequestBody _$CreateUserRequestBodyFromJson(
    Map<String, dynamic> json) {
  return CreateUserRequestBody(
    password: json['password'] as String,
    username: json['username'] as String,
    name: json['name'] as String?,
    surname: json['surname'] as String?,
    phoneNumber: json['phoneNumber'] as String?,
    city: json['city'] as String?,
  );
}

Map<String, dynamic> _$CreateUserRequestBodyToJson(
        CreateUserRequestBody instance) =>
    <String, dynamic>{
      'password': instance.password,
      'username': instance.username,
      'name': instance.name,
      'surname': instance.surname,
      'phoneNumber': instance.phoneNumber,
      'city': instance.city,
    };
