package io.github.dot166.soundstream;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.github.dot166.jlib.app.MediaPlayerActivity;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    private List<RadioRegistryHelper.Station> stations;

    public StationAdapter(List<RadioRegistryHelper.Station> list) {
        this.stations = list;
    }

    public List<RadioRegistryHelper.Station> getStationList() {
        return stations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.station, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        RadioRegistryHelper.Station currentStation = stations.get(position);

        viewHolder.stationName.setText(currentStation.getStationName());

        if (currentStation.getStationLogoUrl() != null && !currentStation.getStationLogoUrl().isEmpty()) {
            Glide.with(viewHolder.logo).load(currentStation.getStationLogoUrl()).into(viewHolder.logo);
        }

        viewHolder.itemView.setOnClickListener(view -> {
            RadioActivity.playAudio(RayoHandler.genUrl(currentStation.getStationUrl()), view.getContext(), currentStation.getStationLogoUrl(), currentStation.getStationName());
        });
    }

    @Override
    public int getItemCount() {
        return stations == null ? 0 : stations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stationName;
        ImageView logo;

        public ViewHolder(View itemView) {

            super(itemView);
            stationName = itemView.findViewById(R.id.name);
            logo = itemView.findViewById(R.id.logo);
        }
    }
}