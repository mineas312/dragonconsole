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

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 *
 * @author Brandon E Buck
 */
public class PromptPanel extends JPanel {
    private PromptLabel promptLabel;

    public PromptPanel(String prompt, String defaultColor) {
        promptLabel = new PromptLabel(prompt, defaultColor);
        promptLabel.setOpaque(false);
        setLayout(new BorderLayout());
        add(promptLabel, BorderLayout.NORTH);
    }

    public void setPrompt(String newPrompt) {
        promptLabel.setText(newPrompt);
    }

    public void setDefaultColor(String defaultColor) {
        promptLabel.setDefaultColor(defaultColor);
    }

    public void addColor(TextColor color) {
        promptLabel.addColor(color);
    }

    public void clearColors() {
        promptLabel.clearColors();
    }

    public void removeColor(TextColor color) {
        promptLabel.removeColor(color);
    }

    public void setColorCodeChar(char colorCodeChar) {
        promptLabel.setColorCodeChar(colorCodeChar);
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

    private class PromptLabel extends JLabel {
        private ArrayList<TextColor> colors;
        private char colorCodeChar = '&';
        private String defaultColor;

        public PromptLabel(String text, String defaultColor) {
            super(text);
            this.defaultColor= defaultColor;
            this.colors = new ArrayList<TextColor>();
        }

        public void addColor(TextColor color) {
            this.colors.add(color);
            this.repaint();
        }

        public void removeColor(TextColor color) {
            this.colors.remove(color);
            this.repaint();
        }

        public void setDefaultColor(String defaultColor) {
            this.defaultColor = defaultColor;
        }

        public void clearColors() {
            this.colors.clear();
        }

        public void setColorCodeChar(char colorCodeChar) {
            this.colorCodeChar = colorCodeChar;
        }

        @Override
        public void paintComponent(Graphics g) {
            String text = super.getText(); // Get text withot chopping codes off
            int startx = 0;

            String processed = "";
            String style = defaultColor;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == colorCodeChar) {
                    if ( ((i + 1) < text.length()) &&
                            (text.charAt(i + 1) == colorCodeChar)) {
                        processed += colorCodeChar;
                        i += 1; // Jump past the - (&&)

                    } else if ((i + 2) < text.length()) {
                        startx = paintProcessed(processed, style, g,
                                startx);
                        processed = "";

                        style = setCurrentStyle(text.substring(i + 1, i + 3), style);

                        i += 2; // Jump past the two character color code

                    } else
                        processed += text.charAt(i);
                } else
                    processed += text.charAt(i);
            }

            paintProcessed(processed, style, g, startx);
        }

        private int paintProcessed(String processed, String style, Graphics g, int x) {
            if (processed.length() > 0) {
                ((Graphics2D)g).setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


                Rectangle2D bounds = g.getFontMetrics().getStringBounds(processed, g);

                int w = (int)(bounds.getWidth());
                int h = (int)(bounds.getHeight());
                int newX = x + (int)(bounds.getWidth());
                int y = h - 3;

                g.setColor(getColorFromDCCC(style.charAt(1)));
                g.fillRect(x, 0, w, h);

                g.setColor(getColorFromDCCC(style.charAt(0)));
                g.drawString(processed, x, y);

                return newX;
            }

            return x;
        }

        /** Helper method for append that sets the current style variable based on the code passed.
         * This method processes a color code passed from append and sets the
         * <code>currentStyle</code> variable accordingly.
         * @param code The new color code by which to set <code>currentStyle</code>.
         */
        private String setCurrentStyle(String code, String currentStyle) {
            String oldStyle = currentStyle;
            currentStyle = "";
            String newStyle = "";
            if (code.length() == 2) {
                if (code.contains("0")) {
                    newStyle = defaultColor;
                } else if (code.contains("-")) {
                    if (!(code.equals("--"))) {
                        if (code.charAt(0) == '-')
                            newStyle = "" + oldStyle.charAt(0) + code.charAt(1);
                        else
                            newStyle = "" + code.charAt(0) + oldStyle.charAt(1);
                    }
                } else
                    newStyle = code;

                currentStyle = newStyle;

            } else
                currentStyle = oldStyle;
            
            return currentStyle;
        }

        private Color getColorFromDCCC(char code) {
            TextColor test = TextColor.getTestTextColor(code);
            for (int i = 0; i < colors.size(); i++) {
                if (colors.get(i).equals(test))
                    return colors.get(i).getColor();
            }

            return Color.GRAY.brighter();
        }

        @Override
        public String getText() {
            return trimDCCC(super.getText());
        }

        private String trimDCCC(String toTrim) {
            StringBuilder buffer = new StringBuilder(toTrim);
            for (int i = 0; i < buffer.length(); i++) {
                if (buffer.charAt(i) == colorCodeChar) {
                    if (buffer.charAt(i) == colorCodeChar) {
                        if ( ((i + 1) < buffer.length()) &&
                                (buffer.charAt(i + 1) == colorCodeChar)) {
                            buffer.replace((i + 1), (i + 2), "");
                            i += 1; // Jump past the - (&&)

                        } else if ((i + 2) < buffer.length()) {
                            buffer.replace(i, i + 3, "");
                        }
                    }
                }
            }

            return buffer.toString();
        }
    }
}
