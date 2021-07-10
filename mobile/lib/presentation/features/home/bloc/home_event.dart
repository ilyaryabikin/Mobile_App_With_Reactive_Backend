part of 'home_bloc.dart';

abstract class HomeEvent extends Equatable {
  const HomeEvent();

  @override
  List<Object?> get props => [];
}

class LogoutEvent extends HomeEvent {}

class SwitchToEditableMode extends HomeEvent {}

class ApplyChanges extends HomeEvent {
  final String? name;
  final String? surname;
  final String? phoneNumber;
  final String? city;

  ApplyChanges({this.name, this.surname, this.phoneNumber, this.city});

  @override
  List<Object?> get props => [name, surname, phoneNumber, city];
}

class DiscardChanges extends HomeEvent {}
