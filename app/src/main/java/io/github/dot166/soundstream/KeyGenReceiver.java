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

import io.github.dot166.jlib.utils.ErrorUtils;

public class KeyGenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RadioService rs = (RadioService) RadioService.getInstance();
        if (rs != null && rs.mediaSession != null) {
            ExoPlayer player = (ExoPlayer) rs.mediaSession.getPlayer();
            if (player.getCurrentMediaItem() != null && player.getCurrentMediaItem().localConfiguration != null) {
                String urltest = player.getCurrentMediaItem().localConfiguration.uri.toString();
                String url = urltest.replace(urltest.split("aw_0_1st.skey=")[1], String.valueOf(System.currentTimeMillis() / 1000));

                MediaItem.Builder mIBuilder = new MediaItem.Builder();
                mIBuilder.setUri(url);
                // yes we just pass through the old metadata, should be fine right...
                mIBuilder.setMediaMetadata(MediaItem.fromUri(url).mediaMetadata.buildUpon().setArtworkUri(player.getMediaMetadata().artworkUri).setStation(player.getMediaMetadata().station).build());

                MediaItem mediaItem = mIBuilder.build();
                player.setMediaItem(mediaItem);
                player.prepare();
                player.play();
            }
        }
    }
}
