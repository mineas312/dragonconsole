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

package com.eleet.dragonconsole.util;

import javax.swing.JTextPane;
import javax.swing.text.*;
import java.awt.Toolkit;

/** Controls Input, more Documentation later.
 * @author Brandon E Buck
 * @version 1.4
 */
public class InputController extends DocumentFilter {
    private final String BYPASS = "<DCb />-";
    private int rangeStart;
    private int rangeEnd;
    private boolean protect;
    private InputString input;
    private String protectedChar = "*";
    private boolean isReceivingInput;
    private AttributeSet inputAttr;
    private JTextPane console;
    private boolean bypassRemove = false;
    private boolean ignoreInput = false;
    private StoredInput stored = null;
    private boolean consoleInputMethod = true; // true for inline input and false for separate input

    /** Default constructor
     * rangeStart - The beginning of the input range, will always contain a
     *    value
     * rangeEnd - The end of the input range, -1 if the range has no limit
     *    or the max value of the input range
     * protect - true or false, whether or not the input needs to be
     *    protected
     * protectedText - if the input needs to be protected the actual input
     *    will be stored here.
     */
    public InputController(AttributeSet attr) {
        super();
        rangeStart = 0;
        rangeEnd = 0;
        protect = false;
        input = new InputString("");
        isReceivingInput = false;
        inputAttr = attr;
        console = null;
    }

    /** Changes the <code>inputAttr</code> for the input controller.
     * Changes the default <code>AttributeSet</code> for this
     * <code>InputController</code> that is used to style all input.
     * @param newInputAttr The new <code>AttributeSet</code> to use
     */
    public void setInputAttributeSet(AttributeSet newInputAttr) {
        inputAttr = newInputAttr;
    }

    public void installConsole(JTextPane jtp) {
        console = jtp;
    }

    public boolean isProtected() {
        return protect;
    }

    public String getBypassPrefix() {
        return BYPASS;
    }

    public void reset() {
        rangeStart = 0;
        rangeEnd = 0;
        protect = false;
        input = new InputString("");
        isReceivingInput = false;
    }

    public void setConsoleInputMethod(boolean consoleInputMethod) {
        this.consoleInputMethod = consoleInputMethod;
    }

    public void setIgnoreInput(boolean ignoreInput) {
        this.ignoreInput = ignoreInput;
    }

    public void setInput(String newInput) {
        if (isReceivingInput && isInfiniteInput()) {
            StyledDocument doc = console.getStyledDocument();
            try {
                int length = doc.getLength() - rangeStart;
                bypassRemove = true;
                doc.remove(rangeStart, length);

                String prefix = "";
                if (consoleInputMethod) // True if inline
                    prefix = BYPASS;

                if (protect)
                    doc.insertString(rangeStart, prefix + getProtectedString(newInput.length()), inputAttr);
                else
                    doc.insertString(rangeStart, prefix + newInput, inputAttr);

                input = new InputString("");
                input.append(newInput);
            } catch (Exception exc) { }
        }
    }

    public void setRangeStart(int newRangeStart) {
        if (isReceivingInput) {
            rangeStart = newRangeStart;
            if (rangeEnd > 0)
                rangeEnd += newRangeStart;
        }
    }

    /** Passed an input string, breaking it apart and setting the input controller to fit the input constraints.
     * Passed an input String from the console and breaks it down and sets
     * up the input controller according the input string passed to it.
     * The input strings format is:
     *      %i#[+|-|];
     *
     * Accepted minimum is "%i;" which means input from the position of %
     * forward.
     *
     * The # can be any number (int) that specifies the amount of characters
     * that fit in this input range.
     *
     * The "[+|-|]" means that either a "+", "-", or blank space. "-" and
     * "" both mean the same thing, unprotected input, if a "+" is present
     * that means the input is protected.
     *
     * If there is an error, or the minimum is passed ("%i;") then input is
     * treated as "UNLIMITED"
     *
     * <strong>IMPORTANT: The Start position MUST be specified after the
     * input style is set. This was added in to give the console more
     * control as to where the cursor needs to default for input control and
     * make input ranges MUCH more accurate.</strong>
     * @param newInputStyle String containing the new input style for the input controller.
     * @return Returns true if all text after the input command should be ignored (if the input has an unlimited Range);
     */
    public boolean setInputStyle(String newInputStyle) {
        rangeStart = -1;
        rangeEnd = 0;
        protect = false;
        input = new InputString("");
        isReceivingInput = true;

        if (newInputStyle.equals("%i;")) {
            rangeEnd = -1;
            return false;
        } else {
            String inputStyle = newInputStyle.substring(2); // Chop off the "%i"
            inputStyle = inputStyle.substring(0, inputStyle.length() - 1); // Chop off the ";"

            if (inputStyle.length() > 0) {
                if (inputStyle.charAt(inputStyle.length() - 1) == '+' || inputStyle.charAt(inputStyle.length() - 1) == '-') {
                    char tempProtect = inputStyle.charAt(inputStyle.length() - 1);
                    inputStyle = inputStyle.substring(0, inputStyle.length() - 1);

                    if (tempProtect == '+')
                        protect = true;
                }

                if (inputStyle.length() > 0) {
                    rangeEnd = Integer.parseInt(inputStyle);

                    input.set(getInputRangeString());

                    return true;
                } else {
                    rangeEnd = -1;

                    return false;
                }

            } else {
                rangeEnd = -1;
                return false;
            }
        }
    }

    /** The location that the input starts, used for setting the Caret position.
     * The location the input range starts, used for programmatically
     * setting the outputPane's Caret position.
     * @return The start position of the input, if not currently receiving input then -1
     */
    public int getInputRangeStart() {
        if (isReceivingInput)
            return rangeStart;
        else
            return -1;
    }

    public void clearText() {
        reset();
        bypassRemove = true;
        StyledDocument doc = console.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch(Exception exc) { }
    }

    public int getInputRangeEnd() {
        return rangeEnd;
    }

    /** Tests to see if input is controlled by a range or infinite.
     * Returns true if this input has no maximum range, or false if the
     * input is limited by a range.
     * @return true if input has no limited range, or false if it does.
     */
    public boolean isInfiniteInput() {
        return (rangeEnd == -1);
    }

    /** Sets the basic input which is unlimited input from position <code>startPosition</code>.
     * Sets input to it's basic level which is an unlimited number of
     * characters after the startPosition which is the equivalent of calling
     * <code>setInputStyle("%i;");</code>. This method is called from
     * the append method if no input script was detected in the output. (By
     * default, all output MUST have input).
     * @param startPosition The last position of output, and the start of the input.
     */
    public void setBasicInput(int startPosition) {
        rangeStart = startPosition;
        rangeEnd = -1;
        protect = false;
        input = new InputString("");
        isReceivingInput = true;
    }

    public String getInputRangeString() {
        if (isReceivingInput && rangeEnd > 0) {
            String inputRangeString = "";
            int counter = 0;
            if (rangeStart >= 0)
                counter = rangeStart;

            for (int x = counter; x < rangeEnd; x++)
                inputRangeString += " ";

            return inputRangeString;
        } else
            return "";
    }

    /** Sets a custom protected character if '*' is not desired.
     * @param protectedChar The new protected char.
     */
    public void setProtectedChar(char protectedChar) {
        this.protectedChar = protectedChar + "";
    }

    /** Used to determine if input is actively being received.
     * This method is used to determine if the InputController has input
     * controls in place for current input. This is used to help prevent
     * tampering with output while
     * @return
     */
    public boolean isReceivingInput() {
        return isReceivingInput;
    }

    public String getInput() {
        isReceivingInput = false;
        return input.get().trim();
    }

    private String getProtectedString(int length) {
        String pString = "";
        for (int i = 0; i < length; i++)
            pString += protectedChar;

        return pString;
    }

    private String restoreProtectedString(String string) {
        String returnString = "";

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ')
                returnString += protectedChar;
            else
                returnString += " ";
        }

        return returnString;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string.startsWith(BYPASS)) {
            string = string.substring(BYPASS.length());
            fb.insertString(offset, string, attr);
        } else if (isReceivingInput && isInfiniteInput() && offset >= rangeStart) {
            if (protect)
                fb.insertString(offset, getProtectedString(string.length()), inputAttr);
            else
                fb.insertString(offset, string, inputAttr);

            input.insert(offset - rangeStart, string);
        } else
            Toolkit.getDefaultToolkit().beep();
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
        if (string.startsWith(BYPASS)) {
            String newString = string.substring(BYPASS.length());
            if (protect)
                newString = restoreProtectedString(newString);
            
            fb.replace(offset, length, newString, attr);
            
        } else {
            if (!ignoreInput) {
                if (isReceivingInput && rangeStart > 0) {
                    if (offset >= rangeStart) {
                        if (!isInfiniteInput() && (offset + 1) <= rangeEnd) {
                            boolean inserted = input.rangeInsert((offset - rangeStart), string);

                            if (inserted) {
                                if (protect)
                                    fb.replace(offset, length, protectedChar, inputAttr);
                                else
                                    fb.replace(offset, length, string, inputAttr);

                                if (input.endIsEmpty())
                                    fb.remove(rangeEnd - 1, 1);
                                else
                                    fb.remove(rangeEnd, 1);
                            } else
                                Toolkit.getDefaultToolkit().beep();



                        } else if (isInfiniteInput()) {
                            if (protect)
                                fb.replace(offset, length, protectedChar, inputAttr);
                            else
                                fb.replace(offset, length, string, inputAttr);

                            input.replace((offset - rangeStart), length, string);
                        } else
                            Toolkit.getDefaultToolkit().beep();
                    } else
                        Toolkit.getDefaultToolkit().beep();
                } else
                    Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if (!ignoreInput) {
            if (!bypassRemove) {
                if (isReceivingInput && rangeStart > 0) {
                    if (!(offset < rangeStart)) {
                        if (!isInfiniteInput()) {
                            fb.remove(offset, length);
                            fb.insertString((rangeEnd - 1), " ", inputAttr);

                            if (console.getCaretPosition() == rangeEnd)
                                console.setCaretPosition(rangeEnd - 1);
                        } else
                            fb.remove(offset, length);


                        if (!isInfiniteInput())
                            input.rangeRemove((offset - rangeStart), length);
                        else
                            input.remove((offset - rangeStart), length);
                    } else
                        Toolkit.getDefaultToolkit().beep();
                } else
                    Toolkit.getDefaultToolkit().beep();
            } else {
                bypassRemove = false;
                fb.remove(offset, length);
            }
        }
    }
    
    public boolean hasStoredInput() {
        return (stored != null);
    }
    
    public void storeInput() {
        stored = new StoredInput(isInfiniteInput(), protect, (rangeEnd - rangeStart), input);
        reset();
    }
    
    public boolean restoreInput() {
        if (stored != null && isReceivingInput) {
            if (stored.matches(isInfiniteInput(), protect, (rangeEnd - rangeStart))) {
                input = stored.getInput();
                
                int end;
                if (isInfiniteInput())
                    end = 0;
                else
                    end = rangeEnd - rangeStart;
                try {
                    if (consoleInputMethod)
                        ((AbstractDocument)console.getStyledDocument()).replace(rangeStart, end, BYPASS + input.get(), inputAttr);
                    else
                        ((AbstractDocument)console.getStyledDocument()).replace(rangeStart, end, input.get(), inputAttr);
                
                } catch (Exception exc) { }

                stored = null;

                return true;
            }
        }

        stored = null;

        return false;
    }

    private class StoredInput {
        private boolean isInfinite;
        private boolean protect;
        private int range;
        private InputString input;

        public StoredInput(boolean isInfinite, boolean protect, int range, InputString input) {
            this.isInfinite = isInfinite;
            this.protect = protect;
            this.range = range;
            this.input = input;
        }

        public boolean matches(boolean isInfinite, boolean protect, int range) {
            if (this.isInfinite == isInfinite) {
                if (!this.isInfinite) {
                    if (this.range == range && this.protect == protect)
                        return true;
                } else {
                    if (this.protect == protect)
                        return true;
                    else
                        return false;
                }
            }

            return false;
        }

        public InputString getInput() {
            return input;
        }
    }
}
