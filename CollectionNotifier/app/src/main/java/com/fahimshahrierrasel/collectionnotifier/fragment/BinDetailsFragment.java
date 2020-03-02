package com.fahimshahrierrasel.collectionnotifier.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fahimshahrierrasel.collectionnotifier.CollectionNotifier;
import com.fahimshahrierrasel.collectionnotifier.R;
import com.fahimshahrierrasel.collectionnotifier.adapter.OptionAdapter;
import com.fahimshahrierrasel.collectionnotifier.model.Bin;
import com.fahimshahrierrasel.collectionnotifier.model.Option;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinDetailsFragment extends Fragment {
    /**
     * Android Views
     **/
    ProgressBar progressBarBin;
    TextView textViewBinStatusText;
    TextView buttonEditBin;
    TextView buttonTuneBin;
    Switch switchBinStatus;
    TextView textViewSwitchBinStatusText;
    RecyclerView recyclerViewOptions;
    private Socket mSocket;
    private String id;
    private FragmentActivity activity;
    private String TAG = getClass().getSimpleName();
    private Bin currentBin;

    public BinDetailsFragment() {
    }

    public static BinDetailsFragment newInstance(String id) {
        BinDetailsFragment binDetailsFragment = new BinDetailsFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        binDetailsFragment.setArguments(args);
        return binDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bin_details, container, false);
        bindViews(root);

        activity = getActivity();

        if (getArguments() != null)
            id = getArguments().getString("id");

        CollectionNotifier app = (CollectionNotifier) activity.getApplication();
        mSocket = app.getSocket();
        mSocket.connect();
        Gson gson = new Gson();

        mSocket.on("take_bin", args -> activity.runOnUiThread(() -> {
            JSONObject jsonObject = (JSONObject) args[0];
            Bin bin = gson.fromJson(jsonObject.toString(), Bin.class);
            if (id.equals(bin.getId())) {
                currentBin = bin;
                populateView(bin);
                populateRecyclerView(bin);

            }
        }));

        mSocket.on("bin_status_updated", args -> activity.runOnUiThread(() -> {
            JSONObject jsonObject = (JSONObject) args[0];
            Bin bin = gson.fromJson(jsonObject.toString(), Bin.class);
            if (id.equals(bin.getId())) {
                if (bin.getStatus().equals("active")) {
                    textViewSwitchBinStatusText.setText(activity.getResources()
                            .getText(R.string.active));
                    switchBinStatus.setChecked(true);
                } else if (bin.getStatus().equals("inactive")) {
                    textViewSwitchBinStatusText.setText(activity.getResources()
                            .getText(R.string.inactive));
                    switchBinStatus.setChecked(false);
                }
            }
        }));

        mSocket.on("update_current_level", args -> activity.runOnUiThread(() -> {
            JSONObject jsonObject = (JSONObject) args[0];
            Bin bin = gson.fromJson(jsonObject.toString(), Bin.class);
            if (id.equals(bin.getId())) {
                updateCurrentBinLevel(bin);
            }
        }));

        mSocket.on("count_updated", args -> activity.runOnUiThread(() -> {
            JSONObject jsonObject = (JSONObject) args[0];
            Bin bin = gson.fromJson(jsonObject.toString(), Bin.class);
            if (id.equals(bin.getId())) {
                populateRecyclerView(bin);
            }
        }));

        switchBinStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textViewSwitchBinStatusText.setText(activity.getResources()
                        .getText(R.string.active));
                Log.d(TAG, id + " is now activating");
                mSocket.emit("update_bin_status", id, "active");
            } else {
                textViewSwitchBinStatusText.setText(activity.getResources()
                        .getText(R.string.inactive));
                Log.d(TAG, id + " is now deactivating");
                mSocket.emit("update_bin_status", id, "inactive");
            }
        });

        buttonEditBin.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_placeholder, BinUpdateFragment.newInstance(id))
                    .addToBackStack(null)
                    .commit();
        });

        buttonTuneBin.setOnClickListener(view -> {
            Map<String, Object> data = new HashMap<>();
            data.put("tuned", false);
            mSocket.emit("tune_bin", id, new JSONObject(data));
            Toast.makeText(activity, "Bin tuning now", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    private void updateCurrentBinLevel(Bin bin) {
        progressBarBin.setProgress(bin.getCurrentLevel());
        textViewBinStatusText.setText(String.format("%s%% Full", bin.getCurrentLevel()));
    }

    private void populateView(Bin bin) {
        progressBarBin.setProgress(bin.getCurrentLevel());
        textViewBinStatusText.setText(String.format("%s%% Full", bin.getCurrentLevel()));
        if (bin.getStatus().equals("active")) {
            textViewSwitchBinStatusText.setText(activity.getResources()
                    .getText(R.string.active));
            switchBinStatus.setChecked(true);
        } else {
            textViewSwitchBinStatusText.setText(activity.getResources()
                    .getText(R.string.inactive));
            switchBinStatus.setChecked(false);
        }
    }

    private void populateRecyclerView(Bin bin) {
        List<Option> options = new ArrayList<>();
        options.add(new Option(R.drawable.ic_location, "Location", "Open in Google Map"));
        options.add(new Option(R.drawable.ic_access_time, "Total Cleaned", String.valueOf(bin.getCount()) + " Times, Last Cleaned:" + String.valueOf(bin.getLastCleaned())));
        options.add(new Option(R.drawable.ic_trigger, "Trigger Pin", String.valueOf(bin.getTrigPin())));
        options.add(new Option(R.drawable.ic_location, "Echo Pin", String.valueOf(bin.getEchoPin())));
        options.add(new Option(R.drawable.ic_level, "Notification Level", String.valueOf(bin.getNotifyLevel())));
        options.add(new Option(R.drawable.ic_echo, "Physical Button", String.valueOf(bin.getButton())));
        options.add(new Option(R.drawable.ic_trigger, "Led Pin", String.valueOf(bin.getLed())));


        OptionAdapter optionAdapter = new OptionAdapter(options, this::onOptionClick);
        recyclerViewOptions.setAdapter(optionAdapter);
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    private void onOptionClick(Option option) {
        if (option.getText().contains("Map")) {
            if(currentBin != null) {
                String uri = "http://maps.google.com/maps?daddr=" + currentBin.getLatitude() + "," +
                        currentBin.getLongitude() + " (" + currentBin.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                if (activity != null)
                    activity.startActivity(intent);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (id != null)
            mSocket.emit("get_bin", id);
    }

    private void bindViews(View rootView) {
        progressBarBin = rootView.findViewById(R.id.progress_bar_bin);
        textViewBinStatusText = rootView.findViewById(R.id.text_view_bin_status_text);
        buttonEditBin = rootView.findViewById(R.id.button_edit_bin);
        buttonTuneBin = rootView.findViewById(R.id.button_tune_bin);
        switchBinStatus = rootView.findViewById(R.id.switch_bin_status);
        textViewSwitchBinStatusText = rootView.findViewById(R.id.text_view_switch_bin_status_text);
        recyclerViewOptions = rootView.findViewById(R.id.recycler_view_options);
    }
}
