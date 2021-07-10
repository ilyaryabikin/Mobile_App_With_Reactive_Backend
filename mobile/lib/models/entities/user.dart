import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'user.g.dart';

@JsonSerializable(createToJson: true, createFactory: true)
class User extends Equatable {
  final String id;
  final String username;
  final String? name;
  final String? surname;
  final String? phoneNumber;
  final String? city;

  User({
    required this.id,
    required this.username,
    this.name,
    this.surname,
    this.phoneNumber,
    this.city,
  });

  factory User.fromJson(Map<String, dynamic> json) => _$UserFromJson(json);

  Map<String, dynamic> toJson() => _$UserToJson(this);

  @override
  List<Object?> get props => [
        id,
        username,
        name,
        surname,
        phoneNumber,
        city,
      ];
}
