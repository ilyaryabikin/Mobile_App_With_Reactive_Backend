part of 'login_bloc.dart';

abstract class LoginState extends Equatable {
  const LoginState();

  @override
  List<Object> get props => [];
}

class LoginInitial extends LoginState {}

class LoginLoading extends LoginState {
  final bool isLogin;

  const LoginLoading({required this.isLogin});

  @override
  List<Object> get props => [
        isLogin,
      ];
}

class LoginFormState extends LoginState {}

class SignUpFormState extends LoginState {}

class LoginFailure extends LoginState {
  final String error;
  final bool isLogin;

  const LoginFailure({required this.error, required this.isLogin});

  @override
  List<Object> get props => [
        error,
        isLogin,
      ];

  @override
  bool? get stringify => true;
}

class LoginSuccess extends LoginState {
  final String message;
  final bool isLogin;

  const LoginSuccess({required this.message, required this.isLogin});

  @override
  List<Object> get props => [
        message,
        isLogin,
      ];

  @override
  bool? get stringify => true;
}
