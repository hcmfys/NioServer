package org.springbus;

import java.util.LinkedList;

public class EventPipLine {

    private LinkedList <MessageEvent>  linkedList=new LinkedList<>();

    public void addHandler(MessageEvent e){
         MessageEvent event=  linkedList.peek();
         linkedList.add(e);
         if(event!=null) {
             event.setNext(e);
         }
    }

}
