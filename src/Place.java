/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
public class Place {

    private String id;
    private int number;
    private String name;
    private char type;
    private boolean isSender;
    private String content;
    static int qty;

    public Place(String id, String name, boolean isSender) {
        this.name = name;
        this.isSender = isSender;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Place(String id, int number, String name, char type, boolean isSender, String content) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.isSender = isSender;
        this.content = content;
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public boolean isIsSender() {
        return isSender;
    }

    public void setIsSender(boolean isSender) {
        this.isSender = isSender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        if (isSender) {
            return id + " " + number + " " + name + " Sender" + " " + content;
        } else {
            return id + " " + number + " " + name + " Receiver" + " " + content;
        }
    }
}
