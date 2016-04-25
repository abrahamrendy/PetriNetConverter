/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      //  String xml = "sequencediagramtestcase.xml";
        String xml = "sequencediagram.xml";
        Converter converter = new Converter();
        converter.readXML(xml);
        converter.convert();
        converter.writeXML("petrinet.xml");
    }
    
}
