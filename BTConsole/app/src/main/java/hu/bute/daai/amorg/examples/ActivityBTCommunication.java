package hu.bute.daai.amorg.examples;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityBTCommunication extends Activity {

	private LinearLayout layoutComm;
	private EditText editTextMessage;
	
	private Handler handler = new Handler() {
		@Override
        public void handleMessage(Message msg) {
			switch(msg.what) {
				case BTCommunicator.DATA_RECEIVED:
					byte[] readBuf = (byte[]) msg.obj;
					String readMessage = new String(readBuf, 0, msg.arg1);
					printMessage(getResources().getString(R.string.txtIn) + readMessage);
					break;
				case BTCommunicator.CONNECTION_SUCCESSFULL:
					Toast.makeText(ActivityBTCommunication.this, getResources().getString(R.string.txtConnectSucc), Toast.LENGTH_LONG).show();
					BTCommunicator.getInstance().start();
					break;
				case BTCommunicator.CONNECTION_FAILED:
					Toast.makeText(ActivityBTCommunication.this, getResources().getString(R.string.txtConnectFailed)+(String)msg.obj, Toast.LENGTH_LONG).show();
					finish();
					break;
				case BTCommunicator.CONNECTION_CLOSED:
					printMessage("Connection closed");
					break;
			}
		}
	};
	
	private void printMessage(String msg) {
		TextView tvMsg = new TextView(this);
		tvMsg.setText(msg);
		layoutComm.addView(tvMsg);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commlayout);
        
        layoutComm = (LinearLayout)findViewById(R.id.layoutComm);
        editTextMessage = (EditText)findViewById(R.id.editTextMessage);
        
        final Button btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Animation showAnim = AnimationUtils.loadAnimation(
						ActivityBTCommunication.this,
						R.anim.pushanim);
				btnSend.startAnimation(showAnim);				
				BTCommunicator.getInstance().write(editTextMessage.getText().toString().getBytes());
				editTextMessage.getText().clear();
			}
        });
        
        Bundle bund = getIntent().getExtras();
        String btAddress = bund.getString(BoschBTConsoleActivity.KEYBTADDRESS);
        String selectedUUID = bund.getString(BoschBTConsoleActivity.KEYUUID);
        
    	Set<BluetoothDevice> pairedDevices = BTCommunicator.getInstance().getBluetoothAdapter().getBondedDevices();
    	boolean deviceFound = false;
    	for(BluetoothDevice device : pairedDevices) {
    		if(device.getAddress().equals(btAddress)) {
    			new BTConnectAsyncTask(this, handler, device, selectedUUID).execute();
    			deviceFound = true;
    		}
    	}
    
    	if(!deviceFound) {
    		Toast.makeText(this, getResources().getString(R.string.txtDevNotPaired) + btAddress, Toast.LENGTH_LONG).show();
    	}
    }

	@Override
	protected void onPause() {
		BTCommunicator.getInstance().cancel();
		super.onPause();
	}
}
