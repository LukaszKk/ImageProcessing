package images;

import javax.swing.*;
import java.awt.*;

public class Frame {
    Frame() {
        // create frame and add component
        JFrame frame = new JFrame("Image Converter");
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        frame.setSize(700, 500);
        frame.setLocation(screenWidth / 2 - 350,
                screenHeight / 2 - 250);

        JComponent component = new Component();

        frame.add(component);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
