package h5adapter.broadcast;

import android.content.Context;
import android.content.Intent;

public interface BroadcastListener {

    void onReceive(Context context, Intent intent);
}
