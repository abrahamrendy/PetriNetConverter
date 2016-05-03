/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
public class Alt extends Message{
    static int qty;
    private int altId;
    private int subAltId;
    private boolean startAlt;
    private boolean endAlt;
    private boolean startSubAlt, endSubAlt;
    
    public Alt(int number, Object receiver, Object sender, String content, String contentType, int altId, int subAltId) {
        super(number, receiver, sender, content, contentType);
        this.altId = altId;
        this.subAltId = subAltId;
    }

    public int getSubAltId() {
        return subAltId;
    }

    public boolean isStartAlt() {
        return startAlt;
    }

    public void setStartAlt(boolean startAlt) {
        this.startAlt = startAlt;
    }

    public boolean isEndAlt() {
        return endAlt;
    }

    public void setEndAlt(boolean endAlt) {
        this.endAlt = endAlt;
    }

    public int getAltId() {
        return altId;
    }

    public boolean isStartSubAlt() {
        return startSubAlt;
    }

    public void setStartSubAlt(boolean startSubAlt) {
        this.startSubAlt = startSubAlt;
    }

    public boolean isEndSubAlt() {
        return endSubAlt;
    }

    public void setEndSubAlt(boolean endSubAlt) {
        this.endSubAlt = endSubAlt;
    }
    
    
    
}
