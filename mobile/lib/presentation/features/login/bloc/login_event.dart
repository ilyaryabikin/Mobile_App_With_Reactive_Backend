part of 'login_bloc.dart';

abstract class LoginEvent extends Equatable {
  const LoginEvent();
}

class LoginButtonPressed extends LoginEvent {
  final String username;
  final String password;

  const LoginButtonPressed({
    required this.username,
    required this.password,
  });

  @override
  List<Object> get props => [username, password];

  @override
  bool? get stringify => true;
}

class SignUpButtonPressed extends LoginEvent {
  final String username;
  final String password;
  final String? name;
  final String? surname;
  final String? phoneNumber;
  final String? city;

  const SignUpButtonPressed({
    required this.username,
    required this.password,
    this.name,
    this.surname,
    this.city,
    this.phoneNumber,
  });

  @override
  List<Object?> get props => [
        username,
        password,
        name,
        surname,
        city,
        phoneNumber,
      ];

  @override
  bool? get stringify => true;
}

class ShowLoginForm extends LoginEvent {
  @override
  List<Object> get props => [];
}

class ShowSignUpForm extends LoginEvent {
  @override
  List<Object> get props => [];
}
