import 'package:equatable/equatable.dart';

class Failure extends Equatable {
  final String? message;

  // If the subclasses have some properties, they'll get passed to this constructor
  // so that Equatable can perform value comparison.
  const Failure(this.message);

  @override
  List<Object?> get props => [message];
}

class ServerFailure extends Failure {
  final int statusCode;

  ServerFailure(this.statusCode) : super('Ошибка сервера : $statusCode');

  @override
  List<Object?> get props => [statusCode, message];
}

class ConnectionFailure extends Failure {
  ConnectionFailure() : super('Ошибка подключения к серверу');
}

class TokenInvalidFailure extends Failure {
  const TokenInvalidFailure() : super('Ошибка авторизации');
}

class WrongCredentialsFailure extends Failure {
  const WrongCredentialsFailure()
      : super('Не найдено пользователя c такими данными');
}

class UserExistsFailure extends Failure {
  const UserExistsFailure()
      : super('Пользователь с таким логином уже существует');
}

class WrongInputDataFailure extends Failure {
  const WrongInputDataFailure()
      : super('Некорректные данные, попробуйте корректнее');
}

class UnknownFailure extends Failure {
  UnknownFailure() : super('Что-то пошло не так');
}
