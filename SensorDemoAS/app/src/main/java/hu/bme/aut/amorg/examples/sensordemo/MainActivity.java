package hu.bme.aut.amorg.examples.sensordemo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {
	private TextView tvStatus;
	private TextView tvAcc;
	private TextView tvMagnet;
	private TextView tvLight;
	
	private SensorManager sm;
	private Sensor sensorAcc;
	private Sensor sensorMagnet;
	private Sensor sensorLight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		tvAcc = (TextView) findViewById(R.id.tvAcc);
		tvMagnet = (TextView) findViewById(R.id.tvMagnet);
		tvLight = (TextView) findViewById(R.id.tvLight);
		
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorAcc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorLight = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		sm.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, sensorMagnet, SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sm.unregisterListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		listSensors();
	}

	private void listSensors() {
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
		for(Sensor s:sensors) {
			tvStatus.append(s.getName()+"\n");
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.equals(sensorMagnet)) {
			float magnetic = event.values[0];
			tvMagnet.setText("Magnetic field: "+magnetic);
		}
		else if (event.sensor.equals(sensorLight)) {
			float light = event.values[0];
			tvLight.setText("Light sensor: "+light);
		}
		else if (event.sensor.equals(sensorAcc)) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			tvAcc.setText("X: "+x+"\nY:"+y+"\nZ: "+z);
		}
	}
}
