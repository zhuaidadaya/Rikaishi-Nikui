package com.github.zhuaidadaya.rikaishinikui.ui.panel;

import javax.swing.*;
import java.awt.*;

public class RikaishiNikuiScrollPanel extends JScrollPane {
    public RikaishiNikuiScrollPanel(Component component) {
        super(component);
        setSidebarColor();
    }

    public void setSidebarColor() {
        JScrollBar bar = new JScrollBar();
        bar.setBounds(0,0,10,getHeight());
        bar.setEnabled(true);
        bar.setUnitIncrement(50);
        setVerticalScrollBar(bar);
        repaint();
    }
}
