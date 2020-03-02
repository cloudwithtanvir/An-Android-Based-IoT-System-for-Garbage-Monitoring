package com.fahimshahrierrasel.collectionnotifier;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fahimshahrierrasel.collectionnotifier.adapter.BinAdapter;
import com.fahimshahrierrasel.collectionnotifier.fragment.HomeFragment;
import com.fahimshahrierrasel.collectionnotifier.model.Bin;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, HomeFragment.newInstance())
                .commit();
    }
}
