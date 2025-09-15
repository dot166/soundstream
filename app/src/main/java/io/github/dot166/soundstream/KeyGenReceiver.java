package io.github.dot166.soundstream;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.Calendar;

import io.github.dot166.jlib.time.ReminderItem;
import io.github.dot166.jlib.utils.ErrorUtils;

public class KeyGenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RadioService rs = (RadioService) RadioService.getInstance();
        if (rs != null && rs.mediaSession != null) {
            ExoPlayer player = (ExoPlayer) rs.mediaSession.getPlayer();
            if (player.getCurrentMediaItem() != null && player.getCurrentMediaItem().localConfiguration != null) {
                String urltest = player.getCurrentMediaItem().localConfiguration.uri.toString();
                String url = RayoHandler.genUrl(urltest);

                MediaItem.Builder mIBuilder = new MediaItem.Builder();
                mIBuilder.setUri(url);
                // yes we just pass through the old metadata, should be fine right...
                mIBuilder.setMediaMetadata(MediaItem.fromUri(url).mediaMetadata.buildUpon().setArtworkUri(player.getMediaMetadata().artworkUri).setStation(player.getMediaMetadata().station).build());

                MediaItem mediaItem = mIBuilder.build();
                player.setMediaItem(mediaItem);
                player.prepare();
                player.play();
            }
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 4);
            ReminderItem reminderItem = new ReminderItem(cal.getTimeInMillis(), 1);
            new KeyGenScheduler(rs.getBaseContext()).schedule(reminderItem);
        }
    }
}
