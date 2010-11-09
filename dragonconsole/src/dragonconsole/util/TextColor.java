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

package dragonconsole.util;

import java.awt.Color;

/**
 *
 * @author Brandon E Buck
 */
public class TextColor {
    private char charCode;
    private Color color;

    public TextColor(char charCode, Color color) {
        this.charCode = charCode;
        this.color = color;
    }

    public TextColor(String charCode, Color color) {
        this(charCode.charAt(0), color);
    }

    public char getCharCode() {
        return charCode;
    }

    public Color getColor() {
        return color;
    }

    public int compareTo(Object o) {
        if (o instanceof Character) {
            Character c = new Character(charCode);
            return c.compareTo((Character)o);
        } else if (o instanceof TextColor) {
            Character c = new Character(charCode);
            Character otherC = new Character(((TextColor)o).getCharCode());
            return c.compareTo(otherC);
        }

        return 0;
    }

    public boolean equals(Object o) {
        if (o instanceof TextColor) {
            TextColor otc = (TextColor)o;
            return ((charCode == otc.getCharCode()) && (color.equals(otc.getColor())));

        } else
            return false;
    }
}
