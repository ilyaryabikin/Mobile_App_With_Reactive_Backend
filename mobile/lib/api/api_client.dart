import 'package:dio/dio.dart';
import 'package:flutter_simple_app/models/entities/token.dart';
import 'package:flutter_simple_app/models/entities/user.dart';
import 'package:flutter_simple_app/models/request_bodies/change_user_request_body.dart';
import 'package:flutter_simple_app/models/request_bodies/create_user_request_body.dart';
import 'package:flutter_simple_app/models/request_bodies/login_request_body.dart';
import 'package:retrofit/retrofit.dart';

part 'api_client.g.dart';

@RestApi()
abstract class ApiService {
  factory ApiService(Dio dio, {String baseUrl}) = _ApiService;

  @POST('persons')
  Future<User> createUser(@Body() CreateUserRequestBody body);

  @POST('login')
  Future<Token> login(@Body() LoginRequestBody body);

  @GET('persons/{id}')
  Future<User> getUserById(
    @Path('id') String userId,
  );

  @PATCH('persons/{id}')
  Future<User> changeUser(
    @Path('id') String userId,
    @Body() ChangeUserRequestBody body,
  );

  @GET('persons/current')
  Future<User> getCurrentUser();
}
