/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
public class Message {
    private int number;
    private Object receiver;
    private Object sender;
    private String content;
    private String contentType;

    public Message(int number, Object receiver, Object sender, String content, String contentType) {
        this.number = number;
        this.receiver = receiver;
        this.sender = sender;
        this.content = content;
        this.contentType = contentType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Object getReceiver() {
        return receiver;
    }

    public void setReceiver(Object receiver) {
        this.receiver = receiver;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String toString() {
        return number + " " + content + " " + contentType;
    }
}
