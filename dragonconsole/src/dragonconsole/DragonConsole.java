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

package dragonconsole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.*;
import java.util.ArrayList;
import javax.swing.event.DocumentListener;
import dragonconsole.util.*;
import dragonconsole.file.*;

/**
 * DragonConsole is a console mimic designed to give Java programmers a RTF
 * console for which to use with any console based/styled applications they
 * wish to write.
 * <br /><br />
 * DragonConsole allows for color output using color codings in text, default
 * char for color code is '&' followed by a letter disignator that represents
 * the foreground color (text) and then another letter for the background
 * color (i.e. '&lb' is Blue font with Black background).
 * <br /><pre>
 * Color        Code
 * Red          r
 * Dark Red     R
 * Blue         l
 * Dark Blue    L
 * Green        g
 * Dark Green   G
 * Yellow       y
 * Dark Yellow  Y
 * Cyan         c
 * Dark Cyan    C
 * Gray         x (Default)
 * Dark Gray    X
 * Purple       p
 * Dark Purple  P
 * Gold         d
 * Black        b
 * White        w
 * Orange       o
 * </pre><br />
 * Built in Defaults:<br />
 * - Color Code char: '&'<br />
 * - Default Fore/Background color: 'xb'<br />
 * - Default System Message color: 'cb'<br />
 * - Default Error Message color: 'rb'<br />
 * -- Mac Style Defaults<br />
 * - Color Code char: '&'<br />
 * - Default Fore/Background color: 'bw'<br />
 * - Default System Message color: 'ow'<br />
 * - Default System Message color: 'rw'<br />
 * <br /><br/>
 * Additional Features include arrow key navigation for previous entered
 * commands (default stored is the last 10, but this is configuarable).
 * <br /><br />
 * Configurable controls for adding a new line in the input box with the "Enter"
 * key or having it just send the input to the output window.
 *
 * @author Brandon E Buck
 * @since October 30, 2009
 * @version 3.0.0
 */
public class DragonConsole extends JFrame implements KeyListener {
    // Version variables
    private static final String VERSION = "3"; // Version Number
    private static final String SUB_VER = "0"; // Sub-version or minor change
    private static final String BUG_FIX = "0"; // Bug fix for current version/minor change

    // GUI Varaiables
    private int width = 800;
    private int height = 600;
    private double dividerPercentage = 0.75;
    private int dividerLocation = 0;
    private JTextPane outputPane;
    private JTextArea inputArea;
    private Font consoleFont = new Font("Monospaced", Font.BOLD, 14);
    private StyledDocument outputStyledDocument;
    private ArrayList<String> previousEntries;
    private int numberOfPreviousEntries;
    private int currentPreviousEntry;
    private CommandProcessor commandProcessor = null;
    private String defaultTitle = "DragonConsole - " + getVersion();
    private PromptPanel consolePrompt = new PromptPanel(" :>");
    private InputController inputControl;

    // Flags
    private boolean inputFieldNewLine;
    private boolean printDefaultMessage = true;
    private boolean useDefaultStyle = true;

    // Default text variables
    private String defaultColor = "xb";
    private String systemColor = "cb";
    private String errorColor = "rb";
    private String inputColor = "xb";
    private char colorCodeChar = '&';

    // Default color variables
    private Color defaultBackground = Color.BLACK;
    private Color defaultForeground = Color.GRAY.brighter(); // GRAY is too dark personally
    private Color defaultCaret = Color.GRAY.brighter();
    private Color defaultMacBackground = Color.WHITE;
    private Color defaultMacForeground = Color.BLACK;
    private Color defaultMacCaret = Color.BLACK;

    /** Default Constructor with the default values for the Console.
     * This constructor sets all the default values:<br />
     * <pre>Title:       "DragonConsole - " + Version Number
     * Size:        800x600
     * Location:    Centered</pre>
     */
    public DragonConsole() {
        super();
        this.setTitle(this.defaultTitle);
        this.initializeConsole();
    }

    /** Default constructor, except tells the console not to print default message.
     */
    public DragonConsole(boolean printDefaultMessage) {
        super();
        this.printDefaultMessage = printDefaultMessage;
        this.setTitle(this.defaultTitle);
        this.initializeConsole();
    }

    /** Creates a console with all default values except a custom title.
     * @param title The title for the DragonConsole window.
     */
    public DragonConsole(String title) {
        super();
        this.setTitle(title);
        this.initializeConsole();
    }

    /** Creates a new DragonConsole with a custom title, and does not print the default message.
     * Creates a new DragonConsole with a custom title and specifies not to print
     * the defaults message to the console.
     * @param title The Title that you want he DragonConsole frame to have.
     * @param printDefaultMessage Either <code>true</code> or <code>false</code> and tells the DragonConsole to print or not to print the default message.
     */
    public DragonConsole(String title, boolean printDefaultMessage) {
        super();
        this.setTitle(title);
        this.printDefaultMessage = printDefaultMessage;
        this.initializeConsole();
    }

    /** Sets the default color style to resemble a Mac Terminal.
     * Set the background to White and the foreground text to Black to mimic
     * the default color settings of a Mac Terminal.
     * Also changes the defaultColor, systemColor, and errorColor codes so
     * that they take into account the new background/foreground colors.
     */
    public void setMacStyle() {
        useDefaultStyle = false;
        outputPane.setBackground(defaultMacBackground);
        outputPane.setCaretColor(defaultMacCaret);
        outputPane.setForeground(defaultMacForeground);
        inputArea.setBackground(defaultMacBackground);
        inputArea.setCaretColor(defaultMacCaret);
        inputArea.setForeground(defaultMacForeground);
        consolePrompt.setPromptForeground(defaultMacForeground);
        consolePrompt.setBackground(defaultMacBackground);
        defaultColor = "bw";
        systemColor = "ow";
        errorColor = "rw";
        inputColor = "bw";

        setInputAttribute();
    }

    /** Sets the colors so the console appears to be a standard Gray on Black.
     * Sets the default styles so the console colors mimic that of a standard
     * Gray on Black color setting. Sets the default colors for defaultColor,
     * systemColor, and errorColor.
     */
    public void setDefaultStyle() {
        useDefaultStyle = true;
        outputPane.setBackground(defaultBackground);
        outputPane.setCaretColor(defaultCaret);
        outputPane.setForeground(defaultForeground);
        inputArea.setBackground(defaultBackground);
        inputArea.setCaretColor(defaultCaret);
        inputArea.setForeground(defaultForeground);
        consolePrompt.setPromptForeground(defaultForeground);
        consolePrompt.setBackground(defaultBackground);
        defaultColor = "xb";
        systemColor = "cb";
        errorColor = "rb";
        inputColor = "xb";

        setInputAttribute();
    }

    /** Sets the Input AttributeSet for the input control to the given input color
     */
    private void setInputAttribute() {
        inputControl.setInputAttributeSet(outputStyledDocument.getStyle(inputColor));
    }

    /** Centers the window based on screen size and window size.
     */
    private void centerWindow() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defaultToolkit.getScreenSize();
        this.setLocation(
                (int)((screenSize.getWidth() / 2) - (this.getWidth() / 2)),
                (int)((screenSize.getHeight() / 2) - (this.getHeight() / 2)));
    }

    /** Sets all the default standard values for the console.
     * Sets all the values that are shared between all constructors so that
     * the console maintains similarities when other settings are turned on/off
     * or changed.
     */
    private void initializeConsole() {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Input Field default behaiviour
        inputFieldNewLine = true;

        // Navigating Previous Entries default behaviour
        //  Default holds 10 values, most recent entry at index 0, and oldest at
        //  the end.
        previousEntries = new ArrayList<String>();
        numberOfPreviousEntries = 10;
        currentPreviousEntry = 0;

        // Create a new input controller
        inputControl = new InputController(null);

        // Setting the Font properly for the Prompt
        consolePrompt.setPromptFont(consoleFont);

        outputPane = new JTextPane();
        outputPane.setBackground(defaultBackground);
        outputPane.setForeground(defaultForeground);
        outputPane.setCaretColor(defaultCaret);
        outputPane.setFont(consoleFont);
        outputPane.addKeyListener(this);
        //outputPane.setEditable(false);
        //outputPane.setFocusable(false);
        outputPane.setBorder(null);

        // Add Copy functionality to the Console
        //   and remove Cut/Paste functionality
        JTextComponent.KeyBinding newBindings[] = {
            new JTextComponent.KeyBinding(
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK),
                    javax.swing.text.DefaultEditorKit.copyAction),
           new JTextComponent.KeyBinding(
                    KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK),
                    javax.swing.text.DefaultEditorKit.beepAction),
           new JTextComponent.KeyBinding(
                    KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK),
                    javax.swing.text.DefaultEditorKit.beepAction)
        };
        Keymap k = outputPane.getKeymap();
        JTextComponent.loadKeymap(k, newBindings, outputPane.getActions());

        // Get the outputPanes StyledDocument and add the DocumentFilter to it
        outputStyledDocument = outputPane.getStyledDocument();
        ((AbstractDocument)outputStyledDocument).setDocumentFilter(inputControl);

        inputArea = new JTextArea();
        inputArea.setBackground(defaultBackground);
        inputArea.setForeground(defaultForeground);
        inputArea.setCaretColor(defaultCaret);
        inputArea.setWrapStyleWord(true);
        inputArea.setLineWrap(true);
        inputArea.setFont(consoleFont);
        inputArea.setBorder(null);
        //inputArea.addKeyListener(this);
        //int inputHeight = (this.getHeight())
        //Dimension inputSize = new Dimension();

        JScrollPane outputScrollPane = new JScrollPane(outputPane);
        outputScrollPane.setBorder(null);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputScrollPane.setBorder(null);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(consolePrompt, BorderLayout.WEST);
        inputPanel.add(inputArea, BorderLayout.CENTER);

        JPanel splitPane = new JPanel(new BorderLayout());
        splitPane.add(outputPane, BorderLayout.CENTER);
        splitPane.add(inputPanel, BorderLayout.SOUTH);

        add(splitPane);

        setDefaultStyle();
        setOutputStyles();
        printDefault();
        centerWindow();
        append("&rbThe console is currenlty in a non-working state, issues arose and the project had to be reset.\n"
                + "Version 3.0.0 BETA is on it's way and should be fully functional soon!\n"
                + "Sorry if this has caused any inconvieniences.\n"
                + " &wb--&CbAuthor, Brandon E Buck, Nov 6, 2010");
    }

    /** Prints a default message that's stored in a text file in the .jar.
     * Prints a default message thats saved within the .jar to the console. The
     * message is just a text file with a default output advertising the
     * DragonConsole.
     */
    private void printDefault() {
        if (printDefaultMessage) {

        }
    }

    public void setInputColor(String inputColor) {
        this.inputColor = inputColor;
        setInputAttribute();
    }
    /** Sets a CommandProcessor to act as an outside input processor if the user
     * decides to add custom control via text based commands with the
     * DragonConsole.
     * @param newCommandProcessor The new command processor that this console should send input to.
     */
    public void setCommandProcessor(CommandProcessor newCommandProcessor) {
        commandProcessor = newCommandProcessor;
        commandProcessor.setDragonConsole(this);
    }

    /** Sets the character used to designate a two character color code.
     * Sets the default character to test for when looking for two character
     * color codes, the default colorCodeChar is '&'.
     * @param colorCodeChar The new character for color codes.
     */
    public void setColorCodeChar(char colorCodeChar) {
        this.colorCodeChar = colorCodeChar;
    }

    /** Turns on/off the ability to set the action of ENTER in the input field.
     * Changes the action of the Enter key, if true is passed then when holding
     * the SHIFT key is pressed then ENTER will make create a new line, without
     * pressing SHIFT the ENTER key sends the input text to the output field.
     * If false is passed then pressing the ENTER key with or without SHIFT
     * pressed will pass the text in the input field to the output field.
     * @param inputFieldNewLine True for new line, false if not.
     */
    public void setInputFieldNewLine(boolean inputFieldNewLine) {
        this.inputFieldNewLine = inputFieldNewLine;
    }

    /** Sets the default output color for all uncolored text.
     * @param defaultColor The new default color for console text.
     */
    public void setDefaultColor(String defaultColor) {
        this.defaultColor = defaultColor;
    }

    /** Sets the default color for all Error messages sent to the console.
     * @param errorColor The new color for default error messages.
     */
    public void setErrorColor(String errorColor) {
        this.errorColor = errorColor;
    }

    /** Sets the default font for the console and changes the styles to match.
     * @param consoleFont The new Font for the console to use.
     */
    public void setConsoleFont(Font consoleFont) {
        this.consoleFont = consoleFont;
        this.setOutputStyles();
        outputPane.setFont(consoleFont);
        inputArea.setFont(consoleFont);
        consolePrompt.setPromptFont(consoleFont);
    }

    /** Sets the default system color for system messages passed to the console.
     * @param systemColor The new color for default system messages.
     */
    public void setSystmeColor(String systemColor) {
        this.systemColor = systemColor;
    }

    /** Adds all the output styles to outputFields Styled Document.
     */
    private void setOutputStyles() {
        outputStyledDocument =
                DocumentStyler.styleDocument(outputStyledDocument, consoleFont);

        setInputAttribute();
    }

    /** Gets a string representation of the current console version.
     * Gets a string representation of the current console version. The version
     * string is formatted as "v" + Version number + Mini Release Version +
     * Bug fix number.
     *
     * All numbers are integers.
     * @return Returns the version string for use with anything.
     */
    public String getVersion() {
        return "v" + VERSION + "." + SUB_VER + "." + BUG_FIX;
    }

    /**
     * Processes a String and prints the String according to all imbedded
     * color codes. If called from the CommandProcessor you can add in your
     * own color codes to the String to give a "System" color. ** DEPRECATED Use
     * <code>append(String)</code> instead.
     * @param outputToProcess The string to be color coded and printed.
     */
    @Deprecated
    public void displayString(String outputToProcess) {
        String temp = "";
        String style = defaultColor;

        for (int i = 0; i < outputToProcess.length(); i++) {
            if (outputToProcess.charAt(i) == '-') {
                //if ((i + 1) < outputToProcess.length()) {
                    if ( ((i + 1) < outputToProcess.length()) &&
                            (outputToProcess.charAt(i + 1) == colorCodeChar)) {
                        temp += colorCodeChar;
                        i++; // Jump past the -
                    }
                //}
                else
                    temp = temp + outputToProcess.charAt(i);
            }
            else if (outputToProcess.charAt(i) == colorCodeChar) {
                print(temp, style);
                style = outputToProcess.substring(i + 1, i + 3);
                temp = ""; // Clear temp for the next series of colored text
                i += 2; // Jump past the two character color code
            }
            else
                temp = temp + outputToProcess.charAt(i);
        }
        print(temp + "\n", style);
    }

    /** Prints the given output without processing.
     * This method prints the passed output String to the console without
     * processing it for scripts or color codes. This would be used if you
     * wanted to directly post user input to the console to prevent the user
     * from creating his/her own input areas that would cause issues with your
     * program.
     * @param ouput The output to print.
     */
    public void appendWithoutProcessing(String ouput) {
        print(ouput, defaultColor);
        inputControl.setBasicInput(outputStyledDocument.getLength());
    }

    /**
     * Processes a String and prints the String according to all embedded
     * color codes. If called from the CommandProcessor you can add in your
     * own color codes to the String to give a "System" color.
     * @param outputToProcess The string to be color coded and printed.
     */
    public void append(String outputToProcess) {
        boolean hasInput = false;
        String processed = "";
        String style = defaultColor;
        int processedOffset = 0; // Number of ingored characters

        for (int i = 0; i < outputToProcess.length(); i++) {
            if (outputToProcess.charAt(i) == colorCodeChar) {
                if ( ((i + 1) < outputToProcess.length()) &&
                        (outputToProcess.charAt(i + 1) == colorCodeChar)) {
                    processed += colorCodeChar;
                    i += 2; // Jump past the - (&&)
                    processedOffset += 1;

                } else if ((i + 2) < outputToProcess.length()) {
                    print(processed, style);
                    style = outputToProcess.substring(i + 1, i + 3);
                    processed = ""; // Clear processed for the next series of colored text
                    i += 2; // Jump past the two character color code
                    processedOffset += 3;

                } else
                    processed += outputToProcess.charAt(i);
            } else if (outputToProcess.charAt(i) == '%') {
                System.out.println("Found at: " + i);
                if ((i + 1) < outputToProcess.length() &&
                        outputToProcess.charAt(i + 1) == '%') {
                    processed += "%";
                    i += 2; // Jump past the "%%"
                    processedOffset += 1;

                } else if (outputToProcess.indexOf(';', i) > i) {
                    if (outputToProcess.charAt(i + 1) == 'i') {
                        hasInput = true;
                        String inputCommand = outputToProcess.substring(i, outputToProcess.indexOf(';', i) + 1);
                        if (inputControl.setInputStyle(inputCommand, i - processedOffset)) {
                            
                            print(processed, style); // Print what's been processed in it's color
                            print(inputControl.getInputRangeString(), defaultColor); // Print the blank space if the input is not infinite
                            
                            processed = ""; // Clear processed for the next series of colored text
                        } else
                            outputToProcess = ""; // Clear out the output to process if input is infinite, which means anything after the input string is ignored.

                        i = outputToProcess.indexOf(';', i);
                    }
                } else
                    processed += outputToProcess.charAt(i);
            } else
                processed += outputToProcess.charAt(i);
        }
        print(processed, style);

        if (!(hasInput))
            inputControl.setBasicInput(outputStyledDocument.getLength());

        setConsoleCaretPosition();
    }

    /** Sets the caret position in the ouputPane according to the input controller.
     * Sets the caret position to the start of the current input, if not
     * currently receiving input then it sets the caret position to the end of
     * the document.
     */
    private void setConsoleCaretPosition() {
        int caretLocation = inputControl.inputRangeStart();
        System.out.println("caretLocation = " + caretLocation);
        if (caretLocation > -1)
            outputPane.setCaretPosition(caretLocation);
        else
            outputPane.setCaretPosition(outputStyledDocument.getLength());
    }

    /** Sends a message to be output with the default system color.
     * Prints a message to the console using the default specified System color
     * code. ** DEPRECATED use <code>appendSystemMessage(String)</code> instead.
     * @param message The string to display as a System Message
     */
    @Deprecated
    public void displaySystemMessage(String message) {
        print(message, systemColor);
    }

    /** Sends a message to be output with the default system color.
     * Prints a message to the console using the default specified System color
     * code.
     * @param message The string to display as a System Message
     */
    public void appendSystemMessage(String message) {
        this.print(message, systemColor);
    }

    /** Sends a message to be output with the default error color.
     * Prints a message to the console using the default specified Error color
     * code. DEPRECATED use <code>appendErrorMessage(String)</code> instead.
     * @param message The message to display as an Error Message.
     */
    @Deprecated
    public void displayErrorMessage(String message) {
        print(message, errorColor);
    }

    /** Sends a message to be output with the default error color.
     * Prints a message to the console using the default specified Error color
     * code.
     * @param message The message to display as an Error Message.
     */
    public void appendErrorMessage(String message) {
        this.print(message, errorColor);
    }

    /**
     * This method adds a string to outputPane's StyledDocument allowing the
     * text to be styled by the predefined color styles.
     * @param output The text string to add to the Console.
     * @param style The color code for this text String.
     */
    protected void print(String output, String style) {
        try { // Try to add the colored string to the output area document
            outputStyledDocument.insertString(
                    outputStyledDocument.getLength(), output,
                    outputStyledDocument.getStyle(style));
        }
        catch (BadLocationException e) {
            JOptionPane.showMessageDialog(this,
                    "An error ocurred adding the input to the console. \n"
                    + "The program must exit.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if ((!inputFieldNewLine) || !(e.isShiftDown())) {
                e.consume();

                String input = inputArea.getText();
                inputArea.setText("");
                if (commandProcessor != null)
                    commandProcessor.processCommand(input);
                else
                    appendWithoutProcessing(input);

                // Previous Entry controls
                previousEntries.add(input);
                if (previousEntries.size() > numberOfPreviousEntries)
                    previousEntries.remove(0);
                currentPreviousEntry = previousEntries.size();
            } else {
                inputArea.append("\n");
            }
        }

        if ((e.getKeyCode() == KeyEvent.VK_RIGHT) && e.isShiftDown()) {
            e.consume();

            currentPreviousEntry--;
            if (currentPreviousEntry < 0)
                currentPreviousEntry = 0;
            else
                inputArea.setText(previousEntries.get(currentPreviousEntry));
        }

        if ((e.getKeyCode() == KeyEvent.VK_LEFT) && (e.isShiftDown())) {
            e.consume();

            currentPreviousEntry++;
            if (currentPreviousEntry >= previousEntries.size()) {
                currentPreviousEntry = previousEntries.size();
                inputArea.setText("");
            } else
                inputArea.setText(previousEntries.get(currentPreviousEntry));
        }
    }

    public void keyReleased(KeyEvent e) { }

    private class PromptPanel extends JPanel {
        private JLabel promptLabel;

        public PromptPanel(String prompt) {
            promptLabel = new JLabel(prompt);
            promptLabel.setOpaque(false);
            setLayout(new BorderLayout());
            add(promptLabel, BorderLayout.NORTH);
        }

        public void setPrompt(String newPrompt) {
            promptLabel.setText(newPrompt);
        }

        public String getPrompt() {
            return promptLabel.getText();
        }

        public void setPromptFont(Font font) {
            promptLabel.setFont(font);
            promptLabel.revalidate();
            promptLabel.repaint();
        }

        public void setPromptForeground(Color c) {
            promptLabel.setForeground(c);
            promptLabel.revalidate();
            promptLabel.repaint();
        }
    }

    private class DCDocumentListener implements DocumentListener {
        public DCDocumentListener() {
            super();
        }

        public void insertUpdate(DocumentEvent e) { }
        public void removeUpdate(DocumentEvent e) { }
        public void changedUpdate(DocumentEvent e) { }
    }

    /** Controls Input, more Documentation later.
     * @author Brandon Buck
     */
    private class InputController extends DocumentFilter {
        private int rangeStart;
        private int rangeEnd;
        private boolean protect;
        private DCString input;
        private String protectedChar = "*";
        private boolean isReceivingInput;
        private AttributeSet inputAttr;

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
        private InputController(AttributeSet attr) {
            super();
            rangeStart = 0;
            rangeEnd = 0;
            protect = false;
            input = new DCString("");
            isReceivingInput = false;
            inputAttr = attr;
        }

        /** Changes the <code>inputAttr</code> for the input controller.
         * Changes the default <code>AttributeSet</code> for this
         * <code>InputController</code> that is used to style all input.
         * @param newInputAttr The new <code>AttributeSet</code> to use
         */
        public void setInputAttributeSet(AttributeSet newInputAttr) {
            inputAttr = newInputAttr;
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
         * @param newInputStyle String containing the new input style for the input controller.
         * @param startPosition The position in the console in which this input will begin.
         * @return Returns true if all text after the input command should be ignored (if the input has an unlimited Range);
         */
        public boolean setInputStyle(String newInputStyle, int startPosition) {
            rangeStart = startPosition;
            rangeEnd = startPosition;
            protect = false;
            input = new DCString("");
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
                        int range = Integer.parseInt(inputStyle);
                        rangeEnd = rangeStart + range;

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
        public int inputRangeStart() {
            if (isReceivingInput)
                return rangeStart;
            else
                return -1;
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
            input = new DCString("");
            isReceivingInput = true;
        }

        public String getInputRangeString() {
            if (isReceivingInput && rangeEnd > 0) {
                String inputRangeString = "";
                for (int x = rangeStart; x < rangeEnd; x++)
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

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            fb.insertString(offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (isReceivingInput) {
                if (offset >= rangeStart) {
                    if (!isInfiniteInput() && (offset + 1) <= rangeEnd) {
                        if (protect)
                            fb.replace(offset, 1, protectedChar, inputAttr);
                        else
                            fb.replace(offset, 1, string, inputAttr);
                        
                        input.replace((offset - rangeStart), 1, string);
                        
                    } else if (isInfiniteInput()) {
                        if (protect)
                            fb.replace(offset, length, protectedChar, inputAttr);
                        else
                            fb.replace(offset, length, string, inputAttr);

                        input.append(string);
                    }
                }
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (isReceivingInput) {
                if (!(offset < rangeStart)) {
                    if (rangeEnd > -1) {
                        fb.remove(offset, length);
                        fb.insertString((rangeEnd - 1), " ", inputAttr);

                        if (outputPane.getCaretPosition() == rangeEnd)
                            outputPane.setCaretPosition(rangeEnd - 1);
                    } else
                        fb.remove(offset, length);

                    
                    if (!isInfiniteInput())
                        input.rangeRemove((offset - rangeStart), length);
                    else
                        input.remove((offset - rangeStart), length);
                }
            }
        }
    }
}