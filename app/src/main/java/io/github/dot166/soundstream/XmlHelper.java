package io.github.dot166.soundstream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.dot166.jlib.utils.ErrorUtils;

public class XmlHelper {

    public static void writeXmlToFile(Context context, String fileName, List<RadioRegistryHelper.Station> stations) {
        try {
            // Create an XmlSerializer instance
            XmlSerializer serializer = Xml.newSerializer();
            // Open file output stream
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            // Initialize XML Serializer
            serializer.setOutput(writer);

            // Start XML document
            serializer.startDocument("UTF-8", true);

            // Start a root element
            serializer.startTag("", "stations");

            // Add child elements with data
            for (int i = 0; i < stations.size(); i++) {
                RadioRegistryHelper.Station station = stations.get(i);
                serializer.startTag("", "station");
                serializer.attribute("", "stationName", station.getStationName());
                serializer.attribute("", "stationUrl", station.getStationUrl());
                serializer.attribute("", "stationLogoUrl", station.getStationLogoUrl());
                serializer.endTag("", "station");
            }

            // End root element
            serializer.endTag("", "stations");

            // End the document
            serializer.endDocument();

            writer.close(); // Close the writer to save the file

        } catch (Exception e) {
            ErrorUtils.handle(e, context);
        }
    }

    public static List<RadioRegistryHelper.Station> readXmlFromFile(Context context, String fileName) {
        return readXmlFromFile(context, fileName, null);
    }

    public static List<RadioRegistryHelper.Station> readXmlFromFile(Context context, String fileName, Uri uri) {
        List<RadioRegistryHelper.Station> stationList = new ArrayList<>();
        try {
            // Open the XML file
            InputStream fis;
            if (uri != null) {
                fis = context.getContentResolver().openInputStream(uri);
            } else {
                fis = context.openFileInput(fileName);
            }
            InputStreamReader reader = new InputStreamReader(fis);

            // Create a new XML pull parser instance
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(reader);

            // Start parsing the XML file
            String str = parseXmlAsString(parser);
            String[] str_aar = str.split(";");
            for (int i = 0; i < str_aar.length; i++) {
                Map<String, String> attributes = new HashMap<>();
                String[] attrs = str_aar[i].replace("<station# ", "").replace("></station>", "").replace("# ", "#").split("#");
                for (int j = 0; j < attrs.length; j++) {
                    String[] key_value_pair = attrs[j].split("=");
                    String value;
                    if (key_value_pair.length == 1) {
                        value = "";// must be empty
                    } else {
                        value = key_value_pair[1];
                    }
                    attributes.put(key_value_pair[0], value);
                }
                stationList.add(new RadioRegistryHelper.Station(attributes));
            }
        } catch (Exception e) {
            ErrorUtils.handle(e, context);
        }
        return stationList;
    }

    private static String parseXmlAsString(@NonNull XmlPullParser in)
            throws XmlPullParserException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        // Some parsers may have already consumed the event that starts the
        // document, so we manually emit that event here for consistency
//        if (in.getEventType() == XmlPullParser.START_DOCUMENT) {
//            stringBuilder.append("<?xml version='1.0' encoding='").append(in.getInputEncoding()).append("' standalone='yes' ?>");
//        }

        while (true) {
            final int token = in.nextToken();
            switch (token) {
//                case XmlPullParser.START_DOCUMENT:
//                    stringBuilder.append("<?xml version='1.0' encoding='").append(in.getInputEncoding()).append("' standalone='yes' ?>");
//                    break;
                case XmlPullParser.END_DOCUMENT:
                    return stringBuilder.toString().replace("<stations>", "").replace("<station></station>;", "").replace(";</stations>;", "");
                case XmlPullParser.START_TAG:
                    stringBuilder.append("<").append(fixNamespace(in.getNamespace())).append(in.getName());
                    for (int i = 0; i < in.getAttributeCount(); i++) {
                        stringBuilder.append("# ").append(fixNamespace(in.getAttributeNamespace(i))).append(in.getAttributeName(i)).append("=").append(in.getAttributeValue(i));
                    }
                    stringBuilder.append(">");
                    break;
                case XmlPullParser.END_TAG:
                    stringBuilder.append("</").append(fixNamespace(in.getNamespace())).append(in.getName()).append(">;");
                    break;
//                case XmlPullParser.TEXT:
//                    stringBuilder.append(in.getText());
//                    break;
//                case XmlPullParser.CDSECT:
//                    out.cdsect(in.getText());
//                    break;
//                case XmlPullParser.ENTITY_REF:
//                    out.entityRef(in.getName());
//                    break;
//                case XmlPullParser.IGNORABLE_WHITESPACE:
//                    out.ignorableWhitespace(in.getText());
//                    break;
//                case XmlPullParser.PROCESSING_INSTRUCTION:
//                    out.processingInstruction(in.getText());
//                    break;
//                case XmlPullParser.COMMENT:
//                    out.comment(in.getText());
//                    break;
//                case XmlPullParser.DOCDECL:
//                    out.docdecl(in.getText());
//                    break;
                default:
                    Log.w("Xml", "Unknown or unsupported token " + token);
            }
        }
    }
    private static String fixNamespace(@Nullable String namespace) {
        if (namespace == null || namespace.isEmpty()) {
            return "";
        } else {
            return namespace + ":";
        }
    }
}
