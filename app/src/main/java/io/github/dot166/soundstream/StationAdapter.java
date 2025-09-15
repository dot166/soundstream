package io.github.dot166.soundstream;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.github.dot166.jlib.registry.RegistryHelper;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    private List<RegistryHelper.Object> stations;

    public StationAdapter(List<RegistryHelper.Object> list) {
        this.stations = list;
    }

    public List<RegistryHelper.Object> getStationList() {
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

        RegistryHelper.Object currentStation = stations.get(position);

        viewHolder.stationName.setText(currentStation.getName());

        if (currentStation.getLogoUrl() != null && !currentStation.getUrl().isEmpty()) {
            Glide.with(viewHolder.logo).load(currentStation.getLogoUrl()).into(viewHolder.logo);
        }

        viewHolder.itemView.setOnClickListener(view -> {
            RadioActivity.playAudio(RayoHandler.genUrl(currentStation.getUrl()), view.getContext(), currentStation.getLogoUrl(), currentStation.getName());
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