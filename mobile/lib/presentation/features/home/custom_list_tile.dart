import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_simple_app/constants.dart';

class CustomListTile extends StatelessWidget {
  final bool readOnly;
  final TextEditingController controller;
  final FocusNode focusNode;
  final String title;
  final TextInputType? inputType;
  final TextInputFormatter? formatter;

  const CustomListTile({
    Key? key,
    required this.readOnly,
    required this.controller,
    required this.focusNode,
    required this.title,
    this.inputType,
    this.formatter,
  }) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text('$title: ', style: textStyleForTitle),
      subtitle: Container(
        height: 30,
        decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(6), color: Colors.black12),
        child: EditableText(
          controller: controller,
          focusNode: focusNode,
          readOnly: readOnly,
          style: readOnly ? textStyleForReadOnlyText : textStyleForEditableText,
          cursorColor: Colors.black,
          backgroundCursorColor: Colors.grey,
          inputFormatters: formatter != null ? [formatter!] : null,
          keyboardType: inputType ?? null,
        ),
      ),
    );
  }
}
