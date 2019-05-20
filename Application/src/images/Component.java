package images;

import image.processing.ImageProcess;
import image.processing.ImageBasicTransformation;
import image.processing.ImageAdvanceTransformation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class Component extends JComponent {
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JButton button11;

    private BufferedImage image;
    private BufferedImage imageIm;
    private BufferedImage imageBa;
    private BufferedImage imageOp;
    private BufferedImage imageEr;
    private BufferedImage imageOr;

    private ImageBasicTransformation im;
    private ImageAdvanceTransformation im2;

    Component() {
        super();

        // create app
        JButton buttonBackground = new JButton("Load Background");
        JButton buttonImage = new JButton("Load Image");
        button1 = new JButton("Gray Background");
        button2 = new JButton("Gray Image");
        button3 = new JButton("Different");
        button4 = new JButton("Binarize");
        button5 = new JButton("Closing");
        button6 = new JButton("Opening");
        button7 = new JButton("Erode");
        button8 = new JButton("Reconstruct");
        button9 = new JButton("Ordfilt2");
        button10 = new JButton("Geodetic map");
        button11 = new JButton("Regionprops");

        buttonBackground.setBounds(10, 10, 150, 30);
        buttonImage.setBounds(10, 40, 150, 30);
        button1.setBounds(10, 100, 150, 30);
        button2.setBounds(10, 130, 150, 30);
        button3.setBounds(10, 160, 150, 30);
        button4.setBounds(10, 190, 150, 30);
        button5.setBounds(10, 220, 150, 30);
        button6.setBounds(10, 250, 150, 30);
        button7.setBounds(10, 280, 150, 30);
        button8.setBounds(10, 310, 150, 30);
        button9.setBounds(10, 360, 150, 30);
        button10.setBounds(10, 390, 150, 30);
        button11.setBounds(10, 420, 150, 30);
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        button5.setEnabled(false);
        button6.setEnabled(false);
        button7.setEnabled(false);
        button8.setEnabled(false);
        button9.setEnabled(false);
        button10.setEnabled(false);
        button11.setEnabled(false);
        add(buttonBackground);
        add(buttonImage);
        add(button1);
        add(button2);
        add(button3);
        add(button4);
        add(button5);
        add(button6);
        add(button7);
        add(button8);
        add(button9);
        add(button10);
        add(button11);
        //

        im = new ImageBasicTransformation();
        im2 = new ImageAdvanceTransformation();

        // add actions to buttons
        buttonBackground.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("src\\images"));
            int val = chooser.showOpenDialog(null);
            if (val != JFileChooser.APPROVE_OPTION)
                return;

            imageBa = ImageProcess.bufferedImage(chooser.getSelectedFile().getPath());
            image = imageBa;
            button1.setEnabled(true);
            button4.setEnabled(true);
            button5.setEnabled(true);
            button6.setEnabled(true);
            button7.setEnabled(true);
            button9.setEnabled(true);
            button10.setEnabled(true);

            repaint();
        });
        buttonImage.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("src\\images"));
            int val = chooser.showOpenDialog(null);
            if (val != JFileChooser.APPROVE_OPTION)
                return;

            imageIm = ImageProcess.bufferedImage(chooser.getSelectedFile().getPath());
            image = imageIm;
            button2.setEnabled(true);
            button4.setEnabled(true);
            button5.setEnabled(true);
            button6.setEnabled(true);
            button7.setEnabled(true);

            repaint();
        });

        AtomicInteger flag = new AtomicInteger();
        button1.addActionListener(e ->
        {
            imageBa = im.toGray(imageBa);
            image = imageBa;

            flag.incrementAndGet();
            if (flag.get() == 2)
                button3.setEnabled(true);
            button11.setEnabled(true);
            repaint();
        });
        button2.addActionListener(e ->
        {
            imageIm = im.toGray(imageIm);
            image = imageIm;

            flag.incrementAndGet();
            if (flag.get() == 2)
                button3.setEnabled(true);
            button11.setEnabled(true);
            repaint();
        });
        button3.addActionListener(e ->
        {
            image = im.differentiation(imageBa, imageIm);
            repaint();
        });
        button4.addActionListener(e ->
        {
            image = im.autoBinarize(image);
            button11.setEnabled(true);
            repaint();
        });
        button5.addActionListener(e ->
        {
            image = im.closing(image, 5);
            repaint();
        });
        button6.addActionListener(e ->
        {
            imageOp = im.opening(image, 3);
            image = imageOp;
            if (imageOp != null && imageEr != null)
                button8.setEnabled(true);
            repaint();
        });
        button7.addActionListener(e ->
        {
            imageEr = im.erosion(image, 10);
            image = imageEr;
            if (imageOp != null && imageEr != null)
                button8.setEnabled(true);
            repaint();
        });
        button8.addActionListener(e ->
        {
            image = im.reconstruct(imageOp, imageEr);
            repaint();
        });
        button9.addActionListener(e ->
        {
            imageOr = im2.ordfilt2(image,3,3,8);
            image = imageOr;
            repaint();
        });
        button10.addActionListener(e ->
        {
            image = im.autoBinarize(image);
            image = im.reverseBinarize(image);
            image = im2.geodistance(image, 0, 0);
            repaint();
        });
        button11.addActionListener(e ->
        {
            im2.regionprops(image);
            repaint();
        });
        //
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(179,9, 670, 9 );
        g.drawLine(670,9, 670, 450 );
        g.drawLine(670,450, 179, 450 );
        g.drawLine(179,450, 179, 9 );

        // draw actual image
        if (image != null)
            g.drawImage(image, 180, 10, 490, 440, this);
    }
}
