package net.mormolhs.facebook.fbanalytics.ui;


import net.mormolhs.facebook.fbanalytics.resources.GlobalParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by toikonomakos on 3/16/14.
 */
public class MyTextArea extends JTextArea implements KeyListener {
    String textareainfo;

    public MyTextArea() {
        this.setPreferredSize(new Dimension(50, 30));
    }

    public String getTextareainfo() {
        return textareainfo.trim();
    }

    public void setTextareainfo(String textareainfo) {
        this.textareainfo = textareainfo;
        GlobalParameters.RESULT_SIZE = textareainfo;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        setTextareainfo(this.getText());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
