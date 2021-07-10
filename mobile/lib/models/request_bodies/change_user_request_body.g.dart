// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'change_user_request_body.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ChangeUserRequestBody _$ChangeUserRequestBodyFromJson(
    Map<String, dynamic> json) {
  return ChangeUserRequestBody(
    // password: json['password'] as String,
    // username: json['username'] as String,
    name: json['name'] as String?,
    surname: json['surname'] as String?,
    phoneNumber: json['phoneNumber'] as String?,
    city: json['city'] as String?,
  );
}

Map<String, dynamic> _$ChangeUserRequestBodyToJson(
    ChangeUserRequestBody instance) {
  final val = <String, dynamic>{
    // 'password': instance.password,
    // 'username': instance.username,
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('name', instance.name);
  writeNotNull('surname', instance.surname);
  writeNotNull('phoneNumber', instance.phoneNumber);
  writeNotNull('city', instance.city);
  return val;
}
