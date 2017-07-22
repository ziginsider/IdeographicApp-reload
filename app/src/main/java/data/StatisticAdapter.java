package data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.AppController;
import io.github.ziginsider.ideographicapp.R;
import model.StatisticTopic;

/**
 * Created by zigin on 20.11.2016.
 */

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {

    private ArrayList<StatisticTopic> statisticTopicList;
    //private int clickedPosition;
    private DatabaseHandlerExternal dbConnExternal;
    //private DatabaseHandlerInner dbInital;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTopic;
        private int idTopic;
        private TextView textCounterTopic;
        //private RelativeLayout relativeLayout;
        private ImageView imgStatisticTopic;

        public ViewHolder(View view) {
            super(view);
            this.textTopic = (TextView) view.findViewById(R.id.text_item_statistic_topic);
            this.textCounterTopic = (TextView) view.findViewById(R.id.number_clicked_statistic_topic);
            //this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_statistic_topic);
            this.imgStatisticTopic = (ImageView) view.findViewById(R.id.image_item_statistic_topic);
            this.idTopic = 0;
        }
    }

    public StatisticAdapter(ArrayList<StatisticTopic> statisticTopicItems) {
        this.statisticTopicList = statisticTopicItems;
        dbConnExternal = AppController.getInstance().getSQLiteConnectionExternal();
    }

    @Override
    public StatisticAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_statistic_topic, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...
//        dbConnExternal = new DatabaseHandlerExternal(parent.getContext());
        //dbInital = new DatabaseHandlerInner(parent.getContext());
        StatisticAdapter.ViewHolder vh = new StatisticAdapter.ViewHolder(v);

        return vh;
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(StatisticAdapter.ViewHolder holder, final int position) {

        StatisticTopic mCurrentStatItem = statisticTopicList.get(position);

        holder.textTopic.setText(mCurrentStatItem.getTextTopic());
        holder.idTopic = mCurrentStatItem.getIdTopic();
        holder.textCounterTopic.setText("Number of Clicks = " +
                String.valueOf(mCurrentStatItem.getCounterTopic()));

//        if (position == clickedPosition){
//            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
//        } else {
//            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_topic_new);
//        }

        if (dbConnExternal.getTopicCountByIdParent(holder.idTopic) > 0) {
            holder.imgStatisticTopic.setImageResource(R.drawable.ic_chevron_color_right);
        } else {
            holder.imgStatisticTopic.setImageResource(R.drawable.ic_three_circle_green);
        }
    }


    @Override
    public int getItemCount() {
        return  statisticTopicList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        dbConnExternal.close();
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
