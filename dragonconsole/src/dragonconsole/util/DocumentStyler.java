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
import java.awt.*;
import javax.swing.text.*;
import java.util.ArrayList;
import dragonconsole.util.TextColor;

/** Adds and updates the static styles as well as handling new style creation.
 * This class is designed to control and make easier the addition of text
 * styles to the StyledDocument of the Consoles JTextPane component.
 * The DocumentStyler is a static class with numerous helper methods for adding
 * styles, however it should never be used outside of the DragonConsole class
 * or any extension of this class (unless you are using it outside of the
 * DragonConsole implementation) because the DocumentStyler was written with a
 * specific use in mind and not as a general utility.
 * @author Brandon E Buck
 * @version 1.3
 */
public class DocumentStyler {
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

    /** Adds a new Style to the StyledDocument based on the two TextColor objects passed.
     * Uses the char codes from each TextColor object passed to create a Style
     * name and then adds the Style to the StyledDocument with the appropriate
     * foreground and background colors supplied by the TextColor objects.
     * @param documentToUpdate The StyledDocument the colors will be added to.
     * @param styleFont The font for each style to use.
     * @param foreground The TextColor that represents the foreground color.
     * @param background The TextColor that represents the background color.
     * @return The altered StyledDocument with the new Style.
     */
    private static StyledDocument addNewStyle(StyledDocument documentToUpdate,
            Font styleFont, TextColor foreground, TextColor background) {
        String styleName = "" + foreground.getCharCode() + background.getCharCode();

        Style parentStyle = StyleContext.getDefaultStyleContext()
                .getStyle(StyleContext.DEFAULT_STYLE);

        Style temp = documentToUpdate.addStyle(styleName, parentStyle);
        setStyleFont(temp, styleFont);
        StyleConstants.setForeground(temp, foreground.getColor());
        StyleConstants.setBackground(temp, background.getColor());

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

    /**  Adds a new color to the StyledDocuemnt with every available background color.
     * This methods adds the new color as a foreground color for all
     * available background colors as well as a background for all available
     * foreground colors. This method should never be called directly, instead
     * it should be indirectly called through <code>DragonConsoles</code>
     * <code>addTextColor(char, Color)</code> method.
     * @param documentToUpdate The StyledDocument the new Color is to be added to.
     * @param consoleFont The Font these styles will use.
     * @param newColor The TextColor that needs to be added as a foreground and background.
     * @param textColors The list of colors already in the Console, used so the newColor can have each background and foreground of previously added colors.
     * @return The modified StyledDocument.
     */
    public static StyledDocument addNewColor(StyledDocument documentToUpdate,
            Font consoleFont, TextColor newColor,
            ArrayList<TextColor> textColors) {
        for (int i = 0; i < textColors.size(); i++) {
            TextColor tc = textColors.get(i);
            documentToUpdate = addNewStyle(documentToUpdate, consoleFont, newColor, tc); // Give the New Color every background
            documentToUpdate = addNewStyle(documentToUpdate, consoleFont, tc, newColor); // Give Every Color the new background
        }

        return documentToUpdate;
    }
}