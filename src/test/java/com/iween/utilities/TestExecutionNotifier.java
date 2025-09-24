package com.iween.utilities;

import javax.swing.*;
import java.awt.*;

public class TestExecutionNotifier {

    public static void showExecutionPopup() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String methodName = stackTrace[2].getMethodName();  // Calling method

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setAlwaysOnTop(true);
            frame.setUndecorated(true);

            JLabel label = new JLabel("Method: " + methodName, JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setForeground(Color.BLACK);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            label.setPreferredSize(new Dimension(350, 85));

            frame.getContentPane().add(label);
            frame.pack();

            // Position at top center of the screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            int x = (screenSize.width - frame.getWidth()) / 2;
            int x=30;
            int y = 200; // Distance from top (adjust as needed)
            frame.setLocation(x, y);

            frame.setVisible(true);

            // Auto-close after 2.5 seconds
            new Timer(2500, e -> frame.dispose()).start();
        });

    }
}
