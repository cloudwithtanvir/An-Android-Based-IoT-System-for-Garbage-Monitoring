package com.fahimshahrierrasel.collectionnotifier.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fahimshahrierrasel.collectionnotifier.CollectionNotifier;
import com.fahimshahrierrasel.collectionnotifier.R;
import com.fahimshahrierrasel.collectionnotifier.adapter.BinAdapter;
import com.fahimshahrierrasel.collectionnotifier.model.Bin;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView recyclerViewBins;
    FloatingActionButton fabCreateBin;
    private Socket mSocket;

    private String TAG = getClass().getSimpleName();
    private FragmentActivity activity;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        activity = getActivity();

        bindViews(root);

        CollectionNotifier app = (CollectionNotifier) getActivity().getApplication();
        mSocket = app.getSocket();
        mSocket.connect();
        Gson gson = new Gson();

        mSocket.on("take_all", args -> {
            activity.runOnUiThread(() -> {
                List<Bin> bins = new ArrayList<>();
                JSONArray array = (JSONArray) args[0];
                for (int i = 0; i < array.length(); i++) {
                    try {
                        Bin bin = gson.fromJson(array.getJSONObject(i).toString(), Bin.class);
                        bins.add(bin);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                populateRecyclerView(bins);
            });
        });


        fabCreateBin.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_placeholder, CreateBinFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        mSocket.emit("get_all", new JSONObject());
    }

    private void populateRecyclerView(List<Bin> bins) {
        BinAdapter binAdapter = new BinAdapter(bins, this::onItemClick);
        recyclerViewBins.setAdapter(binAdapter);
        recyclerViewBins.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    public void onItemClick(Bin bin) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, BinDetailsFragment.newInstance(bin.getId()))
                .addToBackStack(null)
                .commit();
    }

    private void bindViews(View rootView) {
        recyclerViewBins = rootView.findViewById(R.id.recycler_view_bins);
        fabCreateBin = rootView.findViewById(R.id.fab_create_bin);
    }
}
