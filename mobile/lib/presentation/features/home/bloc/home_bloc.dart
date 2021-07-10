import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter_simple_app/presentation/features/auth/bloc/auth_bloc.dart';
import 'package:flutter_simple_app/repositories/user_repository.dart';

part 'home_event.dart';
part 'home_state.dart';

class HomeBloc extends Bloc<HomeEvent, HomeState> {
  final UserRepository _userRepository;
  final AuthBloc _authBloc;

  HomeBloc(this._authBloc, this._userRepository) : super(ReadOnlyState());

  @override
  Stream<HomeState> mapEventToState(
    HomeEvent event,
  ) async* {
    if (event is LogoutEvent) {
      _authBloc.add(AuthLoggedOut());
    } else if (event is SwitchToEditableMode) {
      yield EditableState();
    } else if (event is ApplyChanges) {
      yield LoadingState();
      final result = await _userRepository.changeUser(
        currentUser: _authBloc.state.user!,
        name: event.name,
        surname: event.surname,
        phoneNumber: event.phoneNumber,
        city: event.city,
      );
      _authBloc.add(AuthInitialize());
      yield result.fold((l) => FailureState(l.message ?? 'Unknown Error'),
          (r) => SuccessState());
    } else if (event is DiscardChanges) {
      yield ReadOnlyState();
    }
  }
}
