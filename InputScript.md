# Introduction #

InputScript is a very useful tool for any DragonConsole program that will allow the programmer to control how/where input is and looks with simple text controls that are sent along with output to the Console for printing.

## How to Use InputScript ##

What does InputScript look like?

`%i#+;`

That seems simple enough, now I will explain what each portion of the script does.

The `%` (in order to print "%" in the console you must double it just as the `colorCodeChar` like so "%%") is the character used to denote that DCScript will follow, the `i` tells the DragonConsole that this portion of script is InputScript. The next character I will explain is the `;` at the end which acts as a "script terminator" character and is required. The shortest possible piece of InputScript is "`%i;`" as the `#` and `+` are both optional attributes you can give the Input.

Now, the "`#`" is a valid positive integer that specified the range of characters for this piece of input. If you wanted to give the user 20 characters for input then you would specify "20" in the script, "`%i20;`", which limits the player to 20 characters of input. If no number is given the Console assumes that the user can enter as many characters as they wish. It's important to note, when creating ranged sections of Input that InputScript that gives a range will automatically generate the proper number of blank spaces. This means that if you wanted to create a "box" for the user to type in that was 10 characters wide instead of giving 10 characters with the InputScript you would simply use "`|%i10;|`" which will create 10 blank spaces inside the "|" and "|" characters as well as set the cursor just past the first "|" (where the "%" is in the InputScript.

The final optional character in the InputScript is "+" which tells the Console whether or not to protect this set of input. A "+" means that the input should be protected (displays "`*`" instead of the actual character entered), a "-" means that the input should not be protected, and if no character is present then the Console assumes the input should not be protected.