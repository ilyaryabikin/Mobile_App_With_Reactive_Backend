class Token {
  final String id;
  final String accessToken;
  final String expiresAt;

  Token(
    this.id,
    this.accessToken,
    this.expiresAt,
  );

  factory Token.fromJson(Map<String, dynamic> json) {
    var accessToken = json['token'];
    var expiresAt = json['expiresAt'];
    var id = json['id'];

    return Token(
      id,
      accessToken,
      expiresAt,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'token': accessToken,
        'expiresAt': expiresAt,
      };
}
