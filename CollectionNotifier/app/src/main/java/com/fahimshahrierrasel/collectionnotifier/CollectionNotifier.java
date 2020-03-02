package com.fahimshahrierrasel.collectionnotifier;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class CollectionNotifier extends Application {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.108:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
