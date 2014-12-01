package android.ait.hu.weatherinfoapp.network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Peter on 2014.11.27..
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String>{

    public static final String FILTER_WEATHER = "FILTER_WEATHER";
    public static final String KEY_WEATHER = "KEY_WEATHER";

    private Context ctx;

    public HttpAsyncTask(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        String result = "";

        HttpURLConnection conn = null;
        InputStream is = null;
        try {

            URL url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();

            is = conn.getInputStream();
            int ch;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }

            result = new String(bos.toByteArray());

        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Intent i = new Intent(FILTER_WEATHER);
        i.putExtra(KEY_WEATHER, result);
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(i);
    }
}
