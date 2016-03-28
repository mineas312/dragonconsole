# Introduction #

DragonConsole Color Codes (DCCC) are short, three character codes that the programmer can add to any text sent to the `append(String)` method of `DragonConsole` that give control over the color the text displays with. These codes allow for a Rich Text console application and give the programmer the same control over text set to to the DragonConsole as ANSI gives for common terminals.

## DCCC Format ##

The Format for DCCCs is very simple, and it looks like "&FB" where "&" is the `colorCodeChar` of the `DragonConsole`, which be default is "&," and "F" is the character that represents the Foreground Color of the text and "B" represents the Background color for the text.

DCCCs are processed and handled the same way ANSI Colors are handled, meaning that once a DCCC Style is set, it remains as the "current" style until changed or until the style is reset. It is important to not this and remember to reset your style when you are doing use it to prevent any unwanted "color overflow."

There are quite a few built in Colors by default, and more can be added easily (explained later). The Built in colors are as follows:

| **Name** | **Character** | **Actual Color** | **ANSI Equivalent** | **ANSI Class Variable`*`**|
|:---------|:--------------|:-----------------|:--------------------|:--------------------------|
| Red      | r             | `Color.RED`      | 1;31                | `ANSI.INTENSE_RED`        |
| Dark Red | R             | `Color.RED.darker()` | 31 or 41            | `ANSI.RED`                |
| Green    | g             | `Color.GREEN`    | 1;32                | `ANSI.INTENSE_GREEN`      |
| Dark Green | G             | `Color.GREEN.darker()` | 32 or 42            | `ANSI.GREEN`              |
| Yellow   | y             | `Color.YELLOW`   | 1;33                | `ANSI.INTENSE_YELLOW`     |
| Dark Yellow | Y             | `Color.YELLOW.darker()` | 33 or 43            |`ANSI.YELLOW`              |
| Blue     | l             | `new Color(66, 66, 255)` | 1;34                | `ANSI.INTENSE_BLUE`       |
| Dark Blue | L             | `new Color(66, 66, 255).darker()` | 34 or 44            |`ANSI.BLUE`                |
| Magenta  | m             | `Color.MAGENTA`  | 1;35                | `ANSI.INTENSE_MAGENTA`    |
| Dark Magenta | M             | `Color.MAGENTA.darker()` | 35 or 45            | `ANSI.MAGENTA`            |
| Cyan     | c             | `Color.CYAN`     | 1;36                | `ANSI.INTENSE_CYAN`       |
| Dark Cyan | C             | `Color.CYAN.darker()` | 36 or 46            |`ANSI.CYAN`                |
| Gray     | x             | `Color.GRAY.brighter()` |  37 or 47           | `ANSI.WHITE`              |
| Dark Gray | X             | `Color.GRAY`     | 1;30                | `ANSI.INTENSE_BLACK`      |
| Black    | b             | `Color.BLACK`    | 30 or 40            |`ANSI.BLACK`               |
| White    | w             | `Color.WHITE`    | 1;37                | `ANSI.INTENSE_WHITE`      |
| Orange   | o             | `Color.ORANGE`   | -                   | -                         |
| Dark Orange | O             | `Color.ORANGE.darker()` | -                   | -                         |
| Purple   | p             | `new Color(128, 0, 255)` | -                   | -                         |
| Dark Purple | P             | `new Color(128, 0, 255).darker()` | -                   | -                         |
| Gold     | d             | `new Color(241, 234, 139)` | -                   | -                         |
| Dark Gold | D             | `new Color(241, 234, 139).darker()` | -                   | -                         |

**`*`** If you remove all the default colors from the `DragonConsole` but would like to maintain ANSI compatibility (convert between your DCCCs and ANSI codes) you **MUST** add `TextColor`s to the `DragonConsole` with these `ANSI` Color variables. ANSI Codes are matched to DCCCs by Color, not label, to allow for a complete set of user defined DCCCs that can still convert into ANSI codes.

So when building a true DCCC you would choose the Character representing the color of your foreground, and background from the list above. For RED text on a BLACK background you would chose "r" for RED and "b" for BLACK and your DCCC would be "&rb" which you could add to a String like `"&rbHello, World"` and `append()` it. With this example, "Hello, World" would print RED with a BLACK background (and in a black console, a BLACK background would appear as the background of the console).

There are two "reserved" characters for DCCCs that have a different functionality other than representing a color and those are "0" (zero) and "-" hyphen.

The "0" (zero) is used as "&00" and it resets the "current" style to whatever the default style is for the `DragonConsole`. It's wise to tag "&00" at the end of any bulk output to prevent "color overflow" to the next set of output you print unless you wish to continue using the current style.

The "-" (hyphen) is used as a "carry over" character and by that I mean that whichever position you place it in in the DCCC it will carry over the color from the current style. So if the current style is "&rb" (RED text on BLACK background) and I just want to change the foreground color to GRAY ("x") I could use the DCCC "&x-" which would change the foreground from RED ("r") to GRAY ("x") and carry the Background over from the current style. A side note here, the "-" character is valid for both positions (FOREGROUND and BACKGROUND) but using the DCCC "&--" is the same as not using a DCCC at all because it carries the current style over as the new current style.