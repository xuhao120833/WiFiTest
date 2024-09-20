package com.htc.wifitest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.htc.wifitest.R;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        EnumPowerMode expectPowerMode = AwTvSystemTypes.EnumPowerMode.E_AW_POWER_MODE_STANDBY;
//        AwTvSystemManager.getInstance(getApplicationContext()).setPowerOnMode(expectPowerMode);


        TextView SSID_view = findViewById(R.id.SSID);
        TextView password_view = findViewById(R.id.password);
        requestPermission();


        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        SSID_view.setText(ssid);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<WifiConfiguration> configs = wifiManager.getPrivilegedConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            if (config.SSID.equals(ssid)) {
                String password = config.preSharedKey;
                password = password.replace("\"", "");
                Log.d("WiFi Password", password);
                password_view.setText(password);
            }
        }

    }

    private void requestPermission() {
        boolean isAllGranted = true;
        String[] permissions = {
                "android.permission.INTERNET",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.CHANGE_NETWORK_STATE",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.CHANGE_WIFI_STATE",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_WIFI_CREDENTIAL"
        };

        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                isAllGranted = false;
            }
        }

        if (!isAllGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("myapplication", "Permission " + permissions[i] + " granted");

                } else {
                    Log.d("myapplication", "Permission " + permissions[i] + " denied");
                }
            }
        }
    }
}