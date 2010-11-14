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
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.ArrayList;
import dragonconsole.util.*;
import dragonconsole.file.*;
import java.awt.datatransfer.DataFlavor;;

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
 * Dark Gold    D
 * Black        b
 * White        w
 * Orange       o
 * Dark Orange  O
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
 * commands (default stored is the last 10, but this is configurable).
 * <br /><br />
 * Configurable controls for adding a new line in the input box with the "Enter"
 * key or having it just send the input to the output window.
 *
 * @author Brandon E Buck
 * @since October 30, 2009
 * @version 3.0.0
 */
public class DragonConsole extends JPanel implements KeyListener, CaretListener {
    // Version variables
    private static final String VERSION = "3"; // Version Number
    private static final String SUB_VER = "0"; // Sub-version or minor change
    private static final String BUG_FIX = "0"; // Bug fix for current version/minor change
    private static final String VER_TAG = "b"; // Alpha/Beta tag, blank for non-alpha/beta releases.

    // GUI
    private JTextPane consolePane;
    private JTextArea inputArea;
    private StyledDocument consoleStyledDocument;
    private JScrollPane consoleScrollPane;

    // Console
    private CommandProcessor commandProcessor = null;
    private InputController inputControl;

    // Console GUI (fonts/colors/etc)
    private Font consoleFont;
    private PromptPanel consolePrompt = new PromptPanel(">> ");
    private ArrayList<TextColor> textColors;

    // Input Utility
    private ArrayList<String> previousEntries;
    private int numberOfPreviousEntries;
    private int currentPreviousEntry;

    // Flags
    private boolean inputFieldNewLine = true; // If SHIFT+ENTER makes a new line or not
    private boolean printDefaultMessage = true; // Print the DragonConsole logo
    private boolean useInlineInput = true; // Determines if the cnosole uses inline input or not
    private boolean ignoreInput = false; // Allows the Programmer to Disble input
    private boolean inputCarryOver = true; // If output is sent while input is being receivied, saves input for next input area

    // Default text variables
    private String defaultColor = "xb";
    private String systemColor = "cb";
    private String errorColor = "rb";
    private String inputColor = "xb";
    private char colorCodeChar = '&';

    // Default Console Colors
    private Color defaultBackground = Color.BLACK;
    private Color defaultForeground = Color.GRAY.brighter(); // GRAY is too dark personally
    private Color defaultCaret = Color.GRAY.brighter();
    private Color defaultMacBackground = Color.WHITE;
    private Color defaultMacForeground = Color.BLACK;
    private Color defaultMacCaret = Color.BLACK;

    /** Default Constructor uses all the default values.
     */
    public DragonConsole() {
        super();
        this.setSize(800, 600);
        this.useInlineInput = true;
        this.printDefaultMessage = true;

        this.initializeConsole();
    }

    /** Creates a Console with the specified input method.
     * @param useInlineInput <code>true</code> to use inline input or <code>false</code> to use a text area for input.
     */
    public DragonConsole(boolean useInlineInput) {
        super();
        this.setSize(800, 600);
        this.useInlineInput = useInlineInput;
        this.printDefaultMessage = false;

        this.initializeConsole();
    }

    /** Creates a console with the specified input method and specifies whether to print the logo or not.
     * Tells the console to use either inline input or a text area for input
     * and whether or not to print the default message (DragonConsole logo).
     * @param useInlineInput <code>true</code> to use inline input or <code>false</code> to use a text area for input.
     * @param printDefaultMessage <code>true</code> to print the DragonConsole logo or <code>false</code> to print nothing.
     */
    public DragonConsole(boolean useInlineInput, boolean printDefaultMessage) {
        super();
        this.setSize(800, 600);
        this.useInlineInput = true;
        this.printDefaultMessage = false;

        this.initializeConsole();
    }

    /** Creates a console with a custom width and height. 
     * Allows the programmer to initialize a new console with custom initial
     * settings.
     * @param width Width of the Console.
     * @param height Height of the Console.
     */
    public DragonConsole(int width, int height) {
        super();
        this.setSize(width, height);
        this.useInlineInput = true;
        this.printDefaultMessage = true;

        this.initializeConsole();
    }

    /** Creates a console with a custom width and height and the specified input method.
     * Allows the programmer to initialize a new console with custom initial
     * settings.
     * @param width Width of the Console.
     * @param height Height of the Console.
     * @param useInlineInput <code>true</code> to use inline input or <code>false</code> to use a text area for input.
     */
    public DragonConsole(int width, int height, boolean useInlineInput) {
        super();
        this.setSize(width, height);
        this.useInlineInput = useInlineInput;
        this.printDefaultMessage = true;

        this.initializeConsole();
    }

    /** Creates a console with a custom width and height, the specified input method, and whether or not to print the logo.
     * Allows the programmer to initialize a new console with custom initial
     * settings.
     * @param width Width of the Console.
     * @param height Height of the Console.
     * @param useInlineInput <code>true</code> to use inline input or <code>false</code> to use a text area for input.
     * @param printDefaultMessage <code>true</code> to print the DragonConsole logo or <code>false</code> to print nothing.
     */
    public DragonConsole(int width, int height, boolean useInlineInput, boolean printDefaultMessage) {
        super();
        this.setSize(width, height);
        this.useInlineInput = useInlineInput;
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
        consolePane.setBackground(defaultMacBackground);
        consolePane.setCaretColor(defaultMacCaret);
        consolePane.setForeground(defaultMacForeground);
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
        clearConsole();
        printDefault();
    }

    /** Sets the colors so the console appears to be a standard Gray on Black.
     * Sets the default styles so the console colors mimic that of a standard
     * Gray on Black color setting. Sets the default colors for defaultColor,
     * systemColor, and errorColor.
     */
    public void setDefaultStyle() {
        consolePane.setBackground(defaultBackground);
        consolePane.setCaretColor(defaultCaret);
        consolePane.setForeground(defaultForeground);
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
        clearConsole();
        printDefault();
    }

    public void clearConsole() {
        inputControl.clearText();
    }

    /** Sets the Input AttributeSet for the input control to the given input color.
     * This is called when the input color is changed so that all input is added
     * with the AttributeSet (Style) that has been specified for input.
     */
    private void setInputAttribute() {
        inputControl.setInputAttributeSet(consoleStyledDocument.getStyle(inputColor));

        if (!useInlineInput) {
            inputArea.setForeground(getStyleColorFromCode(inputColor.charAt(0))); // Foreground
            inputArea.setBackground(getStyleColorFromCode(inputColor.charAt(1))); // Background
        }
    }

    private Color getStyleColorFromCode(char foreground) {
        for (int i = 0; i < textColors.size(); i++) {
            TextColor tc = textColors.get(i);
            if (tc.equals(foreground))
                return tc.getColor();
        }

        return Color.WHITE;
    }

    /** Sets the size of this JPanel.
     * This method is overridden so that it will change the Maximum, Minimum,
     * and Preferred Size, as well as call the super.setSize(dim) method.
     * @param width The width of this object
     * @param height The height of this object.
     */
    @Override
    public void setSize(int width, int height) {
        Dimension dim = new Dimension(width, height);
        this.setSize(dim);
    }

    /** Sets the size of this JPanel.
     * This method is overridden so that it will change the Maximum, Minimum,
     * and Preferred Size, as well as call the super.setSize(dim) method.
     * @param dim The new size of this object.
     */
    @Override
    public void setSize(Dimension dim) {
        super.setMaximumSize(dim);
        super.setMinimumSize(dim);
        super.setPreferredSize(dim);
    }

    /** Overridden to remove functionality, use <code>setSize()</code>
     * Functionality has been removed, use <code>setSize()</code>
     */
    @Override
    public void setMaximumSize(Dimension dim) { }

    /** Overridden to remove functionality, use <code>setSize()</code>
     * Functionality has been removed, use <code>setSize()</code>
     */
    @Override
    public void setMinimumSize(Dimension dim) { }
    
    /** Overridden to remove functionality, use <code>setSize()</code>
     * Functionality has been removed, use <code>setSize()</code>
     */
    @Override
    public void setPreferredSize(Dimension dim) { }

    /** Sets all the default standard values for the console.
     * Sets all the values that are shared between all constructors so that
     * the console maintains similarities when other settings are turned on/off
     * or changed.
     */
    protected void initializeConsole() {
        // Navigating Previous Entries default behaviour
        //  Default holds 10 values, most recent entry at index 0, and oldest at
        //  the end.
        previousEntries = new ArrayList<String>();
        numberOfPreviousEntries = 10;
        currentPreviousEntry = 0;

        // Initialzie textColors ArrayList
        textColors = new ArrayList<TextColor>();

        // Create a new input controller
        inputControl = new InputController(null);

        // Setting the Font properly for the Prompt
        consoleFont = FileProcessor.getConsoleFont().deriveFont(Font.BOLD, 14f);
        consolePrompt.setPromptFont(consoleFont);

        if (useInlineInput) {
            consolePane = new JTextPane() {
                @Override
                public void paste() {
                    try {
                        String pasteText = (String)(Toolkit.getDefaultToolkit()
                                .getSystemClipboard()
                                .getData(DataFlavor.stringFlavor));
                        getDocument().insertString(getCaretPosition(),
                                pasteText, null);
                    } catch (Exception exc) { }
                }
            };
            
            consolePane.addKeyListener(this);
            consolePane.addCaretListener(this);

            inputControl.installConsole(consolePane);

        } else {
            consolePane = new JTextPane();
            consolePane.setFocusable(false);
            consolePane.setEditable(false);
        }

        consolePane.setBackground(defaultBackground);
        consolePane.setForeground(defaultForeground);
        consolePane.setCaretColor(defaultCaret);
        consolePane.setFont(consoleFont);
        consolePane.setBorder(null);

        // Add Copy functionality to the Console
        //   and remove Cut/Paste functionality
        JTextComponent.KeyBinding newBindings[] = {
            new JTextComponent.KeyBinding(
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK),
                    javax.swing.text.DefaultEditorKit.copyAction),
           new JTextComponent.KeyBinding(
                    KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK),
                    javax.swing.text.DefaultEditorKit.pasteAction),
           new JTextComponent.KeyBinding(
                    KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK),
                    javax.swing.text.DefaultEditorKit.beepAction)
        };
        Keymap k = consolePane.getKeymap();
        JTextComponent.loadKeymap(k, newBindings, consolePane.getActions());

        // Get the outputPanes StyledDocument and add the DocumentFilter to it
        consoleStyledDocument = consolePane.getStyledDocument();

        if (useInlineInput)
            ((AbstractDocument)consoleStyledDocument).setDocumentFilter(inputControl);

        inputArea = new JTextArea();
        inputArea.setBackground(defaultBackground);
        inputArea.setForeground(defaultForeground);
        inputArea.setCaretColor(defaultCaret);
        inputArea.setWrapStyleWord(true);
        inputArea.setLineWrap(true);
        inputArea.setFont(consoleFont);
        inputArea.setBorder(null);
        inputArea.addKeyListener(this);

        consoleScrollPane = new JScrollPane(consolePane);
        consoleScrollPane.setBorder(null);

        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputScrollPane.setBorder(null);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(consolePrompt, BorderLayout.WEST);
        inputPanel.add(inputArea, BorderLayout.CENTER);

        JPanel splitPane = new JPanel(new BorderLayout());
        splitPane.add(consoleScrollPane, BorderLayout.CENTER);
        splitPane.add(inputPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        if (useInlineInput)
            add(consoleScrollPane, BorderLayout.CENTER);
        else
            add(splitPane, BorderLayout.CENTER);

        setOutputStyles();
        setDefaultStyle();
    }

    /** Prints the logo for DragonConsole if <code>printDefaultMessage</code> is <code>true</code>
     * Prints the DragonConsole logo that is saved in dc_logo.txt in the .jar
     * if <code>printDefaultMessage</code> is set to <code>true</code>.
     */
    private void printDefault() {
        if (printDefaultMessage) {
            try {
                String color = "b";
                if (consolePane.getBackground().equals(Color.WHITE))
                    color = "w";

                append(FileProcessor.readDCResource("colors_" + color));
            } catch(Exception exc) { }
        }
    }

    /** Sets the the input color code (a string of two characters).
     * Changes the Style that all input should use. The String passed is the
     * name of the style which is the character for the foreground color and the
     * character for the background color.
     * @param inputColor The new input color string to use for input.
     */
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
        if (commandProcessor != null)
            commandProcessor.uninstall();

        commandProcessor = newCommandProcessor;
        commandProcessor.install(this);
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
        consolePane.setFont(consoleFont);
        inputArea.setFont(consoleFont);
        consolePrompt.setPromptFont(consoleFont);

        DocumentStyler.changeFont(consoleStyledDocument, consoleFont);
    }

    /** Sets the default system color for system messages passed to the console.
     * @param systemColor The new color for default system messages.
     */
    public void setSystmeColor(String systemColor) {
        this.systemColor = systemColor;
    }

    private void fillConsoleColors() {
        addTextColor('r', Color.RED); // Red
        addTextColor('R', Color.RED.darker()); // Dark Red
        addTextColor('l', Color.BLUE); // Blue
        addTextColor('L', Color.BLUE.darker()); // Dark Blue
        addTextColor('g', Color.GREEN); // Green
        addTextColor('G', Color.GREEN.darker()); // Dark Green
        addTextColor('y', Color.YELLOW); // Yellow
        addTextColor('Y', Color.YELLOW.darker()); // Dark Yellow
        addTextColor('x', Color.GRAY.brighter()); // Gray
        addTextColor('X', Color.GRAY); // Dark Gray
        addTextColor('c', Color.CYAN); // Cyan
        addTextColor('C', Color.CYAN.darker()); // Dark Cyan
        addTextColor('o', Color.ORANGE); // Orange
        addTextColor('O', Color.ORANGE.darker()); // Orange
        addTextColor('p', new Color(128, 0, 255)); // Purple
        addTextColor('P', new Color(128, 0, 255).darker()); // Dark Purple
        addTextColor('d', new Color(241, 234, 139)); // Gold
        addTextColor('D', new Color(241, 234, 139).darker()); // Dark Gold

        // Black and White -- Single colors (no dark version)
        addTextColor('b', Color.BLACK); // Black
        addTextColor('w', Color.WHITE); // White
    }

    public void addTextColor(char code, Color color) {
        TextColor newColor = new TextColor(code, color);
        textColors.add(newColor);

        consoleStyledDocument =
                DocumentStyler.addNewColor(consoleStyledDocument, consoleFont, newColor, textColors);
    }

    /** Adds all the output styles to outputFields Styled Document.
     */
    private void setOutputStyles() {
        fillConsoleColors();
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
        return "v" + VERSION + "." + SUB_VER + "." + BUG_FIX + VER_TAG;
    }

    /**
     * Processes a String and prints the String according to all embedded
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
        inputControl.setBasicInput(consoleStyledDocument.getLength());
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

        for (int i = 0; i < outputToProcess.length(); i++) {
            if (outputToProcess.charAt(i) == colorCodeChar) {
                if ( ((i + 1) < outputToProcess.length()) &&
                        (outputToProcess.charAt(i + 1) == colorCodeChar)) {
                    processed += colorCodeChar;
                    i += 1; // Jump past the - (&&)

                } else if ((i + 2) < outputToProcess.length()) {
                    print(processed, style);
                    style = outputToProcess.substring(i + 1, i + 3);
                    processed = ""; // Clear processed for the next series of colored text
                    i += 2; // Jump past the two character color code

                } else
                    processed += outputToProcess.charAt(i);
            } else if (outputToProcess.charAt(i) == '%') {
                if ((i + 1) < outputToProcess.length() &&
                        outputToProcess.charAt(i + 1) == '%') {
                    processed += "%";
                    i += 1; // Jump past the "%%"

                } else if (outputToProcess.indexOf(';', i) > i) {
                    if (outputToProcess.charAt(i + 1) == 'i') {
                        hasInput = true;
                        String inputCommand = outputToProcess.substring(i, outputToProcess.indexOf(';', i) + 1);
                        if (inputControl.setInputStyle(inputCommand)) {
                            
                            print(processed, style); // Print what's been processed in it's color
                            inputControl.setRangeStart(consoleStyledDocument.getLength());
                            print(inputControl.getInputRangeString(), defaultColor); // Print the blank space if the input is not infinite
                            
                            processed = ""; // Clear processed for the next series of colored text
                        } else {
                            outputToProcess = ""; // Clear out the output to process if input is infinite, which means anything after the input string is ignored.
                            print(processed, style);
                            processed = "";
                            inputControl.setRangeStart(consoleStyledDocument.getLength());
                        }

                        i = outputToProcess.indexOf(';', i);
                    }
                } else
                    processed += outputToProcess.charAt(i);
            } else
                processed += outputToProcess.charAt(i);
        }
        print(processed, style);

        if (!(hasInput))
            inputControl.setBasicInput(consoleStyledDocument.getLength());

        setConsoleCaretPosition();
    }

    /** Sets the caret position in the ouputPane according to the input controller.
     * Sets the caret position to the start of the current input, if not
     * currently receiving input then it sets the caret position to the end of
     * the document.
     */
    private void setConsoleCaretPosition() {
        int caretLocation = inputControl.getInputRangeStart();
        if (caretLocation > -1)
            consolePane.setCaretPosition(caretLocation);
        else
            consolePane.setCaretPosition(consoleStyledDocument.getLength());
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
     * This method adds a string to consolePane's StyledDocument allowing the
     * text to be styled by the predefined color styles.
     * @param output The text string to add to the Console.
     * @param style The color code for this text String.
     */
    protected void print(String output, String style) {
        try { // Try to add the colored string to the output area document
            if (useInlineInput)
                output = inputControl.getBypassPrefix() + output;

            consoleStyledDocument.insertString(
                    consoleStyledDocument.getLength(), output,
                    consoleStyledDocument.getStyle(style));
        }
        catch (BadLocationException e) {
            JOptionPane.showMessageDialog(this,
                    "An error ocurred adding the input to the console. \n"
                    + "The program must exit.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void addPreviousEntry(String entry) {
        previousEntries.add(entry);
        if (previousEntries.size() > numberOfPreviousEntries)
            previousEntries.remove(0);

        currentPreviousEntry = previousEntries.size();
    }

    private void setPreviousEntryText() {
        String text = "";
        if (currentPreviousEntry < previousEntries.size()
                && currentPreviousEntry >= 0)
            text = previousEntries.get(currentPreviousEntry);

        if (useInlineInput) {
            if (inputControl.isReceivingInput() && inputControl.isInfiniteInput())
                inputControl.setInput(text);
        } else
            inputArea.setText(text);

    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        if (!useInlineInput) {
            JScrollBar vbar = consoleScrollPane.getVerticalScrollBar();
            if (vbar.isVisible()) {
                vbar.setValue(vbar.getMaximum());
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!ignoreInput) {
                if (useInlineInput) {
                    e.consume();
                    String input = inputControl.getInput();
                    
                    if (commandProcessor != null)
                        commandProcessor.processCommand(input);
                    else
                        appendWithoutProcessing(input);
                    
                    addPreviousEntry(input);
                    
                } else {
                    if ((!inputFieldNewLine) || !(e.isShiftDown())) {
                        e.consume();

                        String input = inputArea.getText();
                        inputArea.setText("");
                        if (commandProcessor != null)
                            commandProcessor.processCommand(input);
                        else
                            appendWithoutProcessing(input);

                        addPreviousEntry(input);

                    } else {
                        inputArea.append("\n");
                    }
                }
            }
        }

        if ((e.getKeyCode() == KeyEvent.VK_RIGHT) && e.isShiftDown()) {
            e.consume();

            if (!useInlineInput
                    || (useInlineInput
                       && inputControl.isReceivingInput()
                       && inputControl.isInfiniteInput())) {
                currentPreviousEntry--;
                if (currentPreviousEntry < 0)
                    currentPreviousEntry = previousEntries.size();

                setPreviousEntryText();
            }
        }

        if ((e.getKeyCode() == KeyEvent.VK_LEFT) && (e.isShiftDown())) {
            e.consume();

            if (!useInlineInput
                    || (useInlineInput
                       && inputControl.isReceivingInput()
                       && inputControl.isInfiniteInput())) {
            currentPreviousEntry++;
                if (currentPreviousEntry >= previousEntries.size())
                    currentPreviousEntry = previousEntries.size();
                
                setPreviousEntryText();
            }
        }
    }

    public void keyReleased(KeyEvent e) { }

    public void caretUpdate(CaretEvent e) {
        Caret caret = consolePane.getCaret();
        int location = e.getDot();

        if (inputControl.isReceivingInput() && e.getDot() == e.getMark()) {            
            if (location < inputControl.getInputRangeStart()) {
                consolePane.setCaretPosition(inputControl.getInputRangeStart());
                //Toolkit.getDefaultToolkit().beep();
            }

            if (!(inputControl.isInfiniteInput()) && location > inputControl.getInputRangeEnd()) {
                consolePane.setCaretPosition(inputControl.getInputRangeEnd());
                //Toolkit.getDefaultToolkit().beep();
            }      
        }
    }  
}