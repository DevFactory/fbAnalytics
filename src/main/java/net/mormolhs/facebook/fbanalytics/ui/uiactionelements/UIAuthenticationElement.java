package net.mormolhs.facebook.fbanalytics.ui.uiactionelements;

import net.mormolhs.facebook.fbanalytics.resources.GlobalParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by toikonomakos on 3/20/14.
 */

public class UIAuthenticationElement extends JPanel implements ActionListener {
    protected JTextField textField;

    public UIAuthenticationElement(ActionEvent evt) {
        super(new GridBagLayout());

        textField = new JTextField(20);
        textField.addActionListener(this);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        this.actionPerformed(evt);
    }

    public void actionPerformed(ActionEvent evt) {
        GlobalParameters.ACCESS_TOKEN = textField.getText();
    }

}
