# Ease of Use #

DragonConsole was and is designed with a personal goal and usage plan for it, but along the way I do my very best to make DragonConsole as easy as I can to implement and use outside of my needs. This is not just for any who may be interested in using the software but for myself as well, I love it when things are easy!

## Basic Implementation ##

The package structure of DragonConsole is as follows:

`import com.eleet.dragonconsole.*;`

With sub packages `file`, `font`, `resources`, and `util`.

The simplest way to implement `DragonConsole` is with the `DragonConsoleFrame` class which is a simple `JFrame` with just the `DragonConsole` in it.

```
DragonConsoleFrame dcframe = new DragonConsoleFrame();
dcframe.setVisible(true);
```

This is the easiest way to get `DragonConsole` up and running, however this basic `DragonConsole` does nothing since there is no `CommandProcessor` added to process user input, but this is a simple solution.

```
DragonConsoleFrame dcframe = new DragonConsoleFrame();
dcframe.getConsole().setCommandProcessor(new MyCommandProcessor());
dcframe.setVisible(true);
```

This will allow you to implement your extended `CommandProcessor` so that the input can handled properly for you program.

That concludes the basic implementation of the `DragonConsole`.

## Advanced Implementation ##

There are more complex ways to implement `DragonConsole` and as I said earlier, I do my best to keep it simple and easy and give the programmer everything he or she may need in order to customize it as they wish. One way that I do this is that `DragonConsole` is an extension of a `JPanel` so once created this Console can be added to any interface you may have already created.

There are a few constructors within the `DragonConsole` class that allow to change the basic information that needs to be established before the Console can be initialized.
The data that can be passed is as follows:

```
DragonConsole(width, height, useInlineInput, printDefaultMessage);
```

What each variable means is quite simple.

**width** - **Default:** 800 - Is the width you wish for the console to be (the `setSize()` method of the `JPanel has been overridden to change the Maximum, Minimum, and Preferred sizes for the programmer).

**height** - **Default:** 600 - Is the height you wish for the console to be.

**useInlineInput** - **Default:** `true` - There are two methods of input that you can choose between when creating a DragonConsole. You can have "Inline Input" which is where the input/output are all processed in the same TextComponent. Or you can pass `false` for `useInlineInput` and then all the output is handled in on TextComponent and the Input is handled it's own TextComponent at the bottom of the screen. `**`

**printDefaultMessage** - **Default:** `true` - The DragonConsole is built with a default ASCII name Graphic and my copyright tag to print automatically when the console loads. It's your choice for this default message to print or not.

There are more constructors than the above that take in any variation of the above data so make sure you choose whichever one you feel is the correct choice.

If you still choose to use the `DragonConsoleFrame` class but wish to use a custom console, this is very simple.

```
DragonConsole dc = new DragonConsole(false); // Does not use Inline Input
dc.setCommandProcessor(new MyCommandProcessor());
DragonConsoleFrame dcframe = new DragonConsoleFrame(dc);
dcframe.setVisible(true);
```

This way any Console can be constructed and added to a `DragonConsoleFrame`.

### More Customization ###

The Console also has two built in default color settings, the default setting is a Black background with Gray Text but it also has a "Mac Style" Console setting which is a White background with Black text.

```
console.setDefaultStyle();
```

OR:

```
console.setMacStyle();
```

There are more customization options you can set after you create a console, like adding a new text color to the console with:

```
myConsole.addTextColor('m', Color.MAGENTA);
```

This line will create a new Text Color with the char code 'm' and it will be Magenta. This change will be reflected with all existing colors as well. Each existing color will have a Magenta background, and each existing background color will have a Magenta foreground. Text Colors are explained in more detail on the ColorCodes page.

Other minor settings can be changes, for example, the Character used to denote a color code (default '&') via `setColorCodeChar(char)` or the character that displays for Protected input (Inline Input) only) `setProtectedChar(char)` and other changes.