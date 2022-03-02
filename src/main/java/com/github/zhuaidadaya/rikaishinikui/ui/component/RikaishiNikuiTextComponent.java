package com.github.zhuaidadaya.rikaishinikui.ui.component;

import com.github.zhuaidadaya.rikaishinikui.language.MultipleText;
import com.github.zhuaidadaya.rikaishinikui.language.SingleText;
import com.github.zhuaidadaya.rikaishinikui.language.Text;

import javax.swing.text.*;
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
            if (text instanceof SingleText) {
                StyleContext sc = StyleContext.getDefaultStyleContext();
                AttributeSet asset;
                try {
                    asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, text.getAwtColor());
                } catch (Exception e) {
                    asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, getForeground());
                }
                if (clear)
                    doc.remove(0, doc.getLength());
                doc.insertString(doc.getLength(), text.getText(), asset);
            } else {
                if (text instanceof MultipleText) {
//                    if (clear)
//                        doc.remove(0, doc.getLength());
                    for (SingleText singleText : ((MultipleText) text).get())
                        appendText(singleText, false);
                }
            }
        } catch (Exception e) {

        }
    }

    default void clearText() {
    }
}
