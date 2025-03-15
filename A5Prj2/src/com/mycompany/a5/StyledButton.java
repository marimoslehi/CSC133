package com.mycompany.a5;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Font;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.charts.util.ColorUtil;

public class StyledButton extends Button {

    // Constructor to create a styled button with a label
    public StyledButton(String label) {
        super(label);  // Call the parent Button constructor with the label

        // Set the default style for all buttons (blue background and white text)
        Style defaultStyle = this.getAllStyles();
        defaultStyle.setPadding(4, 4, 10, 10);  // Smaller padding: top, bottom, left, right
        defaultStyle.setMargin(2, 2, 2, 2);     // Smaller margin: top, bottom, left, right
        defaultStyle.setBgColor(ColorUtil.rgb(0, 0, 255));  // Bright blue background
        defaultStyle.setBgTransparency(255);  // Fully opaque
        defaultStyle.setFgColor(ColorUtil.WHITE);  // White text
        defaultStyle.setBorder(Border.createLineBorder(3, ColorUtil.BLACK));  // Thicker white border
        defaultStyle.setAlignment(CENTER);  // Center the label

        // Set larger font size using Font.createSystemFont
        Font largeFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        defaultStyle.setFont(largeFont);

        // Optionally set preferred size for bigger buttons
        this.setPreferredH(200);  // Button height (adjust to make it larger or smaller)
        this.setPreferredW(400);  // Button width (adjust to make it larger or smaller)

        // Set the pressed style for when the button is pressed (darker blue background)
        Style pressedStyle = this.getPressedStyle();
        pressedStyle.setBgColor(ColorUtil.rgb(0, 0, 139));  // Darker blue when pressed
        pressedStyle.setFgColor(ColorUtil.WHITE);  // Still white text when pressed
        pressedStyle.setBgTransparency(255);  // Fully opaque
        pressedStyle.setBorder(Border.createLineBorder(3, ColorUtil.BLACK));  // Same border thickness
    }

    // Override the setCommand method to work with Codename One commands
    @Override
    public void setCommand(Command cmd) {
        super.setCommand(cmd);  // Use Codename One's internal command handling
    }
}
