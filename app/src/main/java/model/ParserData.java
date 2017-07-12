package model;

import java.util.ArrayList;

/**
 * Created by zigin on 11.07.2017.
 */

public class ParserData {
    private int type;
    private String text;

    public ParserData(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
