part of 'auth_bloc.dart';

class AuthState extends Equatable {
  final AuthStatus status;
  final bool fromLogout;
  final User? user;

  const AuthState._({
    this.status = AuthStatus.UNKNOWN,
    this.fromLogout = false,
    this.user,
  });

  const AuthState.unknown() : this._();

  const AuthState.authenticated(user)
      : this._(status: AuthStatus.AUTHENTICATED, user: user);

  const AuthState.unauthenticated()
      : this._(status: AuthStatus.UNAUTHENTICATED);

  const AuthState.failure() : this._(status: AuthStatus.FAILURE);

  const AuthState.logOuted()
      : this._(status: AuthStatus.UNAUTHENTICATED, fromLogout: true);

  @override
  List<Object?> get props => [status, user, fromLogout];
}

enum AuthStatus { UNKNOWN, AUTHENTICATED, UNAUTHENTICATED, FAILURE }
