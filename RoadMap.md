# DragonConsole Road Map #

This list of planned features is subject to change without notice, and possibly often. None of the features listed below are planned for any specific version release.

  * Creating a Text Windowing Toolkit (TWT) that will function (to some degree) like NCurses and allow the programmer to simply and easily build a Text Window much like building any other Java GUI and add labels, buttons, actions, and so forth to these text windows.
  * A full featured DCScript Scripting language for on demand processing that may change often (which would prevent the programmer from constantly releasing new versions). This Scripting language would be a loosely typed Scripting language that supported loops and conditional statements and allow for easy access to the DragonConsole it's processed in.
  * Full ANSI support to further allow the DragonConsole to be implemented as a true Terminal Emulator on any system able to "interface" with older applications that use ANSI codes or newer applications where ANSI was preferred.
  * A Propietary DragonConsole Standard that mimics the controls ANSI gives over terminals. The idea here is for DragonConsole defaults to be easier to implement or provide the programmer more control than it's ANSI counterpart.
  * (UNSURE) I need to research this but I'd like to give DragonConsole it's on Input/OutputStream classes so that Objects that send things via these sources would be able to pull an InputStream or OutputStream from the DragonConsole for use.
  * (UNSURE) If I can implement an Input/OutputStream for DragonConsole I'd like to then implement a Socket that used the DragonConsole as it's source of Input/Output via the custom Streams.