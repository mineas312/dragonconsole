# Purpose #

Every piece of software is built to solve a problem of some kind or make processes easier. DragonConsole is no different. DragonConsole is designed to be used in any number of possible programs as a text-based software-user communication tool. Some programs have a hidden "debugger" console or master console and other programs run entirely from a console. DragonConsole aims to supplement these needs with a Cross Platform Console Simulator that will support colored output, user input, programmer defined input processing, and operate exactly the same regardless of what operating system your software runs on.

**Currently the Console is only tested in Windows 7, but upon official release of the non-Beta 3.0.0 compatibility with OS X 10.6 and Linux (as well as an older version of Windows) will be tested.**

# History #

DragonConsole started off as a console interface that I planned to use for a Text based game I wanted to build several years ago. When time became a major issue in the possibility of working on the game I decided to rework the DragonConsole to be an implementable Console Interface that I could use anywhere needed and eventually decided to open it up to anyone who may have the same needs.

Version 1, when it was released, wasn't completely finished. It still had a lot of defaults left over from when it was intended to be used for a game. It did feature color output (which was the main reason I needed to write my own Console Interface, for Cross Platform color text output in a console like environment) but it used two visibly separate TextComponents. One for Input and the other for Output.

Version 2 didn't change anything as far as the interface or operations are concerned it but it was a massive overhaul to the code. I cleaned up a lot of sloppy code and needlessly repeated code segments as well as fine tuned the way a lot of things worked and added in methods to change certain things (such as the character used to denote a Color Code) and other features that were present in a lot of consoles.

Version 3 will be the largest change and is that largest amount of work. The main drive behind it was to make Input/Output take place in the same TextComponent so that it actually appeared to function as a Console and along with that I decided to create what I call InputScript which is a small tag you add into your output to tell the Console where and how to treat the next portion of Input.

DragonConsole is still designed with the intention of being used as an interface for a text based game but I do my best to make it as implementable and customizable as possible for future uses I may require of it or anything that someone else may need of it.

## Included Files ##
DejaVu Sans Mono produced by Bitstream, Inc. [DejaVu Fonts](http://dejavu-fonts.org/wiki/Main_Page)

DejaVu Sans Mono is Copyright (c) 2003 by Bitstream, Inc. All Rights Reserved. Bitstream Vera is a trademark of Bitstream, Inc.
The Full license will be included in the .zip file as well as inside the .jar