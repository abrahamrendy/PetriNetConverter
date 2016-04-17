/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
public class Transition {
    private String id;
    private int number;
    private String name;
    static int qty;

    public Transition(String id, int number, String name) {
        this.number = number;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }
    
    
    
    public String toString () {
        return id+ " " +number + " " + name;
    }
}
