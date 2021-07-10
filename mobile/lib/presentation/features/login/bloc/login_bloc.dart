import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter_simple_app/presentation/features/auth/bloc/auth_bloc.dart';
import 'package:flutter_simple_app/repositories/user_repository.dart';

part 'login_event.dart';

part 'login_state.dart';

class LoginBloc extends Bloc<LoginEvent, LoginState> {
  final UserRepository _userRepository;
  final AuthBloc _authBloc;

  LoginBloc(this._userRepository, this._authBloc) : super(LoginInitial());

  @override
  Stream<LoginState> mapEventToState(
    LoginEvent event,
  ) async* {
    if (event is ShowLoginForm) {
      yield LoginFormState();
    } else if (event is ShowSignUpForm) {
      yield SignUpFormState();
    } else if (event is LoginButtonPressed) {
      yield LoginLoading(isLogin: true);
      final result =
          await _userRepository.login(event.username, event.password);
      yield result.fold(
          (l) => LoginFailure(
              error: l.message ?? 'Неизвестная ошибка', isLogin: true), (r) {
        _authBloc.add(AuthLoggedIn(r));
        return LoginInitial();
      });
    } else if (event is SignUpButtonPressed) {
      yield LoginLoading(isLogin: false);
      final result = await _userRepository.createUser(
        username: event.username,
        password: event.password,
        name: event.name,
        surname: event.surname,
        city: event.city,
        phoneNumber: event.phoneNumber,
      );
      yield result.fold(
          (l) => LoginFailure(
              error: l.message ?? 'Неизвестная ошибка', isLogin: false), (r) {
        return LoginSuccess(
            message: 'Поздравляем, вы зарегистрированы', isLogin: true);
      });
    }
  }
}
