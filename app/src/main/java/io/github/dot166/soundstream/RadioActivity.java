package io.github.dot166.soundstream;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;

import io.github.dot166.jlib.app.MediaPlayerActivity;
import io.github.dot166.jlib.service.MediaPlayerService;

public class RadioActivity extends MediaPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void playAudio(String url, Context context, String drawUrl, String title) {
        Intent i = new Intent(context, RadioActivity.class);
        i.putExtra("uri", url);
        i.putExtra("drawableUrl", drawUrl);
        i.putExtra("title", title);
        context.startActivity(i);
    }

    @Override
    protected void addExtraMetadata(MediaItem.Builder mIdBuilder, MediaMetadata.Builder metadata) {
        metadata.setStation(getIntent().getStringExtra("title"));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
