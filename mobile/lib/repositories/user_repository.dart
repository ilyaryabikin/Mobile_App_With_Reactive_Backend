import 'dart:io';
import 'package:dartz/dartz.dart';
import 'package:dio/dio.dart';
import 'package:flutter_simple_app/api/api_provider.dart';
import 'package:flutter_simple_app/api/token_handler.dart';
import 'package:flutter_simple_app/exceptions/failure.dart';
import 'package:flutter_simple_app/models/entities/token.dart';
import 'package:flutter_simple_app/models/entities/user.dart';
import 'package:flutter_simple_app/models/request_bodies/change_user_request_body.dart';
import 'package:flutter_simple_app/models/request_bodies/create_user_request_body.dart';
import 'package:flutter_simple_app/models/request_bodies/login_request_body.dart';

class UserRepository {
  final SecurityTokenHandler _tokenHandler;
  final ApiProvider _apiProvider;

  UserRepository(this._tokenHandler, this._apiProvider);

  Future<Either<Failure, User>> login(String username, String password) async {
    final apiService =
        await _apiProvider.getApiClientInterface(useToken: false);
    Token token;
    try {
      token = await apiService.login(LoginRequestBody(
        password: password,
        username: username,
      ));
      _tokenHandler.save(token);
      return Right(User(
        username: username,
        id: token.id,
      ));
    } on DioError catch (e) {
      switch (e.response?.statusCode ?? 0) {
        case 400:
        case 401:
          return Left(WrongCredentialsFailure());
        default:
          return Left(ServerFailure(e.response?.statusCode ?? 0));
      }
    } on SocketException catch (_) {
      return Left(ConnectionFailure());
    } on Exception catch (_) {
      return Left(UnknownFailure());
    }
  }

  void logout() {
    _tokenHandler.delete();
  }

  Future<Either<Failure, User?>> getAuthenticatedUserOrNull() async {
    final apiService = await _apiProvider.getApiClientInterface(useToken: true);
    try {
      final verifyRefresh = await _tokenHandler.verify();
      if (verifyRefresh == false) {
        return Right(null);
      }

      final user = await apiService.getCurrentUser();
      return Right(user);
    } on DioError catch (e) {
      if (e.error is SocketException) {
        return Left(ConnectionFailure());
      }
      return Left(
        (e.error is TokenInvalidFailure)
            ? TokenInvalidFailure()
            : ServerFailure(e.response?.statusCode ?? 0),
      );
    } on Exception {
      return Left(UnknownFailure());
    }
  }

  Future<Either<Failure, User>> createUser({
    required String username,
    required String password,
    String? name,
    String? surname,
    String? city,
    String? phoneNumber,
  }) async {
    final apiService =
        await _apiProvider.getApiClientInterface(useToken: false);
    try {
      final user = await apiService.createUser(CreateUserRequestBody(
        password: password,
        username: username,
        name: name,
        surname: surname,
        phoneNumber: phoneNumber,
        city: city,
      ));
      return Right(user);
    } on DioError catch (e) {
      switch (e.response!.statusCode) {
        case 400:
          return Left(WrongInputDataFailure());
        case 409:
          return Left(UserExistsFailure());
        default:
          return Left(ServerFailure(e.response?.statusCode ?? 0));
      }
    } on SocketException {
      return Left(ConnectionFailure());
    } on Exception {
      return Left(UnknownFailure());
    }
  }

  Future<Either<Failure, User>> changeUser({
    required User currentUser,
    String? name,
    String? surname,
    String? phoneNumber,
    String? city,
  }) async {
    final apiService = await _apiProvider.getApiClientInterface(useToken: true);
    try {
      final result = await apiService.changeUser(
          currentUser.id,
          ChangeUserRequestBody(
            // username: currentUser.username,
            name: name,
            surname: surname,
            phoneNumber: phoneNumber,
            city: city,
          ));
      return Right(result);
    } on DioError catch (e) {
      switch (e.response!.statusCode) {
        case 400:
          return Left(WrongInputDataFailure());
        case 401:
          return Left(TokenInvalidFailure());
        default:
          return Left(ServerFailure(e.response?.statusCode ?? 0));
      }
    } on SocketException catch (_) {
      return Left(ConnectionFailure());
    } on Exception catch (_) {
      return Left(UnknownFailure());
    }
  }
}
