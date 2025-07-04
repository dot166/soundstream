package io.github.dot166.soundstream;

import static io.github.dot166.soundstream.XmlHelper.readXmlFromFile;
import static io.github.dot166.soundstream.XmlHelper.writeXmlToFile;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RadioRegistryHelper {

    public static List<Station> getStationsFromRegistry(Context context) {
        //writeXmlToFile(context, "radioRegistry.xml");
        return readXmlFromFile(context, "radioRegistry.xml");
    }

    public static class Station {
        private String stationName;
        private String stationUrl;
        private String stationLogoUrl;

        public Station(Map<String, String> attributes) {
            stationName = attributes.get("stationName");
            stationUrl = attributes.get("stationUrl");
            stationLogoUrl = attributes.get("stationLogoUrl");
        }

        public String getStationLogoUrl() {
            return stationLogoUrl;
        }

        public String getStationName() {
            return stationName;
        }

        public String getStationUrl() {
            return stationUrl;
        }

        public void updateAttributes(Map<String, String> attributes) {
            stationName = attributes.get("stationName");
            stationUrl = attributes.get("stationUrl");
            stationLogoUrl = attributes.get("stationLogoUrl");
        }
    }
}
