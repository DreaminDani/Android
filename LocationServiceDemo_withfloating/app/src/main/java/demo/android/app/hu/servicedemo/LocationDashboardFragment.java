package demo.android.app.hu.servicedemo;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import demo.android.app.hu.servicedemo.service.ServiceLocation;

/**
 * Created by Peter on 2014.10.26..
 */
public class LocationDashboardFragment extends Fragment {

    private TextView tvProviderValue;
    private TextView tvLatValue;
    private TextView tvLngValue;
    private TextView tvSpeedValue;
    private TextView tvAltValue;
    private TextView tvPosTimeValue;

    private ServiceLocation.BinderServiceLocation binderServiceLocation = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_dashboard, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initField(R.id.fieldProvider,
                getActivity().getString(R.string.txt_provider));
        initField(R.id.fieldLat, getActivity().getString(R.string.txt_latitude));
        initField(R.id.fieldLng, getActivity()
                .getString(R.string.txt_longitude));
        initField(R.id.fieldSpeed, getActivity().getString(R.string.txt_speed));
        initField(R.id.fieldAlt, getActivity().getString(R.string.txt_alt));
        initField(R.id.fieldPosTime,
                getActivity().getString(R.string.txt_position_time));

        Button btnGeocode = (Button) view.findViewById(R.id.btnGeocode);
        btnGeocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binderServiceLocation != null && binderServiceLocation.getSerivce() != null &&
                        binderServiceLocation.getSerivce().isLocationMonitorRunning()) {
                    Location loc = binderServiceLocation.getSerivce().getLastLocation();
                    if (loc != null) {
                        new AsyncTask<Location, Void, String>() {
                            @Override
                            protected String doInBackground(Location... params) {
                                String result = "";
                                try {
                                    Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addrs = null;
                                    addrs = gc.getFromLocation(params[0].getLatitude(),
                                            params[0].getLongitude(), 1);

                                    result = addrs.get(0).getCountryName() + "\n" +
                                             addrs.get(0).getAddressLine(0) + "\n" +
                                             addrs.get(0).getAddressLine(1);
                                } catch (Exception e) {
                                    result = "No address: " + e.getMessage();
                                }
                                return result;
                            }

                            @Override
                            protected void onPostExecute(String address) {
                                Toast.makeText(getActivity(),
                                        address,
                                        Toast.LENGTH_LONG).show();
                            }
                        }.execute(loc);
                    }
                }
            }
        });
    }

    private void initField(int fieldId, String headText) {
        View viewField = getView().findViewById(fieldId);
        TextView tvHead = (TextView) viewField.findViewById(R.id.tvHead);
        tvHead.setText(headText);

        switch (fieldId) {
            case R.id.fieldProvider:
                tvProviderValue = (TextView) viewField.findViewById(R.id.tvValue);
                break;
            case R.id.fieldLat:
                tvLatValue = (TextView) viewField.findViewById(R.id.tvValue);
                break;
            case R.id.fieldLng:
                tvLngValue = (TextView) viewField.findViewById(R.id.tvValue);
                break;
            case R.id.fieldSpeed:
                tvSpeedValue = (TextView) viewField.findViewById(R.id.tvValue);
                break;
            case R.id.fieldAlt:
                tvAltValue = (TextView) viewField.findViewById(R.id.tvValue);
                break;
            case R.id.fieldPosTime:
                tvPosTimeValue = (TextView) viewField.findViewById(R.id.tvValue);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent i = new Intent(getActivity(), ServiceLocation.class);
        getActivity().bindService(i, servConn, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver,
                new IntentFilter(ServiceLocation.BR_NEW_LOCATION));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (binderServiceLocation != null) {
            getActivity().unbindService(servConn);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location currentLocation = intent.getParcelableExtra(ServiceLocation.KEY_LOCATION);

            tvLatValue.setText("" + currentLocation.getLatitude());
            tvLngValue.setText("" + currentLocation.getLongitude());
            tvAltValue.setText("" + currentLocation.getAltitude());
            tvSpeedValue.setText("" + currentLocation.getSpeed());
            tvProviderValue.setText(currentLocation.getProvider());
            tvPosTimeValue.setText(new Date(currentLocation.getTime()).toString());
        }
    };

    private ServiceConnection servConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binderServiceLocation = ((ServiceLocation.BinderServiceLocation) iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
}
