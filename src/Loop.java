/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
public class Loop extends Message {

    static int qty;
    private int loopId;
    private boolean startLoop;
    private boolean endLoop;

    public Loop(int number, Object receiver, Object sender, String content, String contentType, int loopId) {
        super(number, receiver, sender, content, contentType);
        this.loopId = loopId;
    }

    public int getLoopId() {
        return loopId;
    }

    public void setLoopId(int loopId) {
        this.loopId = loopId;
    }

    public boolean isStartLoop() {
        return startLoop;
    }

    public void setStartLoop(boolean startLoop) {
        this.startLoop = startLoop;
    }

    public boolean isEndLoop() {
        return endLoop;
    }

    public void setEndLoop(boolean endLoop) {
        this.endLoop = endLoop;
    }

    
}
