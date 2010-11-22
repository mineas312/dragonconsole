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

import javax.swing.JFrame;
import java.awt.*;

/**
 *
 * @author Brandon E Buck
 */
public class DragonConsoleFrame extends JFrame {
    private DragonConsole console;

    public DragonConsoleFrame(String title, DragonConsole console) {
        this.console = console;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(title);
        
        this.add(console);
        this.pack();

        this.centerWindow();
    }

    public DragonConsoleFrame() {
        this.console = new DragonConsole();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("DragonConsole " + console.getVersion());

        this.add(console);
        this.pack();

        this.centerWindow();
    }

    public DragonConsoleFrame(DragonConsole console) {
        this.console = console;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("DragonConsole " + console.getVersion());
        
        this.add(console);
        this.pack();

        this.centerWindow();
    }

    public DragonConsoleFrame(String title) {
        console = new DragonConsole();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(title);

        this.add(console);
        this.pack();

        this.centerWindow();
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

    private DragonConsole getConsole() {
        return console;
    }
}
