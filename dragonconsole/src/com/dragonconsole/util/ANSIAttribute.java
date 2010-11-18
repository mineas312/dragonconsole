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

package com.dragonconsole.util;

import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/** SimpleAttributeSet that will receive and process a String and pull out ANSI colors.
 * ANSIAttribute is an extension of a SimpleAttributeSet that processes a given
 * String for ANSI Color codes, used in place of styles to allow the
 * DragonConsole to use ANSI Color Codes as well as the Default DragonConsole
 * color codes.
 * @author Brandon E Buck
 */
public class ANSIAttribute extends SimpleAttributeSet {
    private static final Color N_0 = Color.BLACK;
    private static final Color N_1 = Color.RED.darker();
    private static final Color N_2 = Color.GREEN.darker();
    private static final Color N_3 = Color.YELLOW.darker();
    private static final Color N_4 = Color.BLUE.darker();
    private static final Color N_5 = Color.MAGENTA.darker();
    private static final Color N_6 = Color.CYAN.darker();
    private static final Color N_7 = new Color(192, 192, 192);

    private static final Color B_0 = new Color(128, 128, 128);
    private static final Color B_1 = Color.RED;
    private static final Color B_2 = Color.GREEN;
    private static final Color B_3 = Color.YELLOW;
    private static final Color B_4 = Color.BLUE;
    private static final Color B_5 = Color.MAGENTA;
    private static final Color B_6 = Color.CYAN;
    private static final Color B_7 = Color.WHITE;
}
