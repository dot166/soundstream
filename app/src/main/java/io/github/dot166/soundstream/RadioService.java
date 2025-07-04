package io.github.dot166.soundstream;

import static android.widget.Toast.LENGTH_LONG;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import io.github.dot166.jlib.app.MediaPlayerActivity;
import io.github.dot166.jlib.service.MediaPlayerService;
import io.github.dot166.jlib.time.ReminderItem;

public class RadioService extends MediaPlayerService {

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onCreate() {
        super.onCreate();
        ReminderItem reminderItem = new ReminderItem(System.currentTimeMillis(), 1);
        new KeyGenScheduler(this).schedule(reminderItem);
        this.mediaSession.setSessionActivity(PendingIntent.getActivity(this, 1, new Intent(this, RadioActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
    }
}
