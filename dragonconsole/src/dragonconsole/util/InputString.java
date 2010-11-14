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

/**
 * DCString acts like a slightly modified String object class that includes
 * three methods not present in the default String class and those are
 * <code>insert(int location, String s)</code>,
 * <code>remove(int location, int length)</code>, and
 * <code>replace(int location, int length, String s)</code>.
 * These function similar to a Document.
 * @author Brandon E Buck
 * @version 1.0
 */
public class InputString {
    private String s;

    public InputString(String s) {
        this.s = s;
    }

    public void append(String s) {
        this.s += s;
        Debug.print("\"" + this.s + "\" - APPEND");
    }

    public void insert(int location, String s) {
        if (location <= this.s.length()) {
            String before = this.s.substring(0, location);
            String after = this.s.substring(location);
            this.set(before + s + after);
            Debug.print("\"" + this.s + "\" - INSERT");
        }
    }

    public void remove(int location, int length) {
        if (location < this.s.length() && (location + length) <= this.s.length()) {
            int end = location + length;
            String before = this.s.substring(0, location);
            String after = "";
            
            if ((location + length) < this.s.length())
                after = this.s.substring(end);

            set(before + after);

            Debug.print("\"" + this.s + "\" - REMOVE");
        }
    }

    public void rangeRemove(int location, int length) {
        if (location < this.s.length() && (location + length) <= this.s.length()) {
            int end = location + length;
            String before = this.s.substring(0, location);
            String after = "";

            if ((location + length) < this.s.length())
                after = this.s.substring(end);

            set(before + after + " ");

            Debug.print("\"" + this.s + "\" - RANGE REMOVE");
        }
    }

    public boolean rangeInsert(int location, String s) {
        if (location < this.s.length() && endIsEmpty()) {
            String before = this.s.substring(0, location);
            String after = this.s.substring(location, this.s.length() - 1);
            this.set(before + s + after);

            Debug.print("\"" + this.s + "\" - RANGE INSERT");
            return true;
        }

        return false;
    }

    public void replace(int location, int length, String s) {
        if (location < this.s.length() && (location + length) <= this.s.length()) {
            int end = location + length;
            String before = this.s.substring(0, location);
            String after = this.s.substring(end);
            set(before + s + after);

            Debug.print("\"" + this.s + "\" - REPLACE");
        } else
            append(s);
    }

    public void set(String s) {
        this.s = s;
    }

    public String get() {
        return this.s;
    }

    public boolean endIsEmpty() {
        return (this.s.charAt(length() - 1) == ' ');
    }

    public int length() {
        return this.s.length();
    }

    @Override
    public String toString() {
        return this.s;
    }
}
