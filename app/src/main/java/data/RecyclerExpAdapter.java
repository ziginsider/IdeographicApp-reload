package data;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;
import com.tooltip.Tooltip;
import com.tuyenmonkey.textdecorator.TextDecorator;
import com.tuyenmonkey.textdecorator.callback.OnTextClickListener;

import java.util.ArrayList;
import java.util.Objects;

import app.AppController;
import io.github.ziginsider.ideographicapp.R;
import model.Expressions;
import model.FavoriteExpressions;
import model.ParserData;

import static data.ParserExp.*;
import static io.github.ziginsider.ideographicapp.R.style.Theme_AppCompat_Dialog_Alert;

/**
 * Created by zigin on 26.10.2016.
 */

public class RecyclerExpAdapter extends RecyclerView.Adapter<RecyclerExpAdapter.ViewHolder> {

    //private int clickedPosition = -1;
    private ArrayList<Expressions> mExpList;
    private DatabaseHandlerInner dbConnInner;
    String regex = "";
    String target = "";

    int colorWordPreviousTrans;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textBody;
        private TextView textSynonym;
        private TextView textDefEn;
        private TextView textDefRu;

        //private RelativeLayout relativeLayout;
        private RelativeLayout relativeSynonym;
        private RelativeLayout relativeExplanatory;

        private ImageView imgFavoriteAdd;

        public ViewHolder(View view) {
            super(view);
            this.textBody = (TextView) view.findViewById(R.id.txt_view_body_exp);
            this.textSynonym = (TextView) view.findViewById(R.id.txt_view_synonym);
            this.textDefEn = (TextView) view.findViewById(R.id.txt_view_explanatory_en);
            this.textDefRu = (TextView) view.findViewById(R.id.txt_view_explanatory_ru);

            //this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_exp_content);
            this.relativeExplanatory = (RelativeLayout) view.findViewById(R.id.relative_explanatory_body);
            this.relativeSynonym = (RelativeLayout) view.findViewById(R.id.relative_synonyms_body);
            this.imgFavoriteAdd = (ImageView) view.findViewById(R.id.img_favorite_add);
        }
    }

    public RecyclerExpAdapter(ArrayList<Expressions> exp) {
        this.mExpList = exp;
        //this.countItems = exp.size();

    }

    @Override
    public RecyclerExpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_exp_item, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...
//        dbConnInner = new DatabaseHandlerInner(parent.getContext());

        //RecyclerExpAdapter.ViewHolder vh = new RecyclerExpAdapter.ViewHolder(v);
        dbConnInner = AppController.getInstance().getSQLiteConnectionInner();
        //dbConnInner = new DatabaseHandlerInner(parent.getContext());

        ///////////

        colorWordPreviousTrans = ContextCompat.getColor(parent.getContext(), R.color.word_previous_trans);

        ////////////


        return new RecyclerExpAdapter.ViewHolder(v);
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(final RecyclerExpAdapter.ViewHolder holder, final int position) {

        final Expressions mCurrentExpItem = mExpList.get(position);

        //holder.textBody.setText(mCurrentExpItem.getExpText());

//        if (position == clickedPosition){
//            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
//        } else {
//            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_exp_new);
//        }

        if (dbConnInner.isExpInFavoriteList(mCurrentExpItem.getExpId())) {
            holder.imgFavoriteAdd.setImageResource(R.drawable.bookmark_ok);
        } else {
            holder.imgFavoriteAdd.setImageResource(R.drawable.bookmark_no);
        }

        holder.relativeExplanatory.setVisibility(View.GONE);
        holder.relativeSynonym.setVisibility(View.GONE);
        holder.textDefRu.setVisibility(View.GONE);
        holder.textDefEn.setVisibility(View.GONE);

        ArrayList<ParserData> parserList = ParserExp.getFirstParse(mCurrentExpItem.getExpText());

        String body = "";
        String synonym = "";
        String defEn = "";
        String defRu = "";

        for (ParserData text : parserList) {

            int type = text.getType();
            String content = text.getText();

//            Log.d("RecyclerExpAdapter", "+++++++++++++++++");
//            Log.d("RecyclerExpAdapter", "type: " + type);
//            Log.d("RecyclerExpAdapter", "content: " + content);
            switch (type) {
                case TYPE_BODY:
                    body = content;
                    break;
                case TYPE_DEF_EN:
                    holder.relativeExplanatory.setVisibility(View.VISIBLE);
                    holder.textDefEn.setVisibility(View.VISIBLE);
                    defEn = defEn + "\n" + content;
                    break;
                case TYPE_DEF_RU:
                    holder.relativeExplanatory.setVisibility(View.VISIBLE);
                    holder.textDefRu.setVisibility(View.VISIBLE);
                    defRu = defRu + "\n" + content;
                    break;
                case TYPE_SYNONYM:
                    holder.relativeSynonym.setVisibility(View.VISIBLE);
                    synonym = synonym + "\n" + content;
                    break;
            }
        }


////        String str = "(It is) such an easy thing. (Yes), I (agree with) (you)";
//        String str = "[It is] such an easy thing. [[Yes]], I [agree with] [you]";
////        String regexx = "\\[{1}|\\]{1}"; // [ ]
////        String regexx = "\\[{1}|\\]{1}";   // [[ ]]
//        String regexx = "\\[{1}\\w+\\]{1}";   // [[ ]]
////        String regexx = "\\({1}|\\){1}"; // ( )
//        String[] array = str.split(regexx);
//        Log.d("RecyclerExpAdapter", "********** str = \"" + str + "\"");
//        Log.d("RecyclerExpAdapter", " ************** array.length: " + array.length + "\n");
//        for (int i = 0; i < array.length; i++) {
//            Log.d("RecyclerExpAdapter", "************** [" + i + "] = \"" + array[i] + "\"");
//        }

        // get (bla-bla-bla)
//        final String[] determArray = getDetermArray(body);

        // ( )
        final String[] determArray = ParserExp.getStrBetweenStr(body, "\\({1}", "\\){1}", 3);
        String[] clickableWordsArray = ParserExp.getPreviousWordsArray(body, determArray, 1);

        // delete (bla-bla-bla)
        // String replace;
        body = ParserExp.replaceArrayStr(body, determArray, "(", ")", "");

        // ((bla-bla-bla)) replace to (bla-bla-bla)
        body = body.replace("((", "(");
        body = body.replace("))", ")");

        // ( )
        String[] explainArray = ParserExp.getStrBetweenStr(body, "\\(", "\\)", 3);
        explainArray = ParserExp.frameStringArray(explainArray, "(", ")");

        //transcription [sdfsdfsdfd]
        String[] transWordsArray = ParserExp.getStrBetweenStr(body, "\\[{1}", "\\]{1}", 3);

        String[] clickableTransArray = ParserExp.getPreviousWordsArray(body, transWordsArray, 1);

        transWordsArray = ParserExp.frameStringArray(transWordsArray, "[", "]");

        body = ParserExp.replaceArrayStr(body, transWordsArray, "", "", "");

        // example: "tra-ta-ta //target targe target// tra-tat-ta"
        int[] posDoubleSlashArray = ParserExp.getPositionsBetweenStr(body, "//", 6);
        body = body.replace("//", "");

        //get positions " s ", "s/s", "sbd"
        //int[] posSbdArray = getPositionsSbd(body);

        // final [ ]
        final String[] finalTransWordsArray = transWordsArray;

        TextDecorator
                .decorate(holder.textBody, body)
                // sbd
//                .setTextColor(android.R.color.holo_blue_light, posSbdArray[0], posSbdArray[1])
//                .setTextColor(android.R.color.holo_blue_light, posSbdArray[2], posSbdArray[3])
//                .setTextColor(android.R.color.holo_blue_light, posSbdArray[4], posSbdArray[5])
                // // //
                .setTextStyle(Typeface.ITALIC, posDoubleSlashArray[0], posDoubleSlashArray[1])
                .setTextStyle(Typeface.ITALIC, posDoubleSlashArray[2], posDoubleSlashArray[3])
                .setTextStyle(Typeface.ITALIC, posDoubleSlashArray[4], posDoubleSlashArray[5])
                // [ ]
                .setTextColor(android.R.color.holo_green_dark, transWordsArray)
                //.setTextColor(android.R.color.holo_red_dark, clickableTransArray)
                // [ ] previous word clickable
                // 1
                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!finalTransWordsArray[0].isEmpty()) {
                            Tooltip tooltip = new Tooltip.Builder(widget)
                                    .setText(finalTransWordsArray[0])
                                    .setGravity(Gravity.TOP)
                                    .setCornerRadius(8f)
                                    .setTextColor(Color.WHITE)
                                    .setPadding(18f)
                                    .setCancelable(true)
                                    .setDismissOnClick(true)
                                    .show();
                        }
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(colorWordPreviousTrans);
                        ds.setUnderlineText(false);
                    }
                }, clickableTransArray[0])
//                .makeTextClickable(new OnTextClickListener() {
//                    @Override
//                    public void onClick(View view, String text) {
//                        if (!finalTransWordsArray[0].isEmpty()) {
//                            Tooltip tooltip = new Tooltip.Builder(view)
//                                    .setText(finalTransWordsArray[0])
//                                    .setGravity(Gravity.TOP)
//                                    .setCornerRadius(8f)
//                                    .setTextColor(Color.WHITE)
//                                    .setPadding(18f)
//                                    .setCancelable(true)
//                                    .setDismissOnClick(true)
//                                    .show();
//                        }
//                    }
//                }, false, clickableTransArray[0])
                // 2
                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!finalTransWordsArray[1].isEmpty()) {
                            Tooltip tooltip = new Tooltip.Builder(widget)
                                    .setText(finalTransWordsArray[1])
                                    .setGravity(Gravity.TOP)
                                    .setCornerRadius(8f)
                                    .setTextColor(Color.WHITE)
                                    .setPadding(18f)
                                    .setCancelable(true)
                                    .setDismissOnClick(true)
                                    .show();
                        }
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(colorWordPreviousTrans);
                        ds.setUnderlineText(false);
                    }
                }, clickableTransArray[1])
                // 3
                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!finalTransWordsArray[2].isEmpty()) {
                            Tooltip tooltip = new Tooltip.Builder(widget)
                                    .setText(finalTransWordsArray[2])
                                    .setGravity(Gravity.TOP)
                                    .setCornerRadius(8f)
                                    .setTextColor(Color.WHITE)
                                    .setPadding(18f)
                                    .setCancelable(true)
                                    .setDismissOnClick(true)
                                    .show();
                        }
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(colorWordPreviousTrans);
                        ds.setUnderlineText(false);
                    }
                }, clickableTransArray[2])
                // (( ))
                //.setAbsoluteSize(26, explainArray)
                .setTextColor(R.color.defenition, explainArray)
                // ( )
                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!determArray[0].isEmpty()) {
                            View dialog = LayoutInflater.from(widget.getContext()).
                                    inflate(R.layout.dialog_defenition_word, null);
                            final SwipeDismissDialog swipeDismissDialog = new SwipeDismissDialog.Builder(widget.getContext())
                                    .setView(dialog)
                                    .build()
                                    .show();

                            TextView textDef = (TextView) dialog.findViewById(R.id.text_def_word);
                            textDef.setText(determArray[0]);

                            Button addButton = (Button) dialog.findViewById(R.id.button_def_word_dismiss);
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    swipeDismissDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                    }
                }, clickableWordsArray[0])

                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!determArray[1].isEmpty()) {
                            View dialog = LayoutInflater.from(widget.getContext()).
                                    inflate(R.layout.dialog_defenition_word, null);
                            final SwipeDismissDialog swipeDismissDialog = new SwipeDismissDialog.Builder(widget.getContext())
                                    .setView(dialog)
                                    .build()
                                    .show();

                            TextView textDef = (TextView) dialog.findViewById(R.id.text_def_word);
                            textDef.setText(determArray[1]);

                            Button addButton = (Button) dialog.findViewById(R.id.button_def_word_dismiss);
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    swipeDismissDialog.dismiss();
                                }
                            });

                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                    }
                }, clickableWordsArray[1])

                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!determArray[2].isEmpty()) {
                            View dialog = LayoutInflater.from(widget.getContext()).
                                    inflate(R.layout.dialog_defenition_word, null);
                            final SwipeDismissDialog swipeDismissDialog = new SwipeDismissDialog.Builder(widget.getContext())
                                    .setView(dialog)
                                    .build()
                                    .show();

                            TextView textDef = (TextView) dialog.findViewById(R.id.text_def_word);
                            textDef.setText(determArray[2]);

                            Button addButton = (Button) dialog.findViewById(R.id.button_def_word_dismiss);
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    swipeDismissDialog.dismiss();
                                }
                            });

                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                    }
                }, clickableWordsArray[2])
                // build
                .build();


        /* definition area */
        defEn = defEn.trim();

        //replace [[ ]] (( ))
        defEn = defEn.replace("[[", "((");
        defEn = defEn.replace("]]", "))");

        // get (bla-bla-bla) in def
        final String[] defArray = ParserExp.getStrBetweenStr(defEn, "\\({1}", "\\){1}", 3);
        clickableWordsArray = ParserExp.getPreviousWordsArray(defEn, defArray, 1);

        // delete (bla-bla-bla) in def
        // String replace;
        defEn = ParserExp.replaceArrayStr(defEn, defArray, "(", ")", "");

        // ((bla-bla-bla)) replace to (bla-bla-bla) in def
        defEn = defEn.replace("((", "(");
        defEn = defEn.replace("))", ")");

        // redacting (bla-bla-bla) in def
        explainArray = ParserExp.getStrBetweenStr(defEn, "\\(", "\\)", 3);
        explainArray = ParserExp.frameStringArray(explainArray, "(", ")");

        //transcription [sdfsdfsdfd] in def
        transWordsArray = ParserExp.getStrBetweenStr(defEn, "\\[{1}", "\\]{1}", 3);
        transWordsArray = ParserExp.frameStringArray(transWordsArray, "[", "]");

        //get positions " s ", "s/s", "sbd"
        //posSbdArray = getPositionsSbd(defEn);


        TextDecorator
                .decorate(holder.textDefEn, defEn)
                // [ ]
                .setTextColor(R.color.def_trans, transWordsArray)
                // somebody
//                .setTextColor(android.R.color.holo_blue_dark, posSbdArray[0], posSbdArray[1])
//                .setTextColor(android.R.color.holo_blue_dark, posSbdArray[2], posSbdArray[3])
//                .setTextColor(android.R.color.holo_blue_dark, posSbdArray[4], posSbdArray[5])
                //def in def
                //.setAbsoluteSize(26, explainArray)
                .setTextColor(R.color.defenition, explainArray)
                // clickable
                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!defArray[0].isEmpty()) {
                            View dialog = LayoutInflater.from(widget.getContext()).
                                    inflate(R.layout.dialog_defenition_word, null);
                            final SwipeDismissDialog swipeDismissDialog = new SwipeDismissDialog.Builder(widget.getContext())
                                    .setView(dialog)
                                    .build()
                                    .show();

                            TextView textDef = (TextView) dialog.findViewById(R.id.text_def_word);
                            textDef.setText(defArray[0]);

                            Button addButton = (Button) dialog.findViewById(R.id.button_def_word_dismiss);
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    swipeDismissDialog.dismiss();
                                }
                            });                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                    }
                }, clickableWordsArray[0])
                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!defArray[1].isEmpty()) {
                            View dialog = LayoutInflater.from(widget.getContext()).
                                    inflate(R.layout.dialog_defenition_word, null);
                            final SwipeDismissDialog swipeDismissDialog = new SwipeDismissDialog.Builder(widget.getContext())
                                    .setView(dialog)
                                    .build()
                                    .show();

                            TextView textDef = (TextView) dialog.findViewById(R.id.text_def_word);
                            textDef.setText(defArray[1]);

                            Button addButton = (Button) dialog.findViewById(R.id.button_def_word_dismiss);
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    swipeDismissDialog.dismiss();
                                }
                            });

                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                    }
                }, clickableWordsArray[1])
                .makeTextClickable(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (!defArray[2].isEmpty()) {View dialog = LayoutInflater.from(widget.getContext()).
                                inflate(R.layout.dialog_defenition_word, null);
                            final SwipeDismissDialog swipeDismissDialog = new SwipeDismissDialog.Builder(widget.getContext())
                                    .setView(dialog)
                                    .build()
                                    .show();

                            TextView textDef = (TextView) dialog.findViewById(R.id.text_def_word);
                            textDef.setText(defArray[2]);

                            Button addButton = (Button) dialog.findViewById(R.id.button_def_word_dismiss);
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    swipeDismissDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                    }
                }, clickableWordsArray[2])
                .build();


        /* Russian area */
        holder.textDefRu.setText(defRu.trim());

        /* Synonym area */

        synonym = synonym.trim();

        // example: "tra-ta-ta //target targe target// tra-tat-ta"
        posDoubleSlashArray = ParserExp.getPositionsBetweenStr(synonym, "//", 6);
        synonym = synonym.replace("//", "");

//        holder.textSynonym.setText(synonym.trim());

        TextDecorator.decorate(holder.textSynonym, synonym)
                // // //
                .setTextStyle(Typeface.ITALIC, posDoubleSlashArray[0], posDoubleSlashArray[1])
                .setTextStyle(Typeface.ITALIC, posDoubleSlashArray[2], posDoubleSlashArray[3])
                .setTextStyle(Typeface.ITALIC, posDoubleSlashArray[4], posDoubleSlashArray[5])
                // build
                .build();


        //copy to clipboard
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(holder.textBody.getText(), holder.textBody.getText());
                clipboard.setPrimaryClip(clip);
                //set the position
                //clickedPosition = position;
                //notify the data has changed
                //notifyDataSetChanged();
                //notifyItemChanged(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                //clickedPosition = position;
                //notifyDataSetChanged();
                return true;
            }
        });

        holder.imgFavoriteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbConnInner.isExpInFavoriteList(mCurrentExpItem.getExpId())) {

                    Toast toast = Toast.makeText(v.getContext(),
                            "Removed from favorite list", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout toastContainer = (LinearLayout) toast.getView();
                    ImageView imgBookmarkRemove = new ImageView(v.getContext());
                    imgBookmarkRemove.setImageResource(R.drawable.bookmark_remove);
                    toastContainer.addView(imgBookmarkRemove, 0);
                    toast.show();

                    dbConnInner.deleteFavoriteExp(mCurrentExpItem.getExpId());

                } else {

                    Toast toast = Toast.makeText(v.getContext(),
                            "Added to favorite list", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout toastContainer = (LinearLayout) toast.getView();
                    ImageView imgBookmarkOk = new ImageView(v.getContext());
                    imgBookmarkOk.setImageResource(R.drawable.bookmark_ok_2);
                    toastContainer.addView(imgBookmarkOk, 0);
                    toast.show();

                    FavoriteExpressions favoriteExp = new FavoriteExpressions();
                    favoriteExp.setTextExp(mCurrentExpItem.getExpText());
                    favoriteExp.setIdExp(mCurrentExpItem.getExpId());
                    favoriteExp.setIdParentTopic(mCurrentExpItem.getExpParentId());

                    dbConnInner.addFavoriteExp(favoriteExp);

                }
                notifyDataSetChanged();
            }
        });

    }

    private String[] getClickableWordsArray(String body, String[] determArray) {

        String[] clickArray = new String[3];
        clickArray[0] = "";
        clickArray[1] = "";
        clickArray[2] = "";

        int offset;
        String word = "";

        for (int i = 0; i < determArray.length; i++) {

            if (determArray[i].isEmpty()) {
                continue;
            }

            offset = body.indexOf(determArray[i]);
            if (offset > 1) {
                offset -= 2;
            } else {
                continue;
            }

            // the next char after the clickable word
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

    private String[] getDetermArray(String body) {
        String[] determArray = new String[3];
        determArray[0] = "";
        determArray[1] = "";
        determArray[2] = "";

        regex = "\\({1}|\\){1}";
        String[] splitArray = body.split(regex);

        for (int i = 1, j = 0; i < splitArray.length; i++) {
            if (j == 3) break;
            if (i % 2 != 0 && !splitArray[i].isEmpty()) {
                determArray[j++] = splitArray[i];
            }
        }
        return determArray;
    }

    private int[] getPositionsInRegex(String regex, String text) {
        int[] posArray = new int[6];

        for (int j = 0; j < posArray.length; j++) {
            posArray[j] = 0;
        }

        String[] splitArray = text.split(regex);
        int offset = 0;
        int pos = 0;

        for (int i = 0; i < splitArray.length; i++) {
            if (splitArray[i].length() == 0) {
                continue;
            }
            if (i == 0) {
                offset += splitArray[i].length();
                continue;
            }
            if (i % 2 == 0) {
                offset += splitArray[i].length();
            } else {
                posArray[pos] = offset;
                posArray[pos + 1] = offset + splitArray[i].length();
                offset = posArray[pos + 1];
                pos += 2;
            }
        }
        return posArray;
    }

    private int[] getPositionsSbd(String body) {
        int[] number = new int[6];
        for (int j = 0; j < number.length; j++) {
            number[j] = 0;
        }

        int offset = 0;
        int offsetSS = 0;
        int offsetSbd = 0;
        int lastOffset = body.length() - 1;
        int sFirst;
        int sLast;
        int sMiddle;

        for (int i = 0; i < number.length; i += 2) {

            sFirst = body.indexOf("s ", offset);
            if (sFirst == 0) {
                number[i] = 0;
                number[i + 1] = 1;
                offset += 2;
                continue;
            }

            sLast = body.lastIndexOf(" s", lastOffset);
            if (sLast == body.length() - 2) {
                number[i] = sLast + 1;
                number[i + 1] = sLast + 2;
                lastOffset -= 2;
                continue;
            }

            sMiddle = body.indexOf(" s ", offset);
            if (sMiddle > -1) {
                number[i] = sMiddle + 1;
                number[i + 1] = sMiddle + 2;
                offset = sMiddle + 3;
                continue;
            }

            sMiddle = body.indexOf("s/s", offsetSS);
            if (sMiddle > -1) {
                if (sMiddle > 0) {
                    if (body.charAt(sMiddle - 1) != ' ') {
                        continue;
                    }
                }
                number[i] = sMiddle;
                number[i + 1] = sMiddle + 3;
                offsetSS = sMiddle + 4;
                continue;
            }

            sMiddle = body.indexOf("sbd", offsetSbd);
            if (sMiddle > -1) {
                number[i] = sMiddle;
                number[i + 1] = sMiddle + 3;
                offsetSbd = sMiddle + 4;
            }
        }
        return number;
    }

    @Override
    public int getItemCount() {
        return mExpList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        //dbConnInner.close();
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
