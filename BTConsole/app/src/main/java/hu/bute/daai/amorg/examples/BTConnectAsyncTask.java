package hu.bute.daai.amorg.examples;

import java.util.UUID;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class BTConnectAsyncTask extends AsyncTask<Void, Void, String> {

	public interface TranslateCompleteListener {
		   public void onTaskComplete(String aText);
	}
	
	private Context context = null;
	private ProgressDialog progressDialog = null;
	private Handler handlerStatus;
	private BluetoothDevice device;
	private String selectedUUID;

	public BTConnectAsyncTask(Context context, Handler aHandlerStatus, BluetoothDevice aDevice, String aSelectedUUID)
	{
	    this.context = context; 
	    handlerStatus = aHandlerStatus;
	    device = aDevice;
	    selectedUUID = aSelectedUUID;
	}
	
	@Override
	protected void onPreExecute() 
	{
		progressDialog = new ProgressDialog(this.context);
	    progressDialog.setMessage("Connecting...");
	    progressDialog.show();
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			BTLogging.d("Async connection started...");
			UUID serviceUuid = UUID.fromString(selectedUUID);
			BTCommunicator.getInstance().connect(device, serviceUuid, handlerStatus);
	        return "OK";
		} catch (Exception e)
		{
			BTLogging.d("Error in async connect: "+e.getMessage());
			return ("Error: "+e.getMessage());
		}
	}


	@Override
	protected void onPostExecute(String result) 
	{
	    progressDialog.dismiss();
	} 
}
