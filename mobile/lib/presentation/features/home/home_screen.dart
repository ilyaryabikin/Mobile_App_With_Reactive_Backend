import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_simple_app/constants.dart';
import 'package:flutter_simple_app/presentation/features/auth/bloc/auth_bloc.dart';
import 'package:flutter_simple_app/presentation/features/home/bloc/home_bloc.dart';
import 'package:flutter_simple_app/presentation/features/home/custom_list_tile.dart';
import 'package:flutter_simple_app/presentation/utils/flushbar.dart';
import 'package:flutter_simple_app/presentation/utils/phone_number_mask.dart';
import 'package:flutter_simple_app/repositories/user_repository.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late final HomeBloc _homeBloc;

  late final TextEditingController _nameController;
  late final TextEditingController _surnameController;
  late final TextEditingController _phoneNumberController;
  late final TextEditingController _cityController;

  late final FocusNode _nameFocus;
  late final FocusNode _surnameFocus;
  late final FocusNode _phoneNumberFocus;
  late final FocusNode _cityFocus;

  @override
  void initState() {
    _homeBloc = HomeBloc(
      BlocProvider.of<AuthBloc>(context),
      RepositoryProvider.of<UserRepository>(context),
    );
    _nameController = TextEditingController();
    _surnameController = TextEditingController();
    _phoneNumberController = TextEditingController();
    _cityController = TextEditingController();

    _nameFocus = FocusNode();
    _surnameFocus = FocusNode();
    _phoneNumberFocus = FocusNode();
    _cityFocus = FocusNode();
    super.initState();
  }

  @override
  void dispose() {
    _homeBloc.close();
    _nameController.dispose();
    _surnameController.dispose();
    _phoneNumberController.dispose();
    _cityController.dispose();
    _nameFocus.dispose();
    _surnameFocus.dispose();
    _phoneNumberFocus.dispose();
    _cityFocus.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          actions: [
            IconButton(
              onPressed: () => _homeBloc.add(LogoutEvent()),
              icon: Icon(
                Icons.exit_to_app,
              ),
            )
          ],
        ),
        body: BlocBuilder<AuthBloc, AuthState>(
          builder: (context, authState) {
            final user = authState.user;
            return BlocConsumer<HomeBloc, HomeState>(
              bloc: _homeBloc,
              listener: (context, homeState) {
                if (homeState is FailureState) {
                  getErrorFlushbar(
                    context,
                    homeState.message,
                  )..show(context);
                } else if (homeState is SuccessState) {
                  getSuccessFlushbar(
                      context, 'Профиль был успешно отредактирован')
                    ..show(context);
                }
              },
              builder: (context, homeState) {
                if (homeState is ReadOnlyState) {
                  _nameController.text = authState.user!.name ?? '';
                  _surnameController.text = authState.user!.surname ?? '';
                  _phoneNumberController.text = authState.user!.phoneNumber !=
                          null
                      ? phoneNumberMask
                          .maskText(authState.user!.phoneNumber!.substring(1))
                      : '';
                  _cityController.text = authState.user!.city ?? '';
                }
                return Stack(
                  children: [
                    SingleChildScrollView(
                      child: Column(
                        children: [
                          ListTile(
                            title: Text('Email: ', style: textStyleForTitle),
                            subtitle: Text(
                              user!.username,
                              style: textStyleForReadOnlyText,
                            ),
                          ),
                          CustomListTile(
                            title: 'Имя',
                            controller: _nameController,
                            focusNode: _nameFocus,
                            readOnly: homeState.isReadOnly,
                          ),
                          CustomListTile(
                            title: 'Фамилия',
                            controller: _surnameController,
                            focusNode: _surnameFocus,
                            readOnly: homeState.isReadOnly,
                          ),
                          CustomListTile(
                            title: 'Телефон',
                            controller: _phoneNumberController,
                            focusNode: _phoneNumberFocus,
                            readOnly: homeState.isReadOnly,
                            inputType: TextInputType.phone,
                            formatter: phoneNumberMask,
                          ),
                          CustomListTile(
                            title: 'Город',
                            controller: _cityController,
                            focusNode: _cityFocus,
                            readOnly: homeState.isReadOnly,
                          ),
                          SizedBox(
                            height: 200,
                          ),
                        ],
                      ),
                    ),
                    Column(
                      mainAxisAlignment: MainAxisAlignment.end,
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: homeState.isReadOnly
                          ? [
                              Padding(
                                padding: const EdgeInsets.all(6),
                                child: ElevatedButton(
                                    onPressed: () =>
                                        _homeBloc.add(SwitchToEditableMode()),
                                    child: Text('Редактировать данные')),
                              ),
                            ]
                          : [
                              Padding(
                                padding:
                                    const EdgeInsets.only(left: 6, right: 6),
                                child: ElevatedButton(
                                  onPressed: () =>
                                      _homeBloc.add(DiscardChanges()),
                                  style: ButtonStyle(
                                      backgroundColor:
                                          MaterialStateProperty.all(
                                              Colors.red)),
                                  child: Text('Отменить изменения'),
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(6),
                                child: ElevatedButton(
                                  onPressed: () => _homeBloc.add(ApplyChanges(
                                    name: _nameController.text,
                                    surname: _surnameController.text,
                                    phoneNumber: _phoneNumberController
                                            .text.isEmpty
                                        ? null
                                        : _phoneNumberController.text
                                            .replaceFirst('+7', '8')
                                            .replaceAll(
                                                RegExp('[\\(\\)\\s\\-]'), ''),
                                    city: _cityController.text,
                                  )),
                                  child: Text('Сохранить'),
                                ),
                              ),
                            ],
                    ),
                  ],
                );
              },
            );
          },
        ),
      ),
    );
  }
}
