package com.fahimshahrierrasel.collectionnotifier.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fahimshahrierrasel.collectionnotifier.CollectionNotifier;
import com.fahimshahrierrasel.collectionnotifier.R;
import com.fahimshahrierrasel.collectionnotifier.model.Bin;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BinUpdateFragment extends Fragment {
    EditText editTextName;
    EditText editTextTrigPin;
    EditText editTextEchoPin;
    EditText editTextNotifyLevel;
    Button buttonAddSensor;
    EditText physicalButton;
    EditText ledPin;
    private FragmentActivity activity;
    private String id;
    private Socket mSocket;
    private String TAG = getClass().getSimpleName();

    public BinUpdateFragment() {
    }

    public static BinUpdateFragment newInstance(String id){
        BinUpdateFragment binUpdateFragment = new BinUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        binUpdateFragment.setArguments(bundle);
        return binUpdateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_bin_details, container, false);
        bindViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();

        if(getArguments() != null)
            id = getArguments().getString("id");

        CollectionNotifier app = (CollectionNotifier) activity.getApplication();
        mSocket = app.getSocket();
        mSocket.connect();
        Gson gson = new Gson();

        mSocket.on("take_bin", args -> activity.runOnUiThread(() -> {
            JSONObject jsonObject = (JSONObject) args[0];
            Bin bin = gson.fromJson(jsonObject.toString(), Bin.class);
            if(id.equals(bin.getId())){
                populateView(bin);
            }
        }));

        buttonAddSensor.setOnClickListener(view1 -> {

            Map<String, Object> data = new HashMap<>();
            data.put("name", editTextName.getText().toString());
            data.put("trig_pin", Integer.parseInt(editTextTrigPin.getText().toString()));
            data.put("echo_pin", Integer.parseInt(editTextEchoPin.getText().toString()));
            data.put("notify_level", Integer.parseInt(editTextNotifyLevel.getText().toString()));
            data.put("button", Integer.parseInt(physicalButton.getText().toString()));
            data.put("led", Integer.parseInt(ledPin.getText().toString()));

            mSocket.emit("update_sensor", id, new JSONObject(data));
            Log.d(TAG, "Data Emitted " + data.toString());
            activity.onBackPressed();
        });
    }

    private void populateView(Bin bin) {
        editTextName.setText(bin.getName());
        editTextEchoPin.setText(String.valueOf(bin.getEchoPin()));
        editTextTrigPin.setText(String.valueOf(bin.getTrigPin()));
        editTextNotifyLevel.setText(String.valueOf(bin.getNotifyLevel()));
        physicalButton.setText(String.valueOf(bin.getButton()));
        ledPin.setText(String.valueOf(bin.getLed()));
    }

    @Override
    public void onStart() {
        super.onStart();
        if(id != null)
            mSocket.emit("get_bin", id);
    }

    private void bindViews(View rootView) {
        editTextName = rootView.findViewById(R.id.editText_name);
        editTextTrigPin = rootView.findViewById(R.id.editText_trig_pin);
        editTextEchoPin = rootView.findViewById(R.id.editText_echo_pin);
        editTextNotifyLevel = rootView.findViewById(R.id.editText_notify_level);
        buttonAddSensor = rootView.findViewById(R.id.button_add_sensor);
        physicalButton = rootView.findViewById(R.id.editText_physical_button_pin);
        ledPin = rootView.findViewById(R.id.editText_led_pin);
    }
}
