package hu.bute.daai.amorg.examples;

import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BTLogging {
	private static final String LogFileName = "log_btconsole.txt";
	private static final String TAG = "CONSOLE";
	private static final boolean FILELOG = true;

	private static void log(char aLevel, String aMsg) {
		if (aMsg == null) {
			aMsg = "";
		}

		if (FILELOG) {
			boolean extStorageAvailable = false;
			boolean extStorageWriteable = false;
			String state = Environment.getExternalStorageState();

			// k�ls� adatt�r ellen�rz�se
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				// csatolt
				extStorageAvailable = true;
				extStorageWriteable = true;
			} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
				// csak olvashat�
				extStorageAvailable = true;
				extStorageWriteable = false;
			} else {
				// nincs
				extStorageAvailable = false;
				extStorageWriteable = false;
			}
			File logPath = null;
			File logFile = null;

			// file
			if (extStorageAvailable && extStorageWriteable) {
				logPath = Environment.getExternalStorageDirectory();
				logFile = new File(logPath, LogFileName);
				try {
					if (logFile.exists() == false) {
						if (logFile.createNewFile() == false) {
							throw new IOException("Log file cannot be created.");
						}
					}
				} catch (Exception e) {
					Log.w(TAG, "File error: " + e.getMessage());
				}

				// set the time
				Time time = new Time();
				time.set(System.currentTimeMillis());
				// BufferedWriter logWriter = null;
				// write to file
				try {
					BufferedWriter logWriter = new BufferedWriter(
							new FileWriter(logFile, true));
					StringBuilder str = new StringBuilder();
					str.append(time.format("[%Y-%m-%d %H:%M:%S]"));
					str.append(" [");
					str.append(aLevel);
					str.append("] [");
					str.append(TAG);
					str.append("] ");
					str.append(aMsg);

					logWriter.append(str.toString());
					logWriter.newLine();
					logWriter.flush();
					logWriter.close();
				} catch (Exception e) {
					Log.w(TAG,
							"File not found in BTLogging.d() method: "
									+ e.getMessage());
				}
			}// endif
		}

		switch (aLevel) {
		case 'V':
			Log.v(TAG, aMsg);
			break;
		case 'D':
			Log.d(TAG, aMsg);
			break;
		case 'I':
			Log.i(TAG, aMsg);
			break;
		case 'W':
			Log.w(TAG, aMsg);
			break;
		case 'E':
			Log.e(TAG, aMsg);
			break;
		default:
			Log.d(TAG, aMsg);
		}
	}

	public static void v(String msg) {
		log('V', msg);
	}

	public static void d(String msg) {
		log('D', msg);
	}

	public static void i(String msg) {
		log('I', msg);
	}

	public static void w(String msg) {
		log('W', msg);
	}

	public static void e(String msg) {
		log('E', msg);
	}
}
