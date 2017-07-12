package data;

import android.util.Log;

import java.util.ArrayList;

import model.ParserData;

import static android.text.TextUtils.indexOf;
import static android.text.TextUtils.split;

/**
 * Created by zigin on 11.07.2017.
 */

public class ParserExp {

    public static final String DELIMITER = "=";

    public static final int TYPE_BODY = 1;
    public static final int TYPE_DEF_ENG = 2;
    public static final int TYPE_DEF_RUS = 3;
    public static final int TYPE_SYNONYM = 4;

    public static final int TYPE_BODY_DEF = 5;
    public static final int TYPE_BODY_CONTEXT = 6;
    public static final int TYPE_BODY_SLASH = 7;
    public static final int TYPE_BODY_TRASCRIPTION = 8;
    public static final int TYPE_BODY_SBD = 9;
    public static final int TYPE_DEF_COUNTRY = 10;
    public static final int TYPE_DEF_DEF =11;
    public static final int TYPE_DEF_SBD = 12;

    public static ArrayList<ParserData> getFirstParse (String text) {
        ArrayList<ParserData> mArrayData = new ArrayList<>();
        String sExp="";
        ArrayList<String> arrayListSynonym = new ArrayList<>();
        ArrayList<String> arrayListDefEng = new ArrayList<>();
        ArrayList<String> arrayListDefRu = new ArrayList<>();


        Log.d("ParserExp", "#####################");
        Log.d("ParserExp", "text = " + text);
        Log.d("ParserExp", "#####################");



        String[] splitArray = text.split(DELIMITER);
        sExp = splitArray[0];

        for (int i = 1; i < splitArray.length; i++) {

            if (splitArray[i].isEmpty()) {
                continue;
            } else {
                if (splitArray[i - 1].isEmpty()) {

                    if (i > 2) {
                        if (splitArray[i - 2].isEmpty()) {
                            arrayListDefRu.add(splitArray[i]);
                            continue;
                        }
                    }

                    arrayListSynonym.add(splitArray[i]);
                } else {
                    arrayListDefEng.add(splitArray[i]);
                }
            }
        }

        mArrayData.add(new ParserData(TYPE_BODY, splitArray[0]));
        for (String defEng : arrayListDefEng) {
            mArrayData.add(new ParserData(TYPE_DEF_ENG, defEng));
        }
        for (String defRu : arrayListDefRu) {
            mArrayData.add(new ParserData(TYPE_DEF_RUS, defRu));
        }
        for (String synonym : arrayListSynonym) {
            mArrayData.add(new ParserData(TYPE_SYNONYM, synonym));
        }

        return  mArrayData;

    }

//    public static ArrayList<> getBodyParse(String text) {
//
//    }
}
