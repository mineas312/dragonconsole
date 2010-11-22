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

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/** Static class that will create a SimpleAttributeSet with the ANSI Code specifics.
 * ANSI is a helper class that will process and create a
 * SimpleAttributeSet that has the color settings from a given String and
 * return it to the calling function. This is used to allow the DragonConsole
 * compatability with ANSI.
 *
 * This method will also convert DragonConsole color codes into ANSI compatible
 * codes (although if no compatible color can be found it will use the default
 * colors.
 * @author Brandon E Buck
 */
public class ANSI {
    public static final String ESCAPE = "\033"; // ANSI Escape Character that starts commands

    private static final Color N_0 = Color.BLACK;           // Normal Black
    private static final Color N_1 = Color.RED.darker();    // Normal Red
    private static final Color N_2 = Color.GREEN.darker();  // Normal Green
    private static final Color N_3 = Color.YELLOW.darker(); // Normal Yellow
    private static final Color N_4 = new Color(0, 0, 174);  // Normal Blue
    private static final Color N_5 = Color.MAGENTA.darker();// Normal Magenta
    private static final Color N_6 = Color.CYAN.darker();   // Normal Cyan
    private static final Color N_7 = Color.GRAY.brighter(); // Normal Gray

    private static final Color B_0 = Color.GRAY;            // Bright Black
    private static final Color B_1 = Color.RED;             // Bright Red
    private static final Color B_2 = Color.GREEN;           // Bright Green
    private static final Color B_3 = Color.YELLOW;          // Bright Yellow
    private static final Color B_4 = new Color(66, 66, 255);// Bright Blue
    private static final Color B_5 = Color.MAGENTA;         // Bright Magenta
    private static final Color B_6 = Color.CYAN;            // Bright Cyan
    private static final Color B_7 = Color.WHITE;           // Bright Gray

    private static final Color normal[] = {N_0, N_1, N_2, N_3, N_4, N_5, N_6, N_7};
    private static final Color bright[] = {B_0, B_1, B_2, B_3, B_4, B_5, B_6, B_7};

    public static SimpleAttributeSet getANSIAttribute(SimpleAttributeSet old, String string, Style defaultStyle) {
        SimpleAttributeSet ANSI = old;

        if (ANSI == null)
            ANSI = new SimpleAttributeSet();
        
        if (string.length() > 3) {
            string = string.substring(2); // Cut off the "\033[";
            string = string.substring(0, string.length() - 1); // Remove the "m" from the end

        } else
            return null;

        String codes[] = string.split(";");

        boolean brighter = false;
        for (int i = 0; i < codes.length; i++) {

            if (codes[i].matches("[\\d]*")) {
                int code = Integer.parseInt(codes[i]);

                switch (code) {
                    case 0:
                        brighter = false;
                        ANSI = new SimpleAttributeSet();
                        break;
                    case 1:
                        brighter = true;
                        break;
                    case 30:
                        if (brighter)
                            StyleConstants.setForeground(ANSI, B_0);
                        else
                            StyleConstants.setForeground(ANSI, N_0);
                        break;
                    case 31:
                        if (brighter)
                            StyleConstants.setForeground(ANSI, B_1);
                        else
                            StyleConstants.setForeground(ANSI, N_1);
                        break;
                    case 32:
                        if (brighter)
                            StyleConstants.setForeground(ANSI, B_2);
                        else
                            StyleConstants.setForeground(ANSI, N_2);
                        break;
                    case 33:
                        if (brighter)
                            StyleConstants.setForeground(ANSI, B_3);
                        else
                            StyleConstants.setForeground(ANSI, N_3);
                        break;
                    case 34:
                        if (brighter)
                            StyleConstants.setForeground(ANSI, B_4);
                        else
                            StyleConstants.setForeground(ANSI, N_4);
                        break;
                    case 35:
                        if (brighter)
                            StyleConstants.setForeground(ANSI, B_5);
                        else
                            StyleConstants.setForeground(ANSI, N_5);
                        break;
                    case 36:
                        if (brighter)
                            StyleConstants.setForeground(ANSI, B_6);
                        else
                            StyleConstants.setForeground(ANSI, N_6);
                        break;
                    case 37:
                        if (brighter)
                            StyleConstants.setForeground(ANSI, B_7);
                        else
                            StyleConstants.setForeground(ANSI, N_7);
                        break;
                    case 39:
                        StyleConstants.setForeground(ANSI, StyleConstants.getForeground(defaultStyle));
                    case 40:
                        StyleConstants.setBackground(ANSI, N_0);
                        break;
                    case 41:
                        StyleConstants.setBackground(ANSI, N_1);
                        break;
                    case 42:
                        StyleConstants.setBackground(ANSI, N_2);
                        break;
                    case 43:
                        StyleConstants.setBackground(ANSI, N_3);
                        break;
                    case 44:
                        StyleConstants.setBackground(ANSI, N_4);
                        break;
                    case 45:
                        StyleConstants.setBackground(ANSI, N_5);
                        break;
                    case 46:
                        StyleConstants.setBackground(ANSI, N_6);
                        break;
                    case 47:
                        StyleConstants.setBackground(ANSI, N_7);
                        break;
                    case 49:
                        StyleConstants.setBackground(ANSI, StyleConstants.getBackground(defaultStyle));
                        break;
                }
            }
        }

        return ANSI;
    }

    public static String getANSICodeFromDCCode(String DCCode, ArrayList<TextColor> colors) {
        if (DCCode.length() == 3) {
            DCCode = DCCode.substring(1); // Remove the & from the code
            char foreground = DCCode.charAt(0);
            char background = DCCode.charAt(1);
            
            String code = ESCAPE + "[";

            if (foreground == '0')
                code += "0";
            else if (foreground == '-')
                code += "";
            else {
                for (int i = 0; i < colors.size(); i++) {
                    TextColor tc = null;
                    if (colors.get(i).equals(TextColor.getTestTextColor(foreground))) {
                        tc = colors.get(i);
                        for (int x = 0; x < normal.length; x++) {
                            if (tc.getColor().equals(normal[x])) {
                                code += "3" + x;
                                break; // Found, no need to continue

                            } else if (tc.getColor().equals(bright[x])) {
                                code += "1;3" + x;
                                break; // Found, no need to continue
                            }
                        }
                        break; // Found, no need to continue
                    }
                }
            }

            if (code.charAt(code.length() - 1) != '[')
                code += ";";

            if (background == '0')
                code += "0";
            else if (background == '-')
                code += "";
            else {
                for (int i = 0; i < colors.size(); i++) {
                    TextColor tc = null;
                    if (colors.get(i).equals(TextColor.getTestTextColor(background))) {
                        tc = colors.get(i);
                        for (int x = 0; x < normal.length; x++) {
                            if (tc.getColor() == normal[x]) {
                                code += "4" + x;
                                break; // Found, no need to continue

                            } else if (tc.getColor().equals(bright[x])) {
                                code += "4" + x;
                                break; // Found, no need to continue
                            }
                        }
                        break; // Found, no need to continue
                    }
                }
            }

            if (code.charAt(code.length() - 1) == ';')
                code = code.substring(0, code.length() - 1);

            if (code.equals(ESCAPE + "["))
                return "";
            else
                return code + "m";
        }

        return ESCAPE + "[39;49m";
    }

    public static String convertDCtoANSIColors(String string, ArrayList<TextColor> colors, char colorCodeChar) {
        StringBuilder buffer = new StringBuilder(string);

        for (int i = 0; i < buffer.length(); i++) {
            if (buffer.charAt(i) == colorCodeChar) {
                if ((i + 1) < buffer.length() && (buffer.charAt(i + 1) == colorCodeChar)) {
                    i += 1; // Jump past the &&

                } else {
                    String code = buffer.substring(i, (i + 3));
                    
                    code = getANSICodeFromDCCode(code, colors);
                    int length = code.length();
                    
                    buffer = buffer.replace(i, (i + 3), code);
                    i = i + length - 1;
                }
            }
        }

        return buffer.toString();
    }

    private static String getDCCodeFromANSICode(String code, ArrayList<TextColor> colors, char colorCodeChar, String defaultStyle) {
        boolean brighter = false;
        code = code.substring(2, code.length()); // Cut off the "\033[" and "m"
        char foreground = ' ';
        char background = ' ';
        
        String colorCodes[] = code.split(";");
        for (int i = 0; i < colorCodes.length; i++) {
            if (colorCodes[i].matches("[\\d]*")) {
                int colorCode = Integer.parseInt(colorCodes[i]);
                if (colorCode == 0)
                    brighter = false;
                else if (colorCode == 1)
                    brighter = true;
                else if (colorCode >= 30 && colorCode <= 39) {
                    if (colorCode == 39)
                        foreground = defaultStyle.charAt(0);
                    else if (colorCode >= 30 && colorCode <= 37) {
                        foreground = getDCCharCodeFromColor(getColorFromANSICode(colorCode, brighter), colors);
                        brighter = false;
                    }
                } else if (colorCode >= 40 && colorCode <= 49) {
                    if (colorCode == 49)
                        background = defaultStyle.charAt(1);
                    else if (colorCode >= 40 && colorCode <= 47)
                        background = getDCCharCodeFromColor(getColorFromANSICode(colorCode, false), colors);
                }
            }
        }

        if (foreground == ' ')
            foreground = defaultStyle.charAt(0);
        if (background == ' ')
            background = defaultStyle.charAt(1);
        
        return "" + colorCodeChar + foreground + background;
    }

    public static char getDCCharCodeFromColor(Color color, ArrayList<TextColor> colors) {
        TextColor test = TextColor.getTestTextColor(color);

        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).equals(test)) {
                return colors.get(i).getCharCode();
            }
        }

        return ' ';
    }

    public static String convertANSIToDCColors(String string, ArrayList<TextColor> colors, char colorCodeChar, String defaultStyle) {
        StringBuilder buffer = new StringBuilder(string);

        for (int i = 0; i < buffer.length(); i++) {
            if (buffer.charAt(i) == '\033') {
                if (buffer.indexOf("m", i) < buffer.length()) {
                    String code = buffer.substring(i, buffer.indexOf("m", i)); // The Color Code
                    int end = i + code.length() + 1;
                    code = getDCCodeFromANSICode(code, colors, colorCodeChar, defaultStyle);
                    
                    buffer = buffer.replace(i, end, code);
                    i = i + 2;
                }
            }
        }

        return buffer.toString();
    }

    private static Color getColorFromANSICode(int code, boolean brighter) {
        switch (code) {
            case 30:
            case 40:
                if (brighter)
                    return B_0;
                else
                    return N_0;
            case 31:
            case 41:
                if (brighter)
                    return B_1;
                else
                    return N_1;
            case 32:
            case 42:
                if (brighter)
                    return B_2;
                else
                    return N_2;
            case 33:
            case 43:
                if (brighter)
                    return B_3;
                else
                    return N_3;
            case 34:
            case 44:
                if (brighter)
                    return B_4;
                else
                    return N_4;
            case 35:
            case 45:
                if (brighter)
                    return B_5;
                else
                    return N_5;
            case 36:
            case 46:
                if (brighter)
                    return B_6;
                else
                    return N_6;
            case 37:
            case 38:
                if (brighter)
                    return B_7;
                else
                    return N_7;
            default:
                return null;
        }
    }
}
