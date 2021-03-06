package com.github.zhuaidadaya.rikaishinikui.ui.panel;

import com.github.zhuaidadaya.rikaishinikui.ui.button.RikaishiNikuiButton;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import org.json.JSONObject;

public class RikaishiNikuiVerticalButtonPanel extends RikaishiNikuiPanel implements RikaishiNikuiComponent {
    private final Int2ObjectRBTreeMap<RikaishiNikuiButton> buttonsQueue = new Int2ObjectRBTreeMap<>();
    private int buttonsHeight = 0;
    private int width = 0;
    private int height = 0;
    private int x = 0;
    private int y = 0;

    public RikaishiNikuiVerticalButtonPanel() {
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiVerticalButtonPanel(String name) {
        setName(name);
    }

    public RikaishiNikuiVerticalButtonPanel(int width, int height) {
        setSize(width, height);
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiVerticalButtonPanel(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public RikaishiNikuiButton getButton(int id) {
        return buttonsQueue.get(id);
    }

    public void cancelButtonsActive() {
        for(RikaishiNikuiButton button : buttonsQueue.values()) {
            button.setActive(false);
        }
    }

    public RikaishiNikuiButton getActiveButton() {
        for(RikaishiNikuiButton button : buttonsQueue.values()) {
            if(button.isActive())
                return button;
        }
        return null;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setSize(int width, int height) {
        setBounds(getX(), getY(), width, height);
        this.height = height;
        this.width = width;
    }

    public void setXY(int x, int y) {
        setBounds(x, y, getWidth(), getHeight());
        this.x = x;
        this.y = y;
    }

    public void applyButtons(RikaishiNikuiButton... buttons) {
        for(RikaishiNikuiButton button : buttons) {
            button.setSize(button.getWidth(), height);
            button.setBounds(0, 0, button.getWidth(), height);
            buttonsQueue.put(button.getId(), button);
        }
    }

    public void applyButtons(JSONObject json) {
        for(String name : json.keySet()) {
            JSONObject buttonJson = json.getJSONObject(name);
            applyButton(buttonsQueue.get(buttonJson.getInt("id")),buttonJson);
        }
    }

    public void applyButton(RikaishiNikuiButton button,JSONObject json) {
        button.apply(json);
    }

    public void rendingButtons(JSONObject json) {
        buttonsHeight = 0;
        for(RikaishiNikuiButton button : buttonsQueue.values()) {
            if(button.isVisible()) {
                button.apply(json.getJSONObject(button.getName()));
                button.setBounds(0, buttonsHeight, width, button.getHeight());
                button.formatText();
                buttonsHeight += button.getHeight();
                add(button);
            }
        }
    }

    public RikaishiNikuiVerticalButtonPanel setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
        return this;
    }

    public void apply(JSONObject json) {
            setXY(json.getInt("x"), json.getInt("y"));
            setSize(json.getInt("width"), json.getInt("height"));

            setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));

            applyButtons(json.getJSONObject("buttons"));
            rendingButtons(json.getJSONObject("buttons"));
            repaint();
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("x", x);
        json.put("y", y);
        json.put("width", width);
        json.put("height", height);
        json.put("background-color", RikaishiNikuiColor.parse(getBackground()).toJSONObject());

        JSONObject buttons = new JSONObject();
        for(RikaishiNikuiButton button : this.buttonsQueue.values()) {
            buttons.put(button.getName(), button.toJSONObject());
        }

        json.put("buttons", buttons);

        return json;
    }
}
