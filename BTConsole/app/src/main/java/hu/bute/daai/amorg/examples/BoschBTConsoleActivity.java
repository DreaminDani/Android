package hu.bute.daai.amorg.examples;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class BoschBTConsoleActivity extends Activity {
	private static int REQUEST_ENABLE_BT = 100;
	public static final String KEYBTADDRESS = "BTADDRESS";
	public static final String KEYUUID = "UUID";
	private DeviceListAdapter listAdapter;
	
	private final BroadcastReceiver foundReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listAdapter.addDevice(device);
                listAdapter.notifyDataSetChanged();
            }
        }
    };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if(!BTCommunicator.getInstance().getBluetoothAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(foundReceiver, filter);
        
        ListView listView = (ListView)findViewById(R.id.listView1);
        listAdapter = new DeviceListAdapter();
        listView.setAdapter(listAdapter);
        
        Button scanButton = (Button)findViewById(R.id.btnScan);
        scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BTCommunicator.getInstance().getBluetoothAdapter().startDiscovery();
			}
        });
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				showUUIDSelectDialog(position);
			}
        });
        
        BTLogging.d("Bosch Bluetooth Console started");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(foundReceiver);
    	BTCommunicator.getInstance().cancel();
    }
    
    private void showUUIDSelectDialog(final int aSelectPosition)
    {
    	final Dialog dialog = new Dialog(this);
    	dialog.setContentView(R.layout.dialog_select_uuid);
    	dialog.setTitle(getResources().getString(R.string.titleSelectUUID));
    	dialog.setCancelable(true);

    	final EditText editTextUUID = (EditText) dialog.findViewById(R.id.editTextUUID);
    	Button btnScan = (Button) dialog.findViewById(R.id.btnUUIDOk);
    	btnScan.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			BluetoothDevice device = (BluetoothDevice)listAdapter.getItem(aSelectPosition);
				Intent intent = new Intent(BoschBTConsoleActivity.this, ActivityBTCommunication.class);
				intent.putExtra(KEYBTADDRESS, device.getAddress());
				intent.putExtra(KEYUUID, editTextUUID.getText().toString());
				BTLogging.d("Start connecting to: "+device.getAddress()+", UUID: "+editTextUUID.getText().toString());
		        startActivity(intent);
		        dialog.dismiss();
    		}
    	});
    	dialog.show();
    }
}