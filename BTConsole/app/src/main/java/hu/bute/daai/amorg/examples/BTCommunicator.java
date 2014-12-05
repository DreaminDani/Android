package hu.bute.daai.amorg.examples;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BTCommunicator extends Thread {
	public static final int DATA_RECEIVED = 0;
	public static final int CONNECTION_SUCCESSFULL = 1;
	public static final int CONNECTION_FAILED = 2;
	public static final int CONNECTION_CLOSED = 3;
	
	private BluetoothAdapter bluetoothAdapter = null;
	private BluetoothSocket clientSocket = null;
    private InputStream inStream = null;
    private OutputStream outStream = null;
    private Handler msgHandler = null;
    private boolean enabled = false;
    
    private static BTCommunicator instance = null;

    public static BTCommunicator getInstance() {
       if(instance == null) {
          instance = new BTCommunicator();
       }
       return instance;
    }
    
    protected BTCommunicator() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    
    public BluetoothAdapter getBluetoothAdapter() {
    	return bluetoothAdapter;
    }
    
    public void connect(BluetoothDevice device, UUID uuid, Handler handler)
    {
    	bluetoothAdapter.cancelDiscovery();
    	msgHandler = handler;
    	enabled = true;
        
        try {
        	BTLogging.d("Start RFCOMM connection: "+uuid);
            clientSocket = device.createRfcommSocketToServiceRecord(uuid);
            BTLogging.d("Socket built");
            clientSocket.connect();
            BTLogging.d("Socket connected");
            inStream = clientSocket.getInputStream();
            BTLogging.d("InputStream ready");
        	outStream = clientSocket.getOutputStream();
        	BTLogging.d("OutputStream ready");
    		handler.obtainMessage(CONNECTION_SUCCESSFULL, "").sendToTarget();
        } catch (Exception e) {
        	BTLogging.d("Connect failed: "+e.getMessage());
        	handler.obtainMessage(CONNECTION_FAILED, e.getMessage()).sendToTarget();
    		try {
    			clientSocket.close();
    		} catch (IOException closeException) { }
		}
    }
 
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
 
        while (enabled) {
            try {
                bytes = inStream.read(buffer);
                msgHandler.obtainMessage(DATA_RECEIVED, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
            	BTLogging.d("Read failed: "+e.getMessage());
            	msgHandler.obtainMessage(CONNECTION_CLOSED, "").sendToTarget();
            	try {
                    clientSocket.close();
                } catch (Exception e2) { }
                break;
            }
        }
    }
 
    public void write(byte[] bytes) {
        try {
        	if(outStream != null) {
        		outStream.write(bytes);
        	}
        } catch (IOException e) {
        	BTLogging.d("Write failed: "+e.getMessage());
        	Log.e(Preferences.LOGTAG, e.getMessage());
        }
    }
    
    public void cancel() {
    	enabled = false;
    	
    	if (bluetoothAdapter != null)
    		bluetoothAdapter.cancelDiscovery();
    	
    	if (msgHandler != null)
    		msgHandler.obtainMessage(CONNECTION_CLOSED, "").sendToTarget();
    	    	
    	if (outStream != null) {
    		try {
    			outStream.close();
    			outStream = null;
            } catch (Exception e) {
            }
    	}
    	
    	if (inStream != null) {
    		try {
    			inStream.close();
    			inStream = null;
            } catch (Exception e) {
            }
    	}
    	
    	if (clientSocket != null) {
    		try {
                clientSocket.close();
                clientSocket = null;
            } catch (IOException e) {
            }
    	}
    	
    	instance = null;
    }
}
