package com.github.zhuaidadaya.rikaishinikui.ui.component;

import com.github.zhuaidadaya.rikaishinikui.handler.text.Text;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import java.awt.*;

public interface RikaishiNikuiTextComponent {
    String getText();

    void setText(Text text);

    void setCaretPosition(int position);

    Color getForeground();

    Color getBackground();

    Document getDoc();

    void setSuperDoc(Document doc);

    void setDoc(Document doc);

    default void updateText() {
        setSuperDoc(getDoc());
        setDoc(new DefaultStyledDocument());
    }

    default void appendText(Text text, boolean clear) {
        try {
            Document doc = getDoc();
            text.applyToDoc(doc, clear, getForeground());
        } catch (Exception e) {

        }
    }

    default void clearText() {
    }
}
