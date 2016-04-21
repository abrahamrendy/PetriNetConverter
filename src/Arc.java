/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
public class Arc {
    
    private static int qty = 1;
    private String id;
    private int number;
    private String orientation;
    private Transition transend;
    //private String transend;
    private Place placeend;
    //private String placeend;

    public Arc(int number, String orientation, Transition transend, Place placeend) {
        this.number = number;
        this.orientation = orientation;
        this.transend = transend;
        this.placeend = placeend;
        this.id = "Arc"+qty;
        qty++;
    }
//    public Arc(int number, String orientation, String transend, String placeend) {
//        this.number = number;
//        this.orientation = orientation;
//        this.transend = transend;
//        this.placeend = placeend;
//    }

//    public String toString() {
//        return number + " " + orientation + " " + transend.getName() + " " + placeend.getName();
//    }

    public String getId() {
        return id;
    }

    public String getOrientation() {
        return orientation;
    }

    public Transition getTransend() {
        return transend;
    }

    public Place getPlaceend() {
        return placeend;
    }

    public int getNumber() {
        return number;
    }
    
    
    
    public String toString() {
        return number + " " + orientation + " " + transend + " " + placeend;
    }
}
