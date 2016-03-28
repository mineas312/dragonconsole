# Introduction #

I will continue adding ANSI support in future releases of DragonConsole as my ultimate plan is for DragonConsole to be compatible with all ANSI codes.

As of version 3.0 the only ANSI Codes that are supported are the ANSI Color Codes. This support has to be explicitly enabled for it to work.

## ANSI Color Codes ##

It's important to note that whether ANSI Color Support is enabled or not ANSI Color Codes will be removed from the String output to the Console and will not be displayed. If ANSI Color Support is enabled, then DCCCs will not be used (but will continue to be processed out of the String that are displayed to the user).

To Enable ANSI Color support all you have to do is call `setUseANSIColorCodes(true);` of the `DragonConsole` and viola, ANSI colors will be processed and the Styles will be present on any output sent to the Console.

To Disable them you call the same method and pass `false` instead of `true`.

To allow for ANSI support without disabling DCCCs or vice versa there are two helper methods within the `DragonConsole` class that allow for conversions between the two code types.

`convertToANSIColors(String)` will convert all DCCCs in the String to the equivalent ANSI code, if no equivalent ANSI code exists for the color then it uses the default text color (39) or default background color (49).

`convertToDCColors(String)` will convert all ANSI Codes in the String to the equivalent DCCC, if no equivalent DCCC exists then it use the default style set in the console.