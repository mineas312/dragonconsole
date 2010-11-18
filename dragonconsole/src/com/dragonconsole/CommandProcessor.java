/*
 * Copyright (c) 2010 Brandon E Buck
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

package com.dragonconsole;

/**
 *
 * @author Brandon E Buck
 */
public class CommandProcessor {
    private DragonConsole console;

    public CommandProcessor() {
        console = null;
    }

    /** Sets the DragonConsole object that output will be sent to.
     * @param console The console where the output will be sent to.
     */
    public void install(DragonConsole console) {
        this.console = console;
    }
    
    /** Sets the console object to null. Only called from DragonConsole.
     * Used to remove the ties to the DragonConsole if the CommandProcessor is
     * changed.
     */
    public void uninstall() {
        this.console = null;
    }

    /**
     * Processes a String entered via a DragonConsole for commands and handles
     * and special cases that need to be taken care of based on the command.
     * This method should just send the string to print if no valid command is
     * entered.
     * @param command The String that might possibly contain a command.
     */
    public void processCommand(String inputToProcess) {
        output("\n&obINPUT: &pb" + inputToProcess + "\n&ob>> ");
    }

    /** Outputs the text sent by sending it to the console.
     * ** USE <code>output(String)</code> instead **
     * @param output The String to be sent to the console for output.
     */
    @Deprecated
    public void outputToConsole(String output) {
        if (console != null)
            console.append(output);
    }

    /** Sends the text to the Console to be processed by the console.
     * Sends the text to the console for Color Code/Script processing.
     * @param output The text for the console to output.
     */
    public void output(String output) {
        if (console!= null)
            console.append(output);
    }
}
