/*
 * Copyright (c) 2010 3l33t Software Developers, L.L.C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.eleet.dragonconsole.*;
import com.eleet.dragonconsole.file.FileProcessor;

/**
 * This class is used when the .jar for DragonConsole is run by itself and
 * functions as a Demonstration of what DragonConsole is and what it is capable
 * of doing. This class is an extension of CommandProcessor and is used to
 * process basic user input.<br /><br />
 * <b>Usable Commands</b><br />
 * info (colors|ANSI|input) - Displays the corresponding file<br />
 * license (dragonconsole|font) - Displays the license for the corresponding
 * argument.<br />
 * exit - Exits the program.
 * @author Brandon E Buck
 */
public class DemoProcessor extends CommandProcessor {
    public DemoProcessor() {
        super();
    }

    @Override
    public void processCommand(String input) {
        input = input.toLowerCase();
        String cmd[] = input.split(" ");

        if (cmd.length == 0)
            outputSystem("\n\nYou must enter a command! Valdid commands are INFO, LICENSE, and EXIT");

        else if (cmd[0].equals("info")) {
            if (cmd.length > 1) {
                try {
                    if (cmd[1].equals("colors"))
                        output("\n\n" + FileProcessor.readDCResource("colors"));
                    else if (cmd[1].equals("ansi"))
                        output("\n\n" + FileProcessor.readDCResource("ansi"));
                    else if (cmd[1].equals("input"))
                        output("\n\n" + FileProcessor.readDCResource("input"));
                    else
                        outputSystem("\n\nNot a valid argument to INFO, type \"INFO\" for help.");

                } catch (Exception exc) {
                    outputError("\n\n" + exc.getMessage());
                }
            } else
                outputSystem("\n\nValid arguments for INFO are: COLORS, ANSI, or INPUT.");

        } else if (cmd[0].equals("license")) {
            if (cmd.length > 1) {
                try {
                    if (cmd[1].equals("font"))
                        output("\n\n" + FileProcessor.readDCResource("l_font"));
                    else if (cmd[1].equals("dragonconsole"))
                        output("\n\n" + FileProcessor.readDCResource("l_console"));
                    else
                        outputSystem("\n\nNot a valid argument for LICENSE, type \"LICENSE\" for help.");

                } catch (Exception exc) {
                    outputError("\n\n" + exc.getMessage());
                }
            } else
                outputSystem("\n\nValid arguments for LICENSE are FONT and DRAGONCONSOLE.");
        } else if (cmd[0].equals("exit"))
            System.exit(0);
        else
            outputSystem("\n\nYou must enter a command! Valid commands are INFO, LICENSE, and EXIT");

        output("\n\n&ob>>&00 ");
    }
}
