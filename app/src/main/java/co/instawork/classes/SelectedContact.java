package co.instawork.classes;

import java.io.Serializable;

/**
 * Created by ankitgoyal on 7/8/15.
 */
public class SelectedContact implements Serializable {
    private String name;
    private int type;
    private int position;

    public SelectedContact(String name, int type, int position) {
        this.name = name;
        this.type = type;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Type: " + type + ", Position: " + position;
    }
}
