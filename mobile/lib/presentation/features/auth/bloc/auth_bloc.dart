import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter_simple_app/models/entities/user.dart';
import 'package:flutter_simple_app/repositories/user_repository.dart';

part 'auth_event.dart';
part 'auth_state.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {
  final UserRepository _userRepository;

  AuthBloc(this._userRepository) : super(AuthState.unknown());

  @override
  Stream<AuthState> mapEventToState(
    AuthEvent event,
  ) async* {
    if (event is AuthInitialize) {
      yield AuthState.unknown();
      final result = await _userRepository.getAuthenticatedUserOrNull();
      yield result.fold(
        (failure) => AuthState.failure(),
        (user) => (user == null)
            ? AuthState.unauthenticated()
            : AuthState.authenticated(user),
      );
    } else if (event is AuthLoggedIn) {
      final result = await _userRepository.getAuthenticatedUserOrNull();
      yield AuthState.authenticated(
          result.isRight() ? (result as Right).value : event.user);
    } else if (event is AuthLoggedOut) {
      _userRepository.logout();
      yield AuthState.logOuted();
    }
  }
}
