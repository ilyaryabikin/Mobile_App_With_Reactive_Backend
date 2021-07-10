import 'dart:convert' as convert;

import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_simple_app/models/entities/token.dart';
import 'package:flutter_simple_app/exceptions/failure.dart';

class SecurityTokenHandler {
  final String _tokenKey = 'token';
  final FlutterSecureStorage _storage;

  SecurityTokenHandler(
    this._storage,
  );

  Future<Token> getToken() async {
    var stringFromJsonData, token;

    stringFromJsonData = await _storage.read(key: _tokenKey);

    if (stringFromJsonData == null) throw TokenInvalidFailure();

    token = Token.fromJson(convert.jsonDecode(stringFromJsonData));

    final verifyToken = await verify();

    if (verifyToken) {
      return token;
    } else {
      throw TokenInvalidFailure();
    }
  }

  Future<Token> obtain(Token token) async {
    await save(token);
    return token;
  }

  Future<void> save(token) async {
    var _token = Token(
      token.id,
      token.accessToken,
      token.expiresAt,
    );
    var stringFromJsonData = convert.jsonEncode(_token.toJson());
    await _storage.write(key: _tokenKey, value: stringFromJsonData);
  }

  Future<void> delete() async {
    await _storage.delete(key: _tokenKey);
  }

  Future<bool> verify() async {
    try {
      final stringFromJsonData = await _storage.read(key: _tokenKey);
      if (stringFromJsonData != null) {
        final token = Token.fromJson(convert.jsonDecode(stringFromJsonData));
        final now = DateTime.now();
        final diffExpiresIn = DateTime.parse(token.expiresAt).difference(now);
        return diffExpiresIn.inSeconds > 0 ? true : false;
      } else {
        throw Exception();
      }
    } on Exception {
      return false;
    }
  }
}
