package io.github.dot166.soundstream;

import static android.widget.Toast.LENGTH_LONG;
import static io.github.dot166.soundstream.XmlHelper.readXmlFromFile;
import static io.github.dot166.soundstream.XmlHelper.writeXmlToFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.utils.ErrorUtils;

public class RegistryEditActivity extends jActivity {

    List<RadioRegistryHelper.Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regedit);
        setSupportActionBar(findViewById(R.id.action_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TabLayout tabLayout = findViewById(R.id.tabs);
        buildTabs(tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RadioRegistryHelper.Station station = (RadioRegistryHelper.Station) tab.getTag();
                ((EditText)findViewById(R.id.name_input)).setText(station.getStationName());
                ((EditText)findViewById(R.id.url_input)).setText(station.getStationUrl());
                ((EditText)findViewById(R.id.logo_url_input)).setText(station.getStationLogoUrl());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("Registry", "No logic needed for onTabUnselected()");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("Registry", "What are you doing???");
            }
        });
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                HashMap<String, String> attrs = new HashMap<>();
                attrs.put("stationName", ((EditText)findViewById(R.id.name_input)).getText().toString());
                attrs.put("stationUrl", ((EditText)findViewById(R.id.url_input)).getText().toString());
                attrs.put("stationLogoUrl", ((EditText)findViewById(R.id.logo_url_input)).getText().toString());
                if (tab.getTag() != null) {
                    if (stations.contains(tab.getTag()) && ((EditText)findViewById(R.id.url_input)).getText().toString().isEmpty()) {
                        stations.remove(tab.getTag());
                        writeXmlToFile(v.getContext(), "radioRegistry.xml", stations);
                        rebuildTabs(tabLayout);
                        return;
                    } else if (stations.contains(tab.getTag()) && !((EditText)findViewById(R.id.url_input)).getText().toString().isEmpty()) {
                        RadioRegistryHelper.Station station = (RadioRegistryHelper.Station) tab.getTag();
                        station.updateAttributes(attrs);
                        stations.remove(tab.getTag());
                        stations.add(station);
                        writeXmlToFile(v.getContext(), "radioRegistry.xml", stations);
                        rebuildTabs(tabLayout);
                        return;
                    }
                }
                RadioRegistryHelper.Station station = new RadioRegistryHelper.Station(attrs);
                stations.add(station);
                writeXmlToFile(v.getContext(), "radioRegistry.xml", stations);
                rebuildTabs(tabLayout);
            }
        });
    }

    protected void rebuildTabs(TabLayout tabLayout) {
        tabLayout.removeAllTabs();
        buildTabs(tabLayout);
    }

    protected void buildTabs(TabLayout tabLayout) {
        TabLayout.Tab newTab = tabLayout.newTab();
        newTab.setText(R.string.add_new_station_to_registry);
        newTab.setTag(new RadioRegistryHelper.Station(new HashMap<>()));
        tabLayout.addTab(newTab);
        newTab.select();
        stations = readXmlFromFile(this, "radioRegistry.xml");
        for (int i = 0; i < stations.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            RadioRegistryHelper.Station station = stations.get(i);
            tab.setTag(station);
            tab.setText(station.getStationName());
            tabLayout.addTab(tab);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else if (itemId == R.id.import_xml) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/xml"); // Or any other MIME type you need
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 61016);
            return true;
        } else if (itemId == R.id.export_xml) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/xml");
            intent.putExtra(Intent.EXTRA_TITLE, "radioRegistry.xml"); // default filename
            startActivityForResult(intent, 888);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 61016 && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            stations = readXmlFromFile(this, "", fileUri);
            writeXmlToFile(this, "radioRegistry.xml", stations);
            rebuildTabs(findViewById(R.id.tabs));
        } else if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                try {
                    // Grab current stations from local registry
                    List<RadioRegistryHelper.Station> currentStations =
                            XmlHelper.readXmlFromFile(this, "radioRegistry.xml");

                    String tempName = "temp_export.xml";
                    XmlHelper.writeXmlToFile(this, tempName, currentStations);

                    File tempFile = new File(getFilesDir(), tempName);

                    // Copy temp file into SAF-chosen destination
                    try (InputStream in = new FileInputStream(tempFile);
                         OutputStream out = getContentResolver().openOutputStream(fileUri)) {

                        if (out != null) {
                            byte[] buffer = new byte[8192];
                            int len;
                            while ((len = in.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                            }
                            out.flush();
                        }
                    }

                    // Delete temp file after use
                    tempFile.delete();

                    Toast.makeText(this, "Registry exported successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    ErrorUtils.handle(e, this);
                }
            } else {
                Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.import_menu, menu);
        return true;
    }

}
