/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
public class Opt extends Message{
    
    static int qty;
    private boolean startOpt;
    private boolean endOpt;
    private int optId;
    
    public Opt(int number, Object receiver, Object sender, String content, String contentType, int optId) {
        super(number, receiver, sender, content, contentType);
        this.optId = optId;
    }

    public boolean isStartOpt() {
        return startOpt;
    }

    public void setStartOpt(boolean startOpt) {
        this.startOpt = startOpt;
    }

    public boolean isEndOpt() {
        return endOpt;
    }

    public void setEndOpt(boolean endOpt) {
        this.endOpt = endOpt;
    }

    public int getOptId() {
        return optId;
    }
    
    
}
