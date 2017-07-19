package model;

import java.io.Serializable;

/**
 * Created by zigin on 20.09.2016.
 */
public class Topics implements Serializable {

    private static final long SerialVersionUID = 10L;
    public String text;
    public int parentId;
    public String labels;
    public int id;

    public Topics(String topicText, int parentId, String labels, int id) {
        this.text = topicText;
        this.parentId = parentId;
        this.labels = labels;
        this.id = id;
    }

    public Topics() {

    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
