package io.github.dot166.soundstream;

import android.util.Log;

/**
 * This class is for handling radio stations owned by <a href="https://en.wikipedia.org/wiki/Bauer_Media_Audio_UK">Bauer Media Audio UK</a> that use <a href="https://www2.hellorayo.co.uk/">Rayo</a> for internet streaming.
 * <br>
 * this has only been tested on the free to air stations and should only work on such as people accessing premium stations when when they have not paid for accessing them is piracy and is illegal under <a href="https://en.wikipedia.org/wiki/Copyright,_Designs_and_Patents_Act_1988">The Copyright, Designs and Patents Act 1988</a> and <a href="https://en.wikipedia.org/wiki/Digital_Economy_Act_2010">the Digital Economy Act 2010</a>.
 * <br>
 * USE AT YOUR OWN RISK, i (._______166) am not responsible for your usage of this code and there is a chance that this could break at any time due to Bauer Media Audio UK changing something in rayo that breaks this.
 * (Bauer please don't sue me)
 */
public class RayoHandler {
    /**
     * Generates the final url with key for rayo stations
     * @param url the input url
     * @return the final url, if the input url is not a rayo station this will be the input url
     * (Bauer please don't sue me)
     */
    public static String genUrl(String url) {
        if (url == null) {
            return null;
        }
        if (url.toUpperCase().startsWith("https://stream-al.hellorayo.co.uk".toUpperCase()) || url.toUpperCase().startsWith("stream-al.hellorayo.co.uk".toUpperCase()) || url.toUpperCase().startsWith("http://stream-al.hellorayo.co.uk".toUpperCase())) {
            if (url.contains("aw_0_1st.skey=")) {
                Log.i("RayoHandler", "parameters are present in the url, replacing all parameters");
                url = url.replace(url.split("\\?")[1], ""); // all parameters are replaced so that if any of the stations require any extra parameters in the url they would not work, this is an attempt to keep this working without allowing piracy or legal action against me (tldr. an anti piracy measure designed to keep this code working without me getting a cease and desist letter or get sued)
            }
            url = url.concat("?aw_0_1st.skey=" + String.valueOf(System.currentTimeMillis() / 1000));
        }
        return url;
    }
}
