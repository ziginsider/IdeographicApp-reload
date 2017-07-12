package data;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.FragmentSlidingTabsRecycler;
import io.github.ziginsider.ideographicapp.R;
import model.Topics;

import static data.Constants.*;

/**
 * Created by zigin on 25.10.2016.
 */

public class RecyclerTopicAdapter extends RecyclerView.Adapter<RecyclerTopicAdapter.ViewHolder> {


    //private DatabaseHandler dba;
    //private int clickedPosition;
    private ArrayList<Topics> mTopicsList;
    private String mNameSelectTopic;
    private DatabaseHandler dba;
    //public static OnItemClickListener listener;

    FragmentActivity workContext;
    private AfterItemClickTask afterItemClickTask;
    //FragmentSlidingTabsRecycler fragment;


    private int mDepth;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTopic;
        private int idTopic;
        private CardView cardViewLayout;
        private ImageView imageNextItem; // topic or expression
        private ImageView imageDepthItem; // depth color


        public ViewHolder(final View view) {
            super(view);
            this.textTopic = (TextView) view.findViewById(R.id.text_item_topic_content);
            this.cardViewLayout = (CardView) view.findViewById(R.id.card_view_topic_content);
            this.idTopic = 0;
            this.imageNextItem = (ImageView) view.findViewById(R.id.image_item_topic_content);
            this.imageDepthItem = (ImageView) view.findViewById(R.id.image_depth_item);
//            view.setOnClickListener( new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
////                    if (RecyclerTopicAdapter.listener != null) {
////                        RecyclerTopicAdapter.listener.onItemClick(view, getLayoutPosition());
////                    }
//                }
//            });
        }
    }

    public RecyclerTopicAdapter(ArrayList<Topics> topics, FragmentActivity workContext) {
        this.mTopicsList = topics;
        this.workContext = workContext;
        //this.fragment = fragment;
    }

    @Override
    public RecyclerTopicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_topic_item, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...

        int itemsParentId = mTopicsList.get(0).getTopicParentId();

        dba = new DatabaseHandler(parent.getContext()); // TODO: 10.07.2017 singleton obj db connection
        PersistantStorage.init(parent.getContext());
        if (itemsParentId == 0) {

            mNameSelectTopic = PersistantStorage.getProperty(Constants.TOPICS_ROOT_NAME);

        } else {
            mNameSelectTopic = PersistantStorage.getProperty(dba.getTopicById(itemsParentId).
                    getTopicText());
        }

        //get depth
        mDepth = 0;
        int currentId = itemsParentId;
        while (currentId != 0) {
            currentId = dba.getTopicById(currentId).getTopicParentId();
            mDepth++;
        }


        return new RecyclerTopicAdapter.ViewHolder(v);
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(RecyclerTopicAdapter.ViewHolder holder, final int position) {

        final Topics mCurrentTopicsItem = mTopicsList.get(position);
        holder.textTopic.setText(mCurrentTopicsItem.getTopicText());
        holder.idTopic = mCurrentTopicsItem.getTopicId();

        //holder.itemView.setLongClickable(true);

//        if (position == clickedPosition){
//            holder.cardViewLayout.setBackgroundResource(R.drawable.bg_current_topic);
//        } else {
//            holder.cardViewLayout.setBackgroundResource(R.drawable.ripple_topic_new);
//        }

        //background items:
//        if (mCurrentTopicsItem.getTopicText().equals(mNameSelectTopic)) {
//            holder.cardViewLayout.setBackgroundResource(R.drawable.bg_current_topic);
//        } else {
//            holder.cardViewLayout.setBackgroundResource(R.drawable.ripple_topic_new);
//        }

        //chevron type:
        if (dba.getTopicCountByIdParent(holder.idTopic) > 0) {
            holder.imageNextItem.setImageResource(R.drawable.ic_chevron_color_right);
        } else {
            holder.imageNextItem.setImageResource(R.drawable.ic_three_circle_green);
        }

        //depth
        switch (mDepth) {
            case DEPTH_ZERO:
                holder.imageDepthItem.setImageResource(R.color.depth_zero);
                break;
            case DEPTH_ONE:
                holder.imageDepthItem.setImageResource(R.color.depth_one);
                break;
            case DEPTH_TWO:
                holder.imageDepthItem.setImageResource(R.color.depth_two);
                break;
            case DEPTH_THREE:
                holder.imageDepthItem.setImageResource(R.color.depth_three);
                break;
            case DEPTH_FOUR:
                holder.imageDepthItem.setImageResource(R.color.depth_four);
                break;
            case DEPTH_FIVE:
                holder.imageDepthItem.setImageResource(R.color.depth_five);
                break;
            case DEPTH_SIX:
                holder.imageDepthItem.setImageResource(R.color.depth_six);
                break;
            case DEPTH_SEVEN:
                holder.imageDepthItem.setImageResource(R.color.depth_seven);
                break;
            case DEPTH_EIGHT:
                holder.imageDepthItem.setImageResource(R.color.depth_eight);
                break;
            case DEPTH_NINE:
                holder.imageDepthItem.setImageResource(R.color.depth_nine);
                break;
            default:
                holder.imageDepthItem.setImageResource(R.color.depth_zero);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //set the position
                //clickedPosition = position;

                //notify the data has changed
                //notifyDataSetChanged();
                //notifyItemChanged(position);
                //Toast.makeText(view.getContext(), "ffff = " + String.valueOf(position), Toast.LENGTH_SHORT).show();


                if (!mTopicsList.isEmpty()) {
//                                //save select item position
//                                PersistantStorage.init(getContext());
//                                if (mParentTopicId == 0) {
//                                    PersistantStorage.addProperty(Constants.TOPICS_ROOT_NAME,
//                                            mFoundTopics.get(position).getTopicText());
//                                } else {
//                                    PersistantStorage.addProperty(dba.getTopicById(mParentTopicId).getTopicText(),
//                                            mFoundTopics.get(position).getTopicText());
//                                }
                    FragmentSlidingTabsRecycler fragment =
                            (FragmentSlidingTabsRecycler) workContext.
                                    getSupportFragmentManager().
                                    findFragmentById(R.id.fragment_sliding_tabs_recycler);

                    //if do not have child topics
                    if (fragment.getCountTabs() ==
                            (fragment.getSelectedTabPosition() + 1)) {
                        //get child topic
                        Topics topic = mCurrentTopicsItem;
                        fragment.addPage(topic.getTopicId());
                    } else {
                        //remove child topics
                        while (fragment.getCountTabs() !=
                                (fragment.getSelectedTabPosition() + 1)) {
                            fragment.removePage(fragment.getSelectedTabPosition() + 1);
                        }
                        //get child topic
                        Topics topic = mCurrentTopicsItem;
                        //Log.d("Zig", "press topic text = " + topic.get TopicText());
                        fragment.addPage(topic.getTopicId());
                    }
                }
                afterItemClickTask = new AfterItemClickTask(workContext);
                afterItemClickTask.execute(mCurrentTopicsItem.getTopicId());



            }
        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
////                clickedPosition = position;
////                notifyDataSetChanged();
//                return  true;
//            }
//        });

        //TODO what is it??
    }

    @Override
    public int getItemCount() {
        return mTopicsList.size();
    }

//    public interface OnItemClickListener{
//        void onItemClick(View view, int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.listener = listener;
//    }
//
//    public void setClickedPosition(int clickedPosition) {
//        this.clickedPosition = clickedPosition;
//    }


    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        //dba.close();
        super.onDetachedFromRecyclerView(recyclerView);
    }

    private class AfterItemClickTask extends AsyncTask<Integer, Void, Void> {

        private Context mContext;

        public AfterItemClickTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Integer... params) {

            PersistantStorage.init(mContext);
            String currentCard;
            int currentCardId;


            AsyncProvider asyncProvider = new AsyncProvider();
            asyncProvider.setRecentTopic(mContext, params[0]);
            asyncProvider.setStatisticTopic(mContext, params[0]);

            if (PersistantStorage.getProperty(Constants.CURRENT_CARD) == null) {
                asyncProvider.setNewCard(mContext);
            }

            currentCard = PersistantStorage.getProperty(Constants.CURRENT_CARD);
            currentCardId = Integer.valueOf(currentCard);
            asyncProvider.updateCardByIdCard(mContext, currentCardId, params[0]);

            return null;
        }
    }
}


