package serverClasses;

import java.io.BufferedReader; 
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


import java.net.Socket;

public class Player {

	Socket clientSocket = null;
	String cAddress;
	int CPortNumber;
	boolean status = false; // player status - 'false' for not playing, 'true'
							// for playing
	public boolean turn = false;

	public String name;

	int userID;

	String memberNodeInfo;

	int board[][] = new int[10][10]; // this user's board

	BufferedReader in;
	PrintWriter out;
	
    InputStream msgIn;
    OutputStream msgOut;

	// reference to the linked list of all the current games

	public Player(Socket cSock, int ID) throws IOException {

		this.clientSocket = cSock;
		this.userID = ID;
		this.cAddress = clientSocket.getLocalAddress().toString();
		this.CPortNumber = clientSocket.getPort();

		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		
		msgIn = clientSocket.getInputStream();
		msgOut = clientSocket.getOutputStream();
		
		

	}
	
	
	public void sendWsMsg(String message)  {
		   byte[] rawData = message.getBytes();

		    int frameCount  = 0;
		    byte[] frame = new byte[10];

		    frame[0] = (byte) 129;

		    if(rawData.length <= 125){
		        frame[1] = (byte) rawData.length;
		        frameCount = 2;
		    }else if(rawData.length >= 126 && rawData.length <= 65535){
		        frame[1] = (byte) 126;
		        byte len = (byte) rawData.length;
		        frame[2] = (byte)((len >> 8 ) & (byte)255);
		        frame[3] = (byte)(len & (byte)255); 
		        frameCount = 4;
		    }else{
		        frame[1] = (byte) 127;
		        byte len = (byte) rawData.length;
		        frame[2] = (byte)((len >> 56 ) & (byte)255);
		        frame[3] = (byte)((len >> 48 ) & (byte)255);
		        frame[4] = (byte)((len >> 40 ) & (byte)255);
		        frame[5] = (byte)((len >> 32 ) & (byte)255);
		        frame[6] = (byte)((len >> 24 ) & (byte)255);
		        frame[7] = (byte)((len >> 16 ) & (byte)255);
		        frame[8] = (byte)((len >> 8 ) & (byte)255);
		        frame[9] = (byte)(len & (byte)255);
		        frameCount = 10;
		    }

		    int bLength = frameCount + rawData.length;

		    byte[] reply = new byte[bLength];

		    int bLim = 0;
		    for(int i=0; i<frameCount;i++){
		        reply[bLim] = frame[i];
		        bLim++;
		    }
		    for(int i=0; i<rawData.length;i++){
		        reply[bLim] = rawData[i];
		        bLim++;
		    }

		   
		    try {
		    	msgOut.write(reply);
				msgOut.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
     		
		
	}
	
	
	

	
	public static String bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public String recvWsMsg() throws IOException{
		
		
		String messageIn = "";
		
		
		   int len = 0;            
	        byte[] b = new byte[200];
	        //rawIn is a Socket.getInputStream();
	     
	            len = this.msgIn.read(b);
	            if(len!=-1){

	                byte rLength = 0;
	                int rMaskIndex = 2;
	                int rDataStart = 0;
	                //b[0] is always text in my case so no need to check;
	                byte data = b[1];
	                byte op = (byte) 127;
	                rLength = (byte) (data & op);

	                if(rLength==(byte)126) rMaskIndex=4;
	                if(rLength==(byte)127) rMaskIndex=10;

	                byte[] masks = new byte[4];

	                int j=0;
	                int i=0;
	                for(i=rMaskIndex;i<(rMaskIndex+4);i++){
	                    masks[j] = b[i];
	                    j++;
	                }

	                rDataStart = rMaskIndex + 4;

	                int messLen = len - rDataStart;

	                byte[] message = new byte[messLen];

	                for(i=rDataStart, j=0; i<len; i++, j++){
	                    message[j] = (byte) (b[i] ^ masks[j % 4]);
	                }

	                
	                
	                messageIn = new String(message);
	             

	            }
	        
	        
	        return messageIn;
	        
	}
	
	

}
