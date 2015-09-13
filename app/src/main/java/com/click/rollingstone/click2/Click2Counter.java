package com.click.rollingstone.click2;

/**
 * Created by Anupama_Sikchi on 9/3/2015.
 */
public class Click2Counter {

    public int i=0;
    public boolean setUpCamera = true;


    private static Click2Counter counter;


    public static Click2Counter getInstance(){
        if(counter==null){
            counter = new Click2Counter();
        }

        return counter;
    }
}
