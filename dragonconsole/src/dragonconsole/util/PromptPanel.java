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

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Brandon E Buck
 */
public class PromptPanel extends JPanel {
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
