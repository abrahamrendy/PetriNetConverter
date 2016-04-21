import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Abraham
 */
public class Converter {

    private ArrayList<Message> arrMessage;
    private ArrayList<Object> arrObject;
    private ArrayList<Place> arrPlace;
    private ArrayList<Place> arrUnusedPlace;
    private ArrayList<Transition> arrTransition;
    private ArrayList<Arc> arrArc;
    private HashMap objectId;

    public Converter() {
        this.arrMessage = new ArrayList();
        this.arrObject = new ArrayList();
        this.arrPlace = new ArrayList();
        this.arrArc = new ArrayList();
        this.arrTransition = new ArrayList();
        this.arrUnusedPlace = new ArrayList();
        this.objectId = new HashMap();
    }

    public void readXML(String xml) {
        Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            try {
                // parse using the builder to get the DOM mapping of the
                // XML file
                dom = db.parse(xml);
            } catch (IOException ex) {
                Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
            }

            Element doc = dom.getDocumentElement();
            NodeList nl = doc.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                //only process element node
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    //get Object from Sequence Diagram
                    if (el.getNodeName().equals("Objects")) {
                        for (int j = 0; j < el.getElementsByTagName("name").getLength(); j++) {
                            String name = el.getElementsByTagName("name").item(j).getTextContent();
                            Object temp = new Object(name);
                            arrObject.add(temp);
                            objectId.put(name, 0);
                        }
                    } else {
                        //get Messages from Sequence Diagram
                        if (el.getNodeName().equals("Messages")) {
                            //get the Message, Alt, Opt, Loop element
                            NodeList subEl = el.getChildNodes();
                            for (int j = 0; j < subEl.getLength(); j++) {
                                if (subEl.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                    Element el2 = (Element) subEl.item(j);
                                    //get the Message element
                                    if (el2.getNodeName().equals("Message")) {
                                        int orderNumber = Integer.parseInt(
                                                el2.getElementsByTagName("orderNumber").item(0).getTextContent());
                                        Object initObject = new Object(
                                                el2.getElementsByTagName("initObject").item(0).getTextContent());
                                        Object destObject = new Object(
                                                el2.getElementsByTagName("destObject").item(0).getTextContent());
                                        String content = el2.getElementsByTagName("content").item(0).getTextContent();
                                        String contentType = el2.getElementsByTagName("contentType").item(0).getTextContent();
                                        arrMessage.add(new Message(orderNumber, destObject, initObject, content, contentType));
                                    } else {
                                        if (el2.getNodeName().equals("Alt")) {
                                            //TBA
                                        } else {
                                            if (el2.getNodeName().equals("Opt")) {
                                                //TBA
                                            } else {
                                                if (el2.getNodeName().equals("Loop")) {
                                                    //TBA
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        }
    }

    public void convert() {
        for (int i = 0; i < arrMessage.size(); i++) {
            //Convert to Sender Place (Sender)
            int messageNumber = arrMessage.get(i).getNumber();
            String senderName = arrMessage.get(i).getSender().getName();
            String content = arrMessage.get(i).getContent();
            char type = arrMessage.get(i).getContentType().charAt(0);
            //Handling first message in Sequence Diagram
            if (messageNumber == 1) {
                //Place.qty++;
                int id = (int) objectId.get(senderName);
                id++;
                objectId.put(senderName, id);
                arrPlace.add(new Place((senderName.charAt(0) + "" + id), messageNumber, senderName + " " + id, type, true, content));
                //arrPlace.add(new Place((senderName.charAt(0) + "" + Place.qty), messageNumber, senderName, type, true, content));
            } else {
                //Handling message except first message in Sequence Diagram
                for (int j = 0; j < arrUnusedPlace.size(); j++) {
                    //Handling if the Place is already existed
                    if (arrUnusedPlace.get(j).getName().equals(senderName) && arrUnusedPlace.get(j).getContent() == null) {
                        arrUnusedPlace.get(j).setName(senderName + " " + objectId.get(senderName));
                        arrUnusedPlace.get(j).setIsSender(true);
                        arrUnusedPlace.get(j).setContent(content);
                        arrUnusedPlace.get(j).setNumber(messageNumber);
                        arrUnusedPlace.get(j).setType(arrMessage.get(i).getContentType().charAt(0));
                        arrPlace.add(arrUnusedPlace.get(j));

                        arrUnusedPlace.remove(j);
                        j = arrUnusedPlace.size();
                    } else {
                        if (j == arrUnusedPlace.size() - 1) {
//                            Place.qty++;
//                            arrPlace.add(new Place((senderName.charAt(0) + "" + Place.qty), messageNumber, senderName, type, true, content));
                            int id = (int) objectId.get(senderName);
                            id++;
                            objectId.put(senderName, id);
                            arrPlace.add(new Place((senderName.charAt(0) + "" + id), messageNumber, senderName + " " + id, type, true, content));
                        }
                    }
                }
            }

            //Convert to Sender Place (Receiver)
            String receiverName = arrMessage.get(i).getReceiver().getName();
            //Handling first message
            if (messageNumber == 1) {
//                Place.qty++;
//                arrPlace.add(new Place((receiverName.charAt(0) + "" + Place.qty), messageNumber, receiverName, type, false, content));
                int id = (int) objectId.get(receiverName);
                id++;
                objectId.put(receiverName, id);
                arrPlace.add(new Place((receiverName.charAt(0) + "" + id), messageNumber, receiverName + " " + id, type, false, content));
            } else {
                //Handling message except first message in Sequence Diagram
                for (int j = 0; j < arrUnusedPlace.size(); j++) {
                    //Handling if the Place is already existed
                    if (arrUnusedPlace.get(j).getName().equals(receiverName) && arrUnusedPlace.get(j).getContent() == null) {
                        arrUnusedPlace.get(j).setName(receiverName + " " + objectId.get(receiverName));
                        arrUnusedPlace.get(j).setIsSender(false);
                        arrUnusedPlace.get(j).setContent(content);
                        arrUnusedPlace.get(j).setNumber(messageNumber);
                        arrUnusedPlace.get(j).setType(arrMessage.get(i).getContentType().charAt(0));
                        arrPlace.add(arrUnusedPlace.get(j));
                        arrUnusedPlace.remove(j);
                        j = arrUnusedPlace.size();
                    } else {
                        if (j == arrUnusedPlace.size() - 1) {
//                            Place.qty++;
//                            arrPlace.add(new Place((receiverName.charAt(0) + "" + Place.qty), messageNumber, receiverName, type, false, content));
                            int id = (int) objectId.get(receiverName);
                            id++;
                            objectId.put(receiverName, id);
                            arrPlace.add(new Place((receiverName.charAt(0) + "" + id), messageNumber, receiverName + " " + id, type, false, content));
                        }
                    }
                }
            }

            //Convert to Transition
            Transition.qty++;
            arrTransition.add(new Transition(("Trans" + Transition.qty), messageNumber, content));

            //Convert to Arc (Place to Transition)
            arrArc.add(new Arc(messageNumber, "PtoT", arrTransition.get(arrTransition.size() - 1),
                    arrPlace.get(arrPlace.size() - 2)));
            arrArc.add(new Arc(messageNumber, "PtoT", arrTransition.get(arrTransition.size() - 1),
                    arrPlace.get(arrPlace.size() - 1)));

            //Convert to Destination Place (Sender)
            if (i != arrMessage.size() - 1) {
//                Place.qty++;
//                arrUnusedPlace.add(new Place((senderName.charAt(0) + "" + Place.qty), senderName, true));
                int id = (int) objectId.get(senderName);
                id++;
                objectId.put(senderName, id);
                arrUnusedPlace.add(new Place((senderName.charAt(0) + "" + id), senderName + " " + id, true));
            }

            //Convert to Destination Place (Receiver)
            if (i != arrMessage.size() - 1) {
//                Place.qty++;
//                arrUnusedPlace.add(new Place((receiverName.charAt(0) + "" + Place.qty), receiverName, false));
                int id = (int) objectId.get(receiverName);
                id++;
                objectId.put(receiverName, id);
                arrUnusedPlace.add(new Place((receiverName.charAt(0) + "" + id), receiverName + " " + id, false));
            }

            //Convert to Arc (Transition to Place)
            if (i == arrMessage.size() - 1) {
                arrArc.add(new Arc(messageNumber, "TtoP", arrTransition.get(arrTransition.size() - 1),
                        arrPlace.get(0)));
                arrArc.add(new Arc(messageNumber, "TtoP", arrTransition.get(arrTransition.size() - 1),
                        arrPlace.get(1)));
            } else {
                arrArc.add(new Arc(messageNumber, "TtoP", arrTransition.get(arrTransition.size() - 1),
                        arrUnusedPlace.get(arrUnusedPlace.size() - 2)));
                arrArc.add(new Arc(messageNumber, "TtoP", arrTransition.get(arrTransition.size() - 1),
                        arrUnusedPlace.get(arrUnusedPlace.size() - 1)));
            }
        }
        System.out.println(arrPlace.toString());
        System.out.println(arrTransition.toString());
        System.out.println(arrArc.toString());
        System.out.println(arrArc.size());
    }

    public void writeXML(String xml) {
        Document dom;
        Element e = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element rootEle = dom.createElement("workspaceElements");

            // create data elements and place them under root
            e = dom.createElement("generator");
            e.setAttribute("tool", "CPN Tools");
            e.setAttribute("version", "4.0.1");
            e.setAttribute("format", "6");
            rootEle.appendChild(e);

            e = dom.createElement("cpnet");
            Element globbox = dom.createElement("globbox");
            Element block = dom.createElement("block");
            block.setAttribute("id", "ID1");
            Element id = dom.createElement("id");
            id.appendChild(dom.createTextNode("Standard declarations"));
            block.appendChild(id);

            //colset STRING
            Element color = dom.createElement("color");
            color.setAttribute("id", "");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("STRING"));
            color.appendChild(id);
            color.appendChild(dom.createElement("string"));
            block.appendChild(color);

            //colset NUM
            color = dom.createElement("color");
            color.setAttribute("id", "");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("NUM"));
            color.appendChild(id);
            color.appendChild(dom.createElement("int"));
            Element layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("colset NUM = int;"));
            color.appendChild(layout);
            block.appendChild(color);

            //colset TYPE
            color = dom.createElement("color");
            color.setAttribute("id", "");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("TYPE"));
            color.appendChild(id);
            Element enume = dom.createElement("enum");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("m"));
            enume.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("s"));
            enume.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("d"));
            enume.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("c"));
            enume.appendChild(id);
            color.appendChild(enume);
            block.appendChild(color);
            layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("colset TYPE = with m | s | c | d;"));
            color.appendChild(layout);

            //colset ACT
            color = dom.createElement("color");
            color.setAttribute("id", "");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("ACT"));
            color.appendChild(id);
            enume = dom.createElement("enum");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("send"));
            enume.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("receive"));
            enume.appendChild(id);
            color.appendChild(enume);
            block.appendChild(color);
            layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("colset ACT = with send | receive;"));
            color.appendChild(layout);
            block.appendChild(color);

            //colset CONTENT
            color = dom.createElement("color");
            color.setAttribute("id", "");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("CONTENT"));
            color.appendChild(id);
            color.appendChild(dom.createElement("string"));
            block.appendChild(color);

            //var NUM
            Element var = dom.createElement("var");
            var.setAttribute("id", "");
            Element type = dom.createElement("type");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("NUM"));
            type.appendChild(id);
            var.appendChild(type);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("n"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("n1"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("n2"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("n3"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("n4"));
            var.appendChild(id);
            layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("var n,n1,n2,n3,n4 : NUM;"));
            var.appendChild(layout);
            block.appendChild(var);

            //var TYPE
            var = dom.createElement("var");
            var.setAttribute("id", "");
            type = dom.createElement("type");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("TYPE"));
            type.appendChild(id);
            var.appendChild(type);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("t"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("t1"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("t2"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("t3"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("t4"));
            var.appendChild(id);
            layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("var t,t1,t2,t3,t4 : TYPE;"));
            var.appendChild(layout);
            block.appendChild(var);

            //var ACT
            var = dom.createElement("var");
            var.setAttribute("id", "");
            type = dom.createElement("type");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("ACT"));
            type.appendChild(id);
            var.appendChild(type);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("a"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("a1"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("a2"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("a3"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("a4"));
            var.appendChild(id);
            layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("var a,a1,a2,a3,a4 : ACT;"));
            var.appendChild(layout);
            block.appendChild(var);

            //var CONTENT
            var = dom.createElement("var");
            var.setAttribute("id", "");
            type = dom.createElement("type");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("CONTENT"));
            type.appendChild(id);
            var.appendChild(type);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("ct"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("ct1"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("ct2"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("ct3"));
            var.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("ct4"));
            var.appendChild(id);
            layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("var ct,ct1,ct2,ct3,ct4 : CONTENT;"));
            var.appendChild(layout);
            block.appendChild(var);

            //colset TYPExACTxCONTENT
            color = dom.createElement("color");
            color.setAttribute("id", "");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("TYPExACTxCONTENT"));
            color.appendChild(id);
            color.appendChild(dom.createElement("timed"));
            Element product = dom.createElement("product");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("TYPE"));
            product.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("ACT"));
            product.appendChild(id);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("CONTENT"));
            product.appendChild(id);
            color.appendChild(product);
            layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("colset TYPExACTxCONTENT = product TYPE*ACT*CONTENT timed;"));
            color.appendChild(layout);
            block.appendChild(color);

            //var TYPExACTxCONTENT
            var = dom.createElement("var");
            var.setAttribute("id", "");
            type = dom.createElement("type");
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("TYPExACTxCONTENT"));
            type.appendChild(id);
            var.appendChild(type);
            id = dom.createElement("id");
            id.appendChild(dom.createTextNode("exp"));
            var.appendChild(id);
            layout = dom.createElement("layout");
            layout.appendChild(dom.createTextNode("var exp : TYPExACTxCONTENT;"));
            var.appendChild(layout);
            block.appendChild(var);

            globbox.appendChild(block);
            e.appendChild(globbox);
            Element page = dom.createElement("page");
            page.setAttribute("id", "");
            Element pageattr = dom.createElement("pageattr");
            pageattr.setAttribute("name", "Page");
            page.appendChild(pageattr);

            //write place
            for (int i = 0; i < arrPlace.size(); i++) {
                Element place = dom.createElement("place");
                place.setAttribute("id", arrPlace.get(i).getId());
                Element temp = dom.createElement("text");
                temp.appendChild(dom.createTextNode(arrPlace.get(i).getName()));
                place.appendChild(temp);
                temp = dom.createElement("type");
                Element text = dom.createElement("text");
                text.setAttribute("tool", "CPN Tools");
                text.setAttribute("version", "4.0.0");
                text.appendChild(dom.createTextNode("TYPExACTxCONTENT"));
                temp.appendChild(text);
                place.appendChild(temp);
                temp = dom.createElement("marking");
                temp.setAttribute("x", "0");
                temp.setAttribute("y", "0");
                temp.setAttribute("hidden", "false");
                place.appendChild(temp);
                String name = arrPlace.get(i).getName();
                String[] arrTemp = name.split(" ");
                if (arrTemp[1].equals("1")) {
                    temp = dom.createElement("initmarking");
                    temp.setAttribute("id", "INIT"+(i+1));
                    text = dom.createElement("text");
                    text.setAttribute("tool", "CPN Tools");
                    text.setAttribute("version", "4.0.1");
                    String tempStatus;
                    if (arrPlace.get(i).isIsSender()) {
                        tempStatus = "send";
                    } else {
                        tempStatus = "receive";
                    }
                    text.appendChild(dom.createTextNode("1`("+ arrPlace.get(i).getType() + ", "+tempStatus+", \""+arrPlace.get(i).getContent()+"\")"));
                    temp.appendChild(text);
                    place.appendChild(temp);
                }
                page.appendChild(place);
            }
            for (int i = 0; i < arrUnusedPlace.size(); i++) {
                Element place = dom.createElement("place");
                place.setAttribute("id", arrUnusedPlace.get(i).getId());
                Element temp = dom.createElement("text");
                temp.appendChild(dom.createTextNode(arrUnusedPlace.get(i).getName()));
                place.appendChild(temp);
                page.appendChild(place);
                temp = dom.createElement("type");
                Element text = dom.createElement("text");
                text.setAttribute("tool", "CPN Tools");
                text.setAttribute("version", "4.0.0");
                text.appendChild(dom.createTextNode("TYPExACTxCONTENT"));
                temp.appendChild(text);
                place.appendChild(temp);
                temp = dom.createElement("marking");
                temp.setAttribute("x", "0");
                temp.setAttribute("y", "0");
                temp.setAttribute("hidden", "false");
                place.appendChild(temp);
            }

            //write transition
            for (int i = 0; i < arrTransition.size(); i++) {
                Element trans = dom.createElement("trans");
                trans.setAttribute("explicit", "false");
                trans.setAttribute("id", arrTransition.get(i).getId());
                Element temp = dom.createElement("text");
                temp.appendChild(dom.createTextNode(arrTransition.get(i).getName()));
                trans.appendChild(temp);
                page.appendChild(trans);
            }

            //write arc
            for (int i = 0; i < arrArc.size(); i++) {
                Element arc = dom.createElement("arc");
                arc.setAttribute("id", arrArc.get(i).getId());
                arc.setAttribute("order", "0");
                arc.setAttribute("orientation", arrArc.get(i).getOrientation());
                Element temp = dom.createElement("transend");
                temp.setAttribute("idref", arrArc.get(i).getTransend().getId());
                arc.appendChild(temp);
                temp = dom.createElement("placeend");
                temp.setAttribute("idref", arrArc.get(i).getPlaceend().getId());
                arc.appendChild(temp);
                if (arrArc.get(i).getOrientation().equals("PtoT")) {
                    if (arrArc.get(i).getPlaceend().isIsSender()) {
                        Element annot = dom.createElement("annot");
                        annot.setAttribute("id", "ANN" + (i + 1));
                        Element text = dom.createElement("text");
                        text.setAttribute("tool", "CPN Tools");
                        text.setAttribute("version", "4.0.1");
                        text.appendChild(dom.createTextNode("1`(t1,a1,ct1)"));
                        annot.appendChild(text);
                        arc.appendChild(annot);
                    } else {
                        Element annot = dom.createElement("annot");
                        annot.setAttribute("id", "ANN" + (i + 1));
                        Element text = dom.createElement("text");
                        text.setAttribute("tool", "CPN Tools");
                        text.setAttribute("version", "4.0.1");
                        text.appendChild(dom.createTextNode("1`(t2,a2,ct2)"));
                        annot.appendChild(text);
                        arc.appendChild(annot);
                    }
                } else {
                    Element annot = dom.createElement("annot");
                    annot.setAttribute("id", "ANN" + (i + 1));
                    Element text = dom.createElement("text");
                    text.setAttribute("tool", "CPN Tools");
                    text.setAttribute("version", "4.0.1");
                    Place tempPlace = arrArc.get(i).getPlaceend();
                    String status;
                    if (tempPlace.isIsSender()) {
                        status = "send";
                    } else {
                        status = "receive";
                    }
                    text.appendChild(dom.createTextNode("1`(" + tempPlace.getType() + ", " + status + ", \"" + tempPlace.getContent() + "\")"));
                    annot.appendChild(text);
                    arc.appendChild(annot);
                }

                page.appendChild(arc);
            }
            Element constraint = dom.createElement("constraint");
            page.appendChild(constraint);
            e.appendChild(page);
            rootEle.appendChild(e);
//            e = dom.createElement("role2");
//            e.appendChild(dom.createTextNode(role2));
//            rootEle.appendChild(e);
//
//            e = dom.createElement("role3");
//            e.appendChild(dom.createTextNode(role3));
//            rootEle.appendChild(e);
//
//            e = dom.createElement("role4");
//            e.appendChild(dom.createTextNode(role4));
//            rootEle.appendChild(e);
            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
                DOMImplementation domImpl = dom.getImplementation();
                DocumentType doctype = domImpl.createDocumentType("workspaceElements",
                        "-//CPN//DTD CPNXML 1.0//EN",
                        "http://cpntools.org/DTD/6/cpn.dtd");
                tr.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(xml)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

}
