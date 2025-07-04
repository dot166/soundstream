package io.github.dot166.soundstream;

import static io.github.dot166.soundstream.RadioRegistryHelper.getStationsFromRegistry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import io.github.dot166.jlib.LIBAboutActivity;
import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.utils.AppUtils;
import io.github.dot166.jlib.utils.ErrorUtils;
import io.github.dot166.jlib.widget.MiniPlayer;

public class MainActivity extends jActivity {

    private RecyclerView mRecyclerView;
    private StationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.action_bar));
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new StationAdapter(getStationsFromRegistry(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        //RadioActivity.playAudio(RayoHandler.genUrl("https://stream-al.hellorayo.co.uk/hitsradiopride.aac"), this, "https://media.bauerradio.com/image/upload/c_crop,g_custom/v1713801146/brand_manager/stations/qkt29lotqatw0dnhoxvl.jpg", "Hits Radio PRIDE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MiniPlayer)findViewById(R.id.player)).onResumeHook();
        mAdapter.getStationList().clear();
        mAdapter.getStationList().addAll(getStationsFromRegistry(this));
        mAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.regedit) {
            try {
                startActivity(new Intent(this, RegistryEditActivity.class));
            } catch (Exception e) {
                ErrorUtils.handle(e, this);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
