package com.vishnurajan.custombluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        final Button pairing = findViewById(R.id.pairing);

        pairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);
                Toast.makeText(SelectDeviceActivity.this, "Select Pair New Device Option, Pair the bluetooth device and restart the app. The New device will be available in the bluetooth device list afterward", Toast.LENGTH_LONG).show();

            }
        });

        // Bluetooth Setup
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Please Check whether bluetooth is turned on", Toast.LENGTH_LONG).show();
            // Show proper message here
            finish();
        }
        else {
            // Get List of Paired Bluetooth Device
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            List<Object> deviceList = new ArrayList<>();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    DeviceInfoModel deviceInfoModel = new DeviceInfoModel(deviceName,deviceHardwareAddress);
                    deviceList.add(deviceInfoModel);
                }
                // Display paired device using recyclerView
                RecyclerView recyclerView = findViewById(R.id.recyclerViewDevice);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                DeviceListAdapter deviceListAdapter = new DeviceListAdapter(this,deviceList);
                recyclerView.setAdapter(deviceListAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            } else {
                View view = findViewById(R.id.recyclerViewDevice);
                Snackbar snackbar = Snackbar.make(view, "Activate Bluetooth or pair a Bluetooth device", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { }
                });
                snackbar.show();
            }
        }

    }
}
