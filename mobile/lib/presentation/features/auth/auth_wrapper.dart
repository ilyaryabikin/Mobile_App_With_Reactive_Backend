import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_simple_app/presentation/features/auth/bloc/auth_bloc.dart';
import 'package:flutter_simple_app/presentation/features/home/home_screen.dart';
import 'package:flutter_simple_app/presentation/features/login/login_screen.dart';

class AuthWrapper extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return BlocBuilder<AuthBloc, AuthState>(
      bloc: BlocProvider.of<AuthBloc>(context),
      builder: (context, state) {
        if (state.status == AuthStatus.UNAUTHENTICATED) {
          return LoginScreen();
        } else if (state.status == AuthStatus.AUTHENTICATED) {
          return HomeScreen();
        } else if (state.status == AuthStatus.FAILURE) {
          return Scaffold(
            body: Center(
              child: TextButton(
                  onPressed: () =>
                      BlocProvider.of<AuthBloc>(context).add(AuthInitialize()),
                  child: Text('Попробовать еще раз')),
            ),
          );
        }
        return const Scaffold(
          body: Center(child: CircularProgressIndicator()),
        );
      },
    );
  }
}
