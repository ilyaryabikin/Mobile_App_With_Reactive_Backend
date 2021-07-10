import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_simple_app/api/api_provider.dart';
import 'package:flutter_simple_app/api/token_handler.dart';
import 'package:flutter_simple_app/constants.dart';
import 'package:flutter_simple_app/presentation/features/auth/auth_wrapper.dart';
import 'package:flutter_simple_app/presentation/features/auth/bloc/auth_bloc.dart';
import 'package:flutter_simple_app/repositories/user_repository.dart';

void main() {
  runZonedGuarded(() => runApp(MyApp()), (Object error, StackTrace stack) {
    print(error);
    print(stack);
  });
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: appName,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: RepositoryProvider(
        create: (context) => UserRepository(
          SecurityTokenHandler(FlutterSecureStorage()),
          ApiProvider(SecurityTokenHandler(FlutterSecureStorage())),
        ),
        child: BlocProvider(
          create: (context) => AuthBloc(
            RepositoryProvider.of<UserRepository>(
              context,
            ),
          )..add(AuthInitialize()),
          child: AuthWrapper(),
        ),
      ),
    );
  }
}
