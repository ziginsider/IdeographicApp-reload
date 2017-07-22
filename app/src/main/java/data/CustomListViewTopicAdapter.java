package data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


import io.github.ziginsider.ideographicapp.R;

import model.Topics;

/**
 * Created by zigin on 24.09.2016.
 */
public class CustomListViewTopicAdapter extends ArrayAdapter<Topics> {

    private int layoutResourse;
    private Activity activity;
    private ArrayList<Topics> TopicsList = new ArrayList<>();
    private PersistantStorage storage;
    private String mNameSelectTopic;
    private DatabaseHandlerExternal dba;
    //private int mSelectItem;
    //private boolean mFlagSelect;
    //FragmentSlidingTabs fragmentSlidingTabs;
    //private ViewPager viewPager;

    public CustomListViewTopicAdapter(Activity act, int resource, ArrayList<Topics> data) {
        super(act, resource, data);
        layoutResourse = resource;
        activity = act;
        TopicsList = data;
        storage = new PersistantStorage();
        dba = new DatabaseHandlerExternal(getContext());

        storage.init(getContext());
         if (data.get(0).getParentId() == 0) {

            mNameSelectTopic = storage.getProperty(Constants.TOPICS_ROOT_NAME);

        } else {
            mNameSelectTopic = storage.getProperty(dba.getTopicById(data.get(0).getParentId()).
                    getText());
        }

        notifyDataSetChanged();
    }
//


    @Override
    public int getCount() {
        return TopicsList.size();
    }

    @Override
    public Topics getItem(int position) {
        return TopicsList.get(position);
    }

    @Override
    public int getPosition(Topics item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if (row == null || (row.getTag() == null)) {

            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResourse, parent, false);

            holder = new ViewHolder();

            holder.topicText = (TextView) row.findViewById(R.id.text_item_topic_content);
            holder.topicLayout = (RelativeLayout) row.findViewById(R.id.card_view_topic_content);



//            int selectItemPosition = fragmentSlidingTabs.
//                    getSelectTopicItemPosition(fragmentSlidingTabs.getSelectedTabPosition());


//            if (position == ic_intro_1) {
//                holder.topicLayout.setBackgroundResource(R.drawable.bg_current_topic);
//            } else {
//                holder.topicLayout.setBackgroundResource(R.drawable.ripple_topic_new);
//            }


//            holder.id = (TextView) row.findViewById(R.id.id);
//            holder.topicIdParent = (TextView) row.findViewById(R.id.topicIdParent);
//            holder.labels = (TextView) row.findViewById(R.id.labels);

            if (getItem(position).getText().equals(mNameSelectTopic)) {

                holder.topicLayout.setBackgroundResource(R.drawable.bg_current_topic);
            } else {
                holder.topicLayout.setBackgroundResource(R.drawable.ripple_topic_new);
            }

            row.setTag(holder);
        } else {

            holder = (ViewHolder) row.getTag();
        }

        holder.topic = getItem(position);

        holder.topicText.setText(holder.topic.getText());
//        holder.topicIdParent.setText("Parent id = " + String.valueOf(holder.topic.getParentId()));
//        holder.id.setText("Topic id = " + String.valueOf(holder.topic.getId()));
//        holder.labels.setText("Labels = " + holder.topic.getLabels());





//        final ViewHolder finalHolder = holder;
//        row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finalHolder.topicLayout.setBackgroundResource(R.drawable.bg_current_topic);
//            }
//        });


//        if (position == ic_intro_3) {
//
//            holder.text.setText("ffffffffffffff");
//        }







        return row;
    }

    public class ViewHolder {
        Topics topic;
        TextView topicText;

        RelativeLayout topicLayout;
//        TextView topicIdParent;
//        TextView id;
//        TextView labels;
    }
}
