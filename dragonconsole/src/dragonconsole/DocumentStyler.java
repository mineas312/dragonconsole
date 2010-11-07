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
import java.awt.*;
import javax.swing.text.*;

/** Adds and updates the static styles as well as handling new style creation.
 * Adds the static Quake 3 Color styles as well as the "default" text color
 * style and a "command" color style for the purpose of displaying commands
 * entered in a different color.
 * This class also contains the methods to allow the programmer to Dynamically
 * create a new style and/or update a specific style at runtime.
 * @author Brandon E Buck
 * @version 1.2
 */
public class DocumentStyler {

    /** *** This Method should only be called from the constructor ***
     * This Method should only be called from the constructor as it creates
     * each of the built in styles and adds them to the document. There is an
     * updateStyles that will update the built in styles which should be used
     * when the Foreground Color, Background Color, or Font change.
     * @param documentToStyle The ConsoleTextPane's StyledDocument the styles will be stored in.
     * @param consoleFont The ConsoleTextPane's Font to print the styles in.
     * @param defaultForeground The ConsoleTextPane's Foreground color for the purpose of setting the default style.
     * @param defaultBackground The ConsoleTextPane's Background color so that each style has the same background as the console.
     * @return The modified StyledDocument, to which all printing is done.
     */
    public static StyledDocument styleDocument(StyledDocument documentToStyle,
            Font consoleFont) {

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.RED, "r", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.BLUE, "l", consoleFont);
        
        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.GREEN, "g", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.YELLOW, "y", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.WHITE, "w", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.GRAY.brighter(), "x", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.CYAN, "c", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.BLACK, "b", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.RED.darker(), "R", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.BLUE.darker(), "L", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.GREEN.darker(), "G", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.YELLOW.darker(), "Y", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.GRAY, "X", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.CYAN.darker(), "C", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                Color.ORANGE, "o", consoleFont);

        // Purple
        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                new Color(128, 0, 255), "p", consoleFont);

        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                new Color(64, 0, 128), "P", consoleFont);

        // Gold
        documentToStyle = DocumentStyler.addNewColor(documentToStyle,
                new Color(241, 234, 139), "d", consoleFont);

        return documentToStyle;
    }

     /** Used at Runtime to add a new style to the StyledDocument for use.
     * This method is used by styleDocument() and by at runtime to add styles
     * to the StyledDocument. <strong>Be warned that any styles added at
     * runtime (i.e. those that are not built in to this class) MUST be updated
     * manually if any changes occur to font/background color on the Console.
     * </strong>
     * @param documentToUpdate The StyledDocument the new Style will be added to.
     * @param styleName The name you want to give the style, used to access it for updating or printing.
     * @param styleFont The Font you want to set for this style, to go with default pass the getFont() from the console.
     * @param styleBackground This can be set to the default background color (getBackground()) or you can change it to highlight the text.
     * @return The StyledDocument after the changes have been made to it.
     */
    public static StyledDocument addNewStyle(StyledDocument documentToUpdate,
            String styleName, Font styleFont, Color styleForeground) {
        Style parentStyle = StyleContext.getDefaultStyleContext()
                .getStyle(StyleContext.DEFAULT_STYLE);

        Style temp = documentToUpdate.addStyle(styleName, parentStyle);
        setStyleFont(temp, styleFont);
        StyleConstants.setForeground(temp, styleForeground);
    
        return documentToUpdate;
    }

    /** Used at Runtime to add a new style to the StyledDocument for use.
     * This method is used by styleDocument() and by at runtime to add styles
     * to the StyledDocument. Unlike other styles, this style is added with a
     * background color which will "highlight" the text. <strong>Be warned that
     * any styles added at runtime (i.e. those that are not built in to this
     * class) MUST be updated manually if any changes occur to font/background
     * color on the Console.</strong>
     * @param documentToUpdate The StyledDocument the new Style will be added to.
     * @param styleName The name you want to give the style, used to access it for updating or printing.
     * @param styleFont The Font you want to set for this style, to go with default pass the getFont() from the console.
     * @param styleForeground The text color for the style.
     * @param styleBackground This can be set to the default background color (getBackground()) or you can change it to highlight the text.
     * @return The StyledDocument after the changes have been made to it.
     */
    public static StyledDocument addNewStyle(StyledDocument documentToUpdate,
            String styleName, Font styleFont, Color styleForeground,
            Color styleBackground) {
        Style parentStyle = StyleContext.getDefaultStyleContext()
                .getStyle(StyleContext.DEFAULT_STYLE);

        Style temp = documentToUpdate.addStyle(styleName, parentStyle);
        setStyleFont(temp, styleFont);
        StyleConstants.setForeground(temp, styleForeground);
        StyleConstants.setBackground(temp, styleBackground);

        return documentToUpdate;
    }

    /** Sets the Font options for a style so that it prints in newFont
     * This method is a helper, it sets the styles Font Family, Font Size, and
     * whether or not the Font is Bold and/or Italic.
     * @param style The Style object in which the Font information will be stored.
     * @param newFont The newFont that needs to be set to the Style object.
     */
    private static void setStyleFont(Style style, Font newFont) {
        StyleConstants.setFontFamily(style, newFont.getFamily());
        StyleConstants.setFontSize(style, newFont.getSize());
        StyleConstants.setBold(style, newFont.isBold());
        StyleConstants.setItalic(style, newFont.isItalic());
    }

    /** Adds a new foreground color for the text with all the default background colors.
     * Adds a new foreground with the corresponding color code with each default
     * background color to the StyledDocument so the rich colors can be seen when
     * added to the console with the proper colors.
     * @param documentToUpdate The StyledDocument the color styles need to be added to.
     * @param foregroundColor The color of the text for the new styles.
     * @param colorCode The single digit color code that will be given for this foreground.
     * @param consoleFont The font that console is using.
     * @return Returns the StyledDocument after the styles have been added to it.
     */
    public static StyledDocument addNewColor(
            StyledDocument documentToUpdate, Color foregroundColor,
            String colorCode, Font consoleFont) {

        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "b", consoleFont,
                foregroundColor, Color.BLACK);
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "r", consoleFont,
                foregroundColor, Color.RED);
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "R", consoleFont,
                foregroundColor, Color.RED.darker());
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "l", consoleFont,
                foregroundColor, Color.BLUE);
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "L", consoleFont,
                foregroundColor, Color.BLUE.darker());
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "g", consoleFont,
                foregroundColor, Color.GREEN);
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "G", consoleFont,
                foregroundColor, Color.GREEN.darker());
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "y", consoleFont,
                foregroundColor, Color.YELLOW);
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "Y", consoleFont,
                foregroundColor, Color.YELLOW.darker());
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "c", consoleFont,
                foregroundColor, Color.CYAN);
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "C", consoleFont,
                foregroundColor, Color.CYAN.darker());
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "x", consoleFont,
                foregroundColor, Color.GRAY.brighter());
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "X", consoleFont,
                foregroundColor, Color.GRAY);
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "w", consoleFont,
                foregroundColor, Color.WHITE);

        // Purple
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "p", consoleFont,
                foregroundColor, new Color(128, 0, 255));
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "P", consoleFont,
                foregroundColor, new Color(64, 0, 128));

        // Gold
        documentToUpdate = DocumentStyler.addNewStyle(documentToUpdate, colorCode + "d", consoleFont,
                foregroundColor, new Color(241, 234, 139));
        
        return documentToUpdate;
    }
}