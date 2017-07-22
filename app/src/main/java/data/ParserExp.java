package data;

import java.util.ArrayList;

import model.ParserData;

import static android.text.TextUtils.indexOf;
import static android.text.TextUtils.split;

/**
 * Created by zigin on 11.07.2017.
 */

public class ParserExp {

    public static final String DELIMITER_BASE = "=";
    public static final String DELIMITER_CONTEXT = "//";


    public static final int TYPE_BODY = 1;
    public static final int TYPE_DEF_EN = 2;
    public static final int TYPE_DEF_RU = 3;
    public static final int TYPE_SYNONYM = 4;

    public static final int TYPE_BODY_DEF = 5;
    public static final int TYPE_BODY_CONTEXT = 6;
    public static final int TYPE_BODY_SLASH = 7;
    public static final int TYPE_TRASCRIPTION = 8;
    public static final int TYPE_SBD = 9;
    public static final int TYPE_COUNTRY = 10;
    public static final int TYPE_DEF_DEF = 11;

    public static ArrayList<ParserData> getFirstParse(String text) {
        ArrayList<ParserData> mArrayData = new ArrayList<>();
        //String sExp="";
        ArrayList<String> arrayListSynonym = new ArrayList<>();
        ArrayList<String> arrayListDefEng = new ArrayList<>();
        ArrayList<String> arrayListDefRu = new ArrayList<>();

        String[] splitArray = text.split(DELIMITER_BASE);
        //sExp = splitArray[0];

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
            mArrayData.add(new ParserData(TYPE_DEF_EN, defEng));
        }
        for (String defRu : arrayListDefRu) {
            mArrayData.add(new ParserData(TYPE_DEF_RU, defRu));
        }
        for (String synonym : arrayListSynonym) {
            mArrayData.add(new ParserData(TYPE_SYNONYM, synonym));
        }

        return mArrayData;

    }

//    public static ArrayList<> getBodyParse(String text) {
//
//    }

    /**
     * Return substrings that are between both separators.
     *
     * @param text       Method splits this string
     * @param regexStart First regex. Separation starts from it.
     * @param regexEnd   Second regex. Separation ends on it.
     * @return String[] array. These substrings are satisfied with the search conditions.
     * Else, an empty string is returned.
     */
    public String[] getStrBetweenStr(String text, String regexStart, String regexEnd) {
        String regex = regexStart + "|" + regexEnd;

        String[] splitArray = text.split(regex);

        if (splitArray.length < 3) {
            String[] string = new String[1];
            string[0] = "";
            return string;
        }

        return getBetweenArray(splitArray, (splitArray.length - 1) / 2);
    }

    /**
     * @param text       Method splits this string
     * @param regexStart First regex. Separation starts from it.
     * @param regexEnd   Second regex. Separation ends on it.
     * @param limit      Limit numbers return substrings
     * @return String[] array. These substrings are satisfied with the search conditions.
     * If the number of found substrings is less than the limit, then empty strings are returned to the limit.
     */
    public static String[] getStrBetweenStr(String text, String regexStart, String regexEnd, int limit) {
        String regex = regexStart + "|" + regexEnd;

        String[] splitArray = text.split(regex);

        if (splitArray.length < 2) {
            String[] string = new String[limit];

            for (int i = 0; i < limit; i++) {
                string[i] = "";
            }

            return string;
        }

        return getBetweenArray(splitArray, limit);
    }

    private static String[] getBetweenArray(String[] splitArray, int limit) {
        String[] betweenArray = new String[limit];

        for (int k = 0; k < limit; k++) {
            betweenArray[k] = "";
        }

        for (int i = 1, j = 0; i < splitArray.length; i++) {
            if (j == limit) break;
            if (i % 2 != 0 && !splitArray[i].isEmpty()) {
                betweenArray[j++] = splitArray[i];
            }
        }
        return betweenArray;
    }

    /**
     * Returns previous words that are before the words in the array
     *
     * @param body            String
     * @param afterWordsArray array with next words
     * @param startOffset     start offset of search previous word.
     * @return array previous words
     */
    public static String[] getPreviousWordsArray(String body, String[] afterWordsArray, int startOffset) {

        String[] clickArray = new String[afterWordsArray.length];

        for (int k = 0; k < afterWordsArray.length; k++) {
            clickArray[k] = "";
        }

        int offset;
        String word;
        for (int i = 0; i < afterWordsArray.length; i++) {

            word = "";

            if (afterWordsArray[i].isEmpty()) {
                continue;
            }

            offset = body.indexOf(afterWordsArray[i]);
            if (offset > 1) {
                offset = offset - 1 - startOffset;
            } else {
                continue;
            }
            // the next char after the previous word
            if (body.charAt(offset) == ' ') {
                for (int j = offset - 1; j >= 0; j--) {

                    if (body.charAt(j) == ' ' && j == offset - 1) {
                        continue;
                    }
                    if (body.charAt(j) != ' ') {
                        word += body.charAt(j);
                    } else {
                        break;
                    }
                }
            }
            clickArray[i] = new StringBuilder(word).reverse().toString();
        }
        return clickArray;
    }

    public static String[] frameStringArray(String[] array, String firstFrame, String lastFrame) {
        for (int i = 0; i < array.length; i++) {
            if (!array[i].isEmpty()) {
                array[i] = firstFrame + array[i] + lastFrame;
            }
        }
        return array;
    }

    public static String replaceArrayStr(String text,
                                         String[] targetArray,
                                         String firstFrame,
                                         String lastFrame,
                                         String replacement) {
        for (int i = 0; i < targetArray.length; i++) {
            text = text.replace(firstFrame + targetArray[i] + lastFrame, replacement);
        }
        return text.replace("  ", " ");
    }

}
