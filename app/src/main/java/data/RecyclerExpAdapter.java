package data;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;
import model.Expressions;
import model.FavoriteExpressions;
import model.ParserData;

import static data.ParserExp.*;

/**
 * Created by zigin on 26.10.2016.
 */

public class RecyclerExpAdapter extends RecyclerView.Adapter<RecyclerExpAdapter.ViewHolder> {

    //private int clickedPosition = -1;
    private ArrayList<Expressions> mExpList;
    private InitalDatabaseHandler dbInital;

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
        dbInital = new InitalDatabaseHandler(parent.getContext()); // TODO: 10.07.2017 singleton obj db conn

        //RecyclerExpAdapter.ViewHolder vh = new RecyclerExpAdapter.ViewHolder(v);

        return new RecyclerExpAdapter.ViewHolder(v);
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(RecyclerExpAdapter.ViewHolder holder, final int position) {

        final Expressions mCurrentExpItem = mExpList.get(position);

        //holder.textBody.setText(mCurrentExpItem.getExpText());

//        if (position == clickedPosition){
//            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
//        } else {
//            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_exp_new);
//        }

        if (dbInital.isExpInFavoriteList(mCurrentExpItem.getExpId())) {
            holder.imgFavoriteAdd.setImageResource(R.drawable.bookmark_ok);
        } else {
            holder.imgFavoriteAdd.setImageResource(R.drawable.bookmark_no);
        }

        holder.relativeExplanatory.setVisibility(View.GONE);
        holder.relativeSynonym.setVisibility(View.GONE);
        holder.textDefRu.setVisibility(View.GONE);
        holder.textDefEn.setVisibility(View.GONE);

        ArrayList<ParserData> parserList = ParserExp.getFirstParse(mCurrentExpItem.getExpText());

        String synonym="";
        String defEn="";
        String defRu="";

        for (ParserData text : parserList) {

            int type = text.getType();
            String content = text.getText();

//            Log.d("RecyclerExpAdapter", "+++++++++++++++++");
//            Log.d("RecyclerExpAdapter", "type: " + type);
//            Log.d("RecyclerExpAdapter", "content: " + content);
            switch (type) {
                case TYPE_BODY:
                    holder.textBody.setText(content);
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

        holder.textDefEn.setText(defEn.trim());
        holder.textDefRu.setText(defRu.trim());
        holder.textSynonym.setText(synonym.trim());

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(mCurrentExpItem.getExpText(), mCurrentExpItem.getExpText());
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
                return  true;
            }
        });

        holder.imgFavoriteAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (dbInital.isExpInFavoriteList(mCurrentExpItem.getExpId())) {

                Toast toast = Toast.makeText(v.getContext(),
                        "Removed from favorite list", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastContainer = (LinearLayout) toast.getView();
                ImageView imgBookmarkRemove = new ImageView(v.getContext());
                imgBookmarkRemove.setImageResource(R.drawable.bookmark_remove);
                toastContainer.addView(imgBookmarkRemove, 0);
                toast.show();

                dbInital.deleteFavoriteExp(mCurrentExpItem.getExpId());

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

                dbInital.addFavoriteExp(favoriteExp);

            }
            notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return  mExpList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        //dbInital.close();
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
