package udalostna.salon;

import udalostna.gui.EventGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EventGUI::new);
    }

}
