part of 'home_bloc.dart';

abstract class HomeState extends Equatable {
  final bool isReadOnly;
  const HomeState(this.isReadOnly);

  @override
  List<Object> get props => [isReadOnly];
}

class ReadOnlyState extends HomeState {
  const ReadOnlyState() : super(true);
}

class EditableState extends HomeState {
  const EditableState() : super(false);
}

class LoadingState extends HomeState {
  const LoadingState() : super(true);
}

class SuccessState extends HomeState {
  const SuccessState() : super(true);
}

class FailureState extends HomeState {
  final String message;
  const FailureState(this.message) : super(false);
}
