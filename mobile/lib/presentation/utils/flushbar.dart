import 'package:another_flushbar/flushbar.dart';
import 'package:flutter/material.dart';

Flushbar getSuccessFlushbar(BuildContext context, String content,
    {Duration? duration}) {
  return Flushbar(
    messageText: Text(
      content,
      style: Theme.of(context)
          .textTheme
          .bodyText1
          ?.copyWith(fontWeight: FontWeight.w500, color: Colors.white),
    ),
    backgroundColor: Colors.green,
    duration: duration ?? Duration(seconds: 3),
    icon: Icon(Icons.check_circle, color: Colors.white),
  );
}

Flushbar getErrorFlushbar(BuildContext context, String content,
    {Duration? duration}) {
  return Flushbar(
    messageText: Text(
      content,
      style: Theme.of(context)
          .textTheme
          .bodyText1
          ?.copyWith(fontWeight: FontWeight.w500, color: Colors.white),
    ),
    backgroundColor: Theme.of(context).errorColor,
    duration: duration ?? Duration(seconds: 3),
    icon: Icon(Icons.error, color: Colors.white),
  );
}
