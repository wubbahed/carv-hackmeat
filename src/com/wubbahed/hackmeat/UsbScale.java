package com.wubbahed.hackmeat;

import java.nio.ByteBuffer;

import android.util.Log;

public class UsbScale {
	
	// THESE SHOULD REMAIN CONSTANT
	public int REQUEST_TYPE = 0x21;
	public int INDEX = 0x0;
	
	public int REQUEST = 0x5;
	public int VALUE = 0x0;

	private int OUNCES = 11;
	private int GRAMS = 2;
	
	
	// there should be six types of requests, and six different corresponding reports that come back
	// http://www.usb.org/developers/devclass_docs/pos1_02.pdf
		
	
    public byte[] message = new byte[8];
    
    public void newMessage() {
    	message = new byte[8];
    }
    
    public int[] getWeight(ByteBuffer b) {
    	int units = (int) b.get(2);

    	int lowbyte = (int) b.get(4) & 0xFF;
    	int highbyte = (int) b.get(5) & 0xFF;

    	int value = ((256*highbyte)+lowbyte);
    	
    	int[] result = {units, value};
    	
    	return result;
    }
    
    
    public String printMessage(ByteBuffer b) {
    	String str = "";
    	for (int i=0; i<8; i++) {
    		str += Integer.toString(b.get(i) & 0xFF, 16) + "\n";
    	}
    	str += "\n\n";
    	int units = (int) b.get(2);
    	int lowbyte = (int) b.get(4) & 0xFF;
    	int highbyte = (int) b.get(5) & 0xFF;

    	int value = ((256*highbyte)+lowbyte);
    	if (units == OUNCES) {
    		
    		if (value > 160) {
        		int pounds = (value - (value % 160)) / 160;
    			str += pounds + " POUNDS, " + ((value%160)*0.1) + " OUNCES\n\n";
    		} else {
    			str += (value*0.1) + " OUNCES\n\n";
    		}
    		// unit is 0.1 oz
    	}
    	if (units == GRAMS) {
    		str += value + " GRAMS\n\n";
    		//1 gram 2*255, 0x67 = 103
    	}
    	return str;
    }
    
    public String printMessage() {
    	String str = "";
    	
    	for (int i=0; i<8; i++) {
    		str += Integer.toString(message[i] & 0xFF, 16) + ", ";
    	}
    	
    	str += " - wubbahed";
    	return str;
    }
}
