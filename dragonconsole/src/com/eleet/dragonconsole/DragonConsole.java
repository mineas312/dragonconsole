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

package com.eleet.dragonconsole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import com.eleet.dragonconsole.util.*;
import com.eleet.dragonconsole.file.*;
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
 * There are two unique characters that cannot be used as Color codes since they
 * are used for style control. That is "0" which is used in "&00" which is the
 * equivalent of ANSIStyle "\003[0m". Both reset the style back to it's default. The
 * second is "-" which can be substituted in place of any color in a color code
 * to "carry over" that color. For Example, I have the code "&ob" for Orange
 * foreground and black background and I want to change the foreground without
 * changing the background I can add "&r-" which changes the foreground to red
 * and the background stays the same so the current style code is "&rb".
 * <br /><br />
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
public class DragonConsole extends JPanel implements KeyListener, 
                                                       CaretListener,
                                                       AdjustmentListener {
    // Version variables
    private static final String VERSION = "3"; // Version Number
    private static final String SUB_VER = "0"; // Sub-version or minor change
    private static final String BUG_FIX = "0"; // Bug fix for current version/minor change
    private static final String VER_TAG = "b"; // Alpha/Beta tag, blank for non-alpha/beta releases.

    // Default Finals
    private static final int DEFAULT_WIDTH = 725;
    private static final int DEFAULT_HEIGHT = 450;

    // GUI
    private JTextPane consolePane;
    private JTextArea inputArea;
    private StyledDocument consoleStyledDocument;
    private JScrollPane consoleScrollPane;
    private int cWidth = DEFAULT_WIDTH;
    private int cHeight = DEFAULT_HEIGHT;

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
    // Tells the console to keep the ScrollBar maxed when new content is added, or to ignore ScrollBar changes the user has adjusted it away from max.
    private boolean alwaysKeepScrollBarMaxed = false;
    private boolean isScrollBarAtMax = true; // Tells the setConsoleCaretPosition() to max out the ScrollBar as well or not
    private boolean ignoreAdjustment = false; // Tells the Adjustment Listener to ignore any Adjustments to the Scrollbar

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

    // ANSIStyle Support
    private boolean useANSIColorCodes = false;

    // Style Variables
    private SimpleAttributeSet ANSIStyle = null; // Null unless ANSIStyle is turned on.
    private String currentStyle = defaultColor; // Used for the DragonConsole styles so they work like ANSIStyle

    /** Default Constructor uses all the default values.
     */
    public DragonConsole() {
        super();
        //this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.useInlineInput = true;
        this.printDefaultMessage = true;

        this.initializeConsole();
    }

    /** Creates a Console with the specified input method.
     * @param useInlineInput <code>true</code> to use inline input or <code>false</code> to use a text area for input.
     */
    public DragonConsole(boolean useInlineInput) {
        super();
        //this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.useInlineInput = useInlineInput;
        this.printDefaultMessage = true;

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
        //this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.useInlineInput = useInlineInput;
        this.printDefaultMessage = printDefaultMessage;

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
        this.cWidth = width;
        this.cHeight = height;
        //this.setSize(width, height);
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
        //this.setSize(width, height);
        this.cWidth = width;
        this.cHeight = height;
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
        //this.setSize(width, height);
        this.cHeight = height;
        this.cWidth = width;
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

    /** Scans the TextColor list for the code passed and returns the color corresponding to it.
     * Scans the list of TextColors added to the console for one matching the
     * <code>char code</code> passed and returns the Color that corresponds to
     * the <code>char code</code> given. If no Color that matches the
     * <code>code</code> is found returns <code>Color.WHITE</code>
     * @param code The <code>char code</code> to find the corresponding Color for.
     * @return The Color corresponding to the given <code>code</code> or White if none is found.
     */
    private Color getStyleColorFromCode(char code) {
        for (int i = 0; i < textColors.size(); i++) {
            TextColor tc = textColors.get(i);
            if (tc.equals(code))
                return tc.getColor();
        }

        return Color.WHITE;
    }

    /** Tells the Console how to handled the ScrollBar when new text is added to the Console.
     * Allows the programmer to set the method in which to control the Vertical
     * ScrollBar. If <code>true</code> is passed then anytime new text is
     * added to the <code>consolePane</code> the Vertical JScrollBar will be
     * set to it's max value. If <code>false</code> is passed then as long as
     * The JScrollBar is at it's max value when text is added, then it
     * automatically update to the new max value after the text has been added.
     * If it's moved from the max value by the user then it will stay at
     * whatever value it was placed at by the user.
     * @param alwaysKeepScrollBarMaxed The boolean value determining the method on how to control the Vertical JScrollBar.
     */
    public void setKeepScrollBarMax(boolean alwaysScrollMax) {
        this.alwaysKeepScrollBarMaxed = alwaysScrollMax;
    }

    /** Sets the size of this JPanel.
     * This method is overridden so that it will change the Maximum, Minimum,
     * and Preferred Size, as well as call the super.setSize(dim) method.
     * @param width The width of this object
     * @param height The height of this object.
     */
    public void setConsoleSize(int width, int height) {
        Dimension dim = new Dimension(width, height);
        this.setConsoleSize(dim);
    }

    /** Sets the size of this JPanel.
     * This method is overridden so that it will change the Maximum, Minimum,
     * and Preferred Size, as well as call the super.setSize(dim) method.
     * @param dim The new size of this object.
     */
    public void setConsoleSize(Dimension dim) {
        super.setMaximumSize(dim);
        super.setMinimumSize(dim);
        super.setPreferredSize(dim);
    }

    /** Sets the Character that should be output for protected input.
     * Changes the character that should be used for protected input. By default
     * the character is a "*".
     * @param protectedChar The new character for protected input.
     */
    public void setProtectedCharacter(char protectedChar) {
        inputControl.setProtectedChar(protectedChar);
    }

    /** Sets all the default standard values for the console.
     * Sets all the values that are shared between all constructors so that
     * the console maintains similarities when other settings are turned on/off
     * or changed.
     */
    protected void initializeConsole() {
        setConsoleSize(cWidth, cHeight);

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
        consoleFont = FileProcessor.getConsoleFont().deriveFont(Font.PLAIN, 14f);
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

        } else {
            consolePane = new JTextPane();
            //consolePane.setFocusable(false);
            consolePane.setEditable(false);
        }

        consolePane.setBackground(defaultBackground);
        consolePane.setForeground(defaultForeground);
        consolePane.setCaretColor(defaultCaret);
        consolePane.setFont(consoleFont);
        consolePane.setBorder(null);

        inputControl.installConsole(consolePane);
        inputControl.setConsoleInputMethod(useInlineInput);

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
        if (!useInlineInput)
            inputArea.requestFocusInWindow();

        consoleScrollPane = new JScrollPane(consolePane);
        consoleScrollPane.setBorder(null);
        consoleScrollPane.getVerticalScrollBar().addAdjustmentListener(this);

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

    /** Tell the DragonConsole whether to allow (and display) ANSIStyle Color Codes.
     * Allows the programmer to specify whether to allow ANSIStyle Color Codes or
     * not. If ANSIStyle Color Codes are allowed then you will be unable to use the
     * default DragonConsole Color Codes (although they will still be
     * processed out of any String passed through append().
     * @param useANSIColorCodes <code>true</code> to use ANSIStyle Color Codes, or <code>false</code> to use DragonConsole Color Codes.
     */
    public void setUseANSIColorCodes(boolean useANSIColorCodes) {
        this.useANSIColorCodes = useANSIColorCodes;
    }

    /** Sets the Prompt for the Input Area (when not using Inline Input) to the specified String.
     * Sets the prompt for the Input Area to the specified prompt String passed.
     * This method does nothing when using Inline Input since no Input Area is
     * displayed.<br /><br />
     * ** NOTE: Only DragonConsole color codes can be used to style the Prompt
     * **
     * @param newPrompt The new prompt for the input area.
     */
    public void setPrompt(String newPrompt) {
        consolePrompt.setPrompt(newPrompt);
    }

    /** Prints the logo for DragonConsole if <code>printDefaultMessage</code> is <code>true</code>
     * Prints the DragonConsole logo that is saved in logo_b or lobo_w in the
     * .jar depending on if the background color is White or not White.
     * (logo_w is for White backgrounds).
     * if <code>printDefaultMessage</code> is set to <code>true</code>.
     */
    private void printDefault() {
        if (printDefaultMessage) {
            try {
                String color = "b";
                if (consolePane.getBackground().equals(Color.WHITE))
                    color = "w";

                append(FileProcessor.readDCResource("logo_" + color));
                
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
        addTextColor('l', new Color(66, 66, 255)); // Blue
        addTextColor('L', new Color(0, 0, 174)); // Dark Blue
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
        addTextColor('m', Color.MAGENTA); // Magenta
        addTextColor('M', Color.MAGENTA.darker()); // Dark Magenta

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

    /** Tells the DragonConsole whether to ignore input from the user or not.
     * This method can be used to ignore input scripts passed to the console
     * via append() and ignore any input from the user.
     * @param ignoreInput true if Input should be disabled, or false if Input should be enabled.
     */
    public void setIgnoreInput(boolean ignoreInput) {
        this.ignoreInput = ignoreInput;
        this.inputControl.setIgnoreInput(ignoreInput);
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

    /** Tells the Console whether to save input if interrupted.
     * This method tells the Console if it should carry over input if the user
     * is interrupted by a message while typing or not. Input will only be
     * carried over if the previous input and the current input match (have the
     * same parameters) otherwise the previous input will be lost.
     * @param inputCarryOver <code>true</code> to carry input over <code>false</code> to ignore it.
     */
    public void setInputCarryOver(boolean inputCarryOver) {
        this.inputCarryOver = inputCarryOver;
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

    /** This method should be used to interface with the ANSI class for converting colors.
     * This method will convert the DragonConsole Color Codes in the passed
     * String into their ANSI equivalent. This method should be used in place of
     * calling the ANSI class directly.
     * @param toConvert The String that needs to be converted.
     * @return The String after the DragonConsole Codes have been converted to ANSI Codes.
     */
    public String convertToANSIColors(String toConvert) {
        return ANSI.convertDCtoANSIColors(toConvert, textColors, colorCodeChar);
    }

    /** This method should be used to interface with the ANSI class for converting colors.
     * This method will convert ANSI Color Codes into their DragonConsole Color
     * Code equivalent. This method should be used in place of calling the ANSI
     * class directly.
     * @param toConvert The String that needs to be converted.
     * @return The String after the ANSI Codes have been converted to DragonConsole Codes.
     */
    public String convertToDCColors(String toConvert) {
        return ANSI.convertANSIToDCColors(toConvert, textColors, colorCodeChar, defaultColor);
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
        if (!ignoreInput && inputCarryOver && inputControl.isReceivingInput()) {
            inputControl.storeInput();
        }

        boolean hasInput = false;
        String processed = "";

        for (int i = 0; i < outputToProcess.length(); i++) {
            if (outputToProcess.charAt(i) == colorCodeChar) {
                if ( ((i + 1) < outputToProcess.length()) &&
                        (outputToProcess.charAt(i + 1) == colorCodeChar)) {
                    processed += colorCodeChar;
                    i += 1; // Jump past the - (&&)

                } else if ((i + 2) < outputToProcess.length()) {
                    print(processed);
                    processed = "";

                    setCurrentStyle(outputToProcess.substring(i + 1, i + 3));

                    i += 2; // Jump past the two character color code

                } else
                    processed += outputToProcess.charAt(i);

            } else if (outputToProcess.charAt(i) == '\033') {
                if (outputToProcess.indexOf('m', i) < outputToProcess.length()) {
                    print(processed);
                    processed = "";

                    ANSIStyle = ANSI.getANSIAttribute(ANSIStyle,
                            outputToProcess.substring(i,
                                outputToProcess.indexOf('m', i) + 1),
                            consoleStyledDocument.getStyle(defaultColor));

                    i = outputToProcess.indexOf('m', i);
                }
            } else if (outputToProcess.charAt(i) == '%' && !ignoreInput) {

                if ((i + 1) < outputToProcess.length() &&
                        outputToProcess.charAt(i + 1) == '%') {
                    processed += "%";
                    i += 1; // Jump past the "%%"

                } else if (outputToProcess.indexOf(';', i) > i) {
                    if (outputToProcess.charAt(i + 1) == 'i') {
                        hasInput = true;
                        String inputCommand = outputToProcess.substring(i, outputToProcess.indexOf(';', i) + 1);

                        if (inputControl.setInputStyle(inputCommand)) {
                            print(processed);
                            processed = "";

                            inputControl.setRangeStart(consoleStyledDocument.getLength());
                            print(inputControl.getInputRangeString(), defaultColor); // Print the blank space if the input is not infinite

                        } else {
                            outputToProcess = ""; // Clear out the output to process if input is infinite, which means anything after the input string is ignored.
                            
                            print(processed);
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

        print(processed);

        if (!(hasInput))
            inputControl.setBasicInput(consoleStyledDocument.getLength());

        setConsoleCaretPosition();
    }

    /** Helper method for append that sets the current style variable based on the code passed.
     * This method processes a color code passed from append and sets the
     * <code>currentStyle</code> variable accordingly.
     * @param code The new color code by which to set <code>currentStyle</code>.
     */
    private void setCurrentStyle(String code) {
        String newStyle = "";
        if (code.length() == 2) {
            if (code.contains("0")) {
                currentStyle = defaultColor;
            } else if (code.contains("-")) {
                if (!(code.equals("--"))) {
                    if (code.charAt(0) == '-')
                        currentStyle = "" + currentStyle.charAt(0) + code.charAt(1);
                    else
                        currentStyle = "" + code.charAt(0) + currentStyle.charAt(1);
                }
            } else
                currentStyle = code;
        }
    }

    /** Helper method for append() which prints the text with the style depending on if ANSIStyle is on or not.
     * This is a helper method for append() which prints the current text out
     * depending on which color code method is set (ANSIStyle or DCCodes). This
     * method was added to cut back on code.
     * @param processed The current String of processed text in append().
     */
    private void print(String processed) {
        if (processed.length() > 0) {
            if (useANSIColorCodes && ANSIStyle != null)
                print(processed, ANSIStyle);
            else
                print(processed, currentStyle);
        }
    }

    /** Processes the String for DCScript if a DCScript Character is found in append()
     * This method pulls out and processes a piece of DCScript is DCScript is
     * found in append().
     * @param output The output that contains a piece of DCScript and needs to be processed.
     * @param index The current index being used to process the String, needed to work within output.
     * @return The index in which to continue processing the passed String.
     */
    private int processDCScript(String output, String processed, String style, int index) {
        int modifier = 0;



        return index + modifier;
    }

    /** Sets the caret position in the ouputPane according to the input controller.
     * Sets the caret position to the start of the current input, if not
     * currently receiving input then it sets the caret position to the end of
     * the document.
     */
    private void setConsoleCaretPosition() {
        int caretLocation = inputControl.getInputRangeStart();
        if (!ignoreInput) {
            if (caretLocation > -1)
                consolePane.setCaretPosition(caretLocation);
            else
                consolePane.setCaretPosition(consoleStyledDocument.getLength());

            if (inputCarryOver && inputControl.hasStoredInput())
                inputControl.restoreInput();

            if (alwaysKeepScrollBarMaxed || !alwaysKeepScrollBarMaxed && isScrollBarAtMax)
                setScrollBarMax();
        }
        else {
            consolePane.setCaretPosition(0);
        }

    }

    private void setScrollBarMax() {
        final JScrollBar vBar = consoleScrollPane.getVerticalScrollBar();
        if (isScrollBarAtMax) {
            new Thread() {
                public void run() {
                    try {
                        sleep(100); // Time out for a tenth of a second
                        if (vBar.isVisible())
                            vBar.setValue(vBar.getMaximum());
                    } catch (Exception exc) { }
                }
            }.start();
        }
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
            ignoreAdjustment = true;
            
            if (useInlineInput)
                output = inputControl.getBypassPrefix() + output;

            consoleStyledDocument.insertString(
                    consoleStyledDocument.getLength(), output,
                    consoleStyledDocument.getStyle(style));
        }
        catch (BadLocationException e) { }
    }

    /** Prints a String in the Document with the given AttributeSet.
     * This method adds a string to consolePane's StyledDocument allowing the
     * text to be styled by the predefined color styles.
     * @param output The text string to add to the Console.
     * @param style The color code for this text String.
     */
    protected void print(String output, SimpleAttributeSet style) {
        try { // Try to add the colored string to the output area document
            ignoreAdjustment = true;

            if (useInlineInput)
                output = inputControl.getBypassPrefix() + output;

            consoleStyledDocument.insertString(
                    consoleStyledDocument.getLength(), output, style);
        }
        catch (BadLocationException e) { }
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
            if (alwaysKeepScrollBarMaxed || (!alwaysKeepScrollBarMaxed && isScrollBarAtMax)) {
                JScrollBar vBar = consoleScrollPane.getVerticalScrollBar();
                ignoreAdjustment = true;
                if (vBar.isVisible())
                    vBar.setValue(vBar.getMaximum() - vBar.getModel().getExtent());
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

        if (!ignoreInput) {
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

    public void adjustmentValueChanged(AdjustmentEvent e) {
        JScrollBar sBar = (JScrollBar)e.getSource();
        
        if (!ignoreAdjustment) {
            int value = e.getValue();
            int maxValue = sBar.getMaximum() - sBar.getModel().getExtent();

            if (value == maxValue)
                isScrollBarAtMax = true;
            else if (value < maxValue)
                isScrollBarAtMax = false;
        } else
            ignoreAdjustment = false;
    }
}