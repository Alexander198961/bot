package com.trading.gui;

import javax.swing.*;
import java.awt.*;

public class CommonForm {

    protected JPanel componentWithLabel(String labelText, JComponent textField, JComponent additionalComponent){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel jLabel = new JLabel(labelText);
        panel.add(jLabel);
        panel.add(textField);
        if(additionalComponent != null){
            panel.add(additionalComponent);
        }
        return panel;
    }
}
