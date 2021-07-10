import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_simple_app/presentation/features/auth/bloc/auth_bloc.dart';
import 'package:flutter_simple_app/presentation/features/login/bloc/login_bloc.dart';
import 'package:flutter_simple_app/presentation/utils/flushbar.dart';
import 'package:flutter_simple_app/presentation/utils/phone_number_mask.dart';
import 'package:flutter_simple_app/repositories/user_repository.dart';
import 'package:mask_text_input_formatter/mask_text_input_formatter.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  late final LoginBloc _loginBloc;

  final GlobalKey<FormState> _globalKey = GlobalKey<FormState>();

  late final TextEditingController _usernameController;
  late final TextEditingController _passwordController;
  late final TextEditingController _nameController;
  late final TextEditingController _surnameController;
  late final TextEditingController _phoneNumberController;
  late final TextEditingController _cityController;

  @override
  void initState() {
    _loginBloc = LoginBloc(
      RepositoryProvider.of<UserRepository>(context),
      BlocProvider.of<AuthBloc>(context),
    );
    _usernameController = TextEditingController();
    _passwordController = TextEditingController();
    _nameController = TextEditingController();
    _surnameController = TextEditingController();
    _phoneNumberController = TextEditingController();
    _cityController = TextEditingController();
    super.initState();
  }

  @override
  void dispose() {
    _loginBloc.close();
    _usernameController.dispose();
    _passwordController.dispose();
    _nameController.dispose();
    _surnameController.dispose();
    _phoneNumberController.dispose();
    _cityController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: SingleChildScrollView(
          child: BlocConsumer<LoginBloc, LoginState>(
            bloc: _loginBloc,
            listener: (context, state) {
              if (state is LoginSuccess) {
                getSuccessFlushbar(context, state.message,
                    duration: Duration(seconds: 3))
                  ..show(context);
                _usernameController.clear();
                _passwordController.clear();
                _nameController.clear();
                _surnameController.clear();
                _phoneNumberController.clear();
                _cityController.clear();
              } else if (state is LoginFailure) {
                getErrorFlushbar(context, state.error,
                    duration: Duration(seconds: 3))
                  ..show(context);
                _usernameController.clear();
                _passwordController.clear();
              }
            },
            builder: (context, state) {
              if (state is LoginInitial || state is LoginFormState) {
                return _loginForm(isLogin: true);
              } else if (state is SignUpFormState) {
                return _loginForm(isLogin: false);
              } else if (state is LoginLoading) {
                return _loginForm(isLogin: state.isLogin, isLoading: true);
              }
              return _loginForm(isLogin: true);
            },
          ),
        ),
      ),
    );
  }

  Widget _loginForm({
    required bool isLogin,
    bool isLoading = false,
  }) {
    return isLoading
        ? Container(
            margin: EdgeInsets.only(top: 200),
            child: Center(
              child: CircularProgressIndicator(),
            ),
          )
        : Container(
            margin: const EdgeInsets.all(50),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(
                  height: isLogin ? 150 : 40,
                ),
                Text(
                  isLogin ? 'Вход' : 'Регистрация',
                  style: TextStyle(
                    fontSize: 30,
                    fontWeight: FontWeight.bold,
                  ),
                  textAlign: TextAlign.center,
                ),
                SizedBox(
                  height: 50,
                ),
                Form(
                  key: _globalKey,
                  child: Column(
                    children: [
                      TextFormField(
                        controller: _usernameController,
                        decoration: InputDecoration(labelText: 'Email'),
                        keyboardType: TextInputType.emailAddress,
                        validator: (value) {
                          if (value == null || value == '') {
                            return 'Введите email';
                          } else if (!EmailValidator.validate(value)) {
                            return 'Введите корректный email адрес';
                          }
                        },
                      ),
                      TextFormField(
                        controller: _passwordController,
                        decoration: InputDecoration(labelText: 'Пароль'),
                        obscureText: true,
                        validator: (value) => value == null || value == ''
                            ? 'Введите пароль'
                            : (value.length < 8 || value.length > 20)
                                ? 'Пароль не должен быть меньше 8 и больше 20 символов'
                                : null,
                      ),
                      if (!isLogin) ...[
                        TextFormField(
                          controller: _nameController,
                          decoration: InputDecoration(labelText: 'Имя'),
                        ),
                        TextFormField(
                          controller: _surnameController,
                          decoration: InputDecoration(labelText: 'Фамилия'),
                        ),
                        TextFormField(
                          controller: _phoneNumberController,
                          decoration: InputDecoration(labelText: 'Телефон'),
                          keyboardType: TextInputType.phone,
                          inputFormatters: [
                            phoneNumberMask,
                          ],
                        ),
                        TextFormField(
                          controller: _cityController,
                          decoration: InputDecoration(labelText: 'Город'),
                        ),
                      ]
                    ],
                  ),
                ),
                SizedBox(
                  height: 50,
                ),
                ElevatedButton(
                  onPressed: () {
                    if (_globalKey.currentState!.validate()) {
                      isLogin
                          ? _loginBloc.add(LoginButtonPressed(
                              username: _usernameController.text,
                              password: _passwordController.text))
                          : _loginBloc.add(SignUpButtonPressed(
                              username: _usernameController.text,
                              password: _passwordController.text,
                              name: _nameController.text.isEmpty
                                  ? null
                                  : _nameController.text,
                              surname: _surnameController.text.isEmpty
                                  ? null
                                  : _surnameController.text,
                              phoneNumber: _phoneNumberController.text.isEmpty
                                  ? null
                                  : _phoneNumberController.text
                                      .replaceFirst('+7', '8')
                                      .replaceAll('[\\(\\)\\s\\-]', ''),
                              city: _cityController.text.isEmpty
                                  ? null
                                  : _cityController.text,
                            ));
                    }
                  },
                  child: isLogin
                      ? Text(
                          'Войти',
                          style: TextStyle(fontSize: 15),
                        )
                      : Text(
                          'Создать аккаунт',
                          style: TextStyle(fontSize: 15),
                        ),
                ),
                TextButton(
                  onPressed: () => _loginBloc
                      .add(isLogin ? ShowSignUpForm() : ShowLoginForm()),
                  child: isLogin ? Text('Зарегистрироваться') : Text('Войти'),
                ),
              ],
            ),
          );
  }
}
