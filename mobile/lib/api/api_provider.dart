import 'package:dio/dio.dart';
import 'package:flutter_simple_app/api/token_handler.dart';
import 'package:flutter_simple_app/constants.dart';

import 'api_client.dart';

class ApiProvider {
  final SecurityTokenHandler _tokenHandler;

  ApiProvider(
    this._tokenHandler,
  );

  Future<ApiService> getApiClientInterface({
    bool useToken = false,
  }) async {
    final dio = Dio();
    dio.interceptors.add(
      InterceptorsWrapper(
        onRequest:
            (RequestOptions options, RequestInterceptorHandler handler) async {
          final headers = <String, dynamic>{};
          // headers['accept'] = headers['Content-Type'] = 'application/json';
          if (useToken) {
            final token = await _tokenHandler.getToken();
            headers.putIfAbsent(
              'authorization',
              () => 'Bearer ${token.accessToken}',
            );
          }
          options.headers.addAll(headers);
          handler.next(options);
        },
      ),
    );
    // dio.interceptors.add(LogInterceptor(
    //   request: true,
    //   requestHeader: true,
    //   responseHeader: true,
    //   requestBody: true,
    //   responseBody: true,
    //   logPrint: print,
    // ));

    return ApiService(
      dio,
      baseUrl: serverApiUrl,
    );
  }
}
