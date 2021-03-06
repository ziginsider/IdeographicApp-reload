package io.github.ziginsider.ideographicapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import data.AllExpAdapter;
import data.AllExpDataCreator;

@EActivity(R.layout.activity_result_topic_search)
public class ResultTopicSearchActivity extends AppCompatActivity {

    //private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    //private DatabaseHandlerExternal dbConnInner;
    //private ArrayList<DoubleItem> mDoubleItems;

    private List<ParentObject> mItems = new ArrayList<>();

    @ViewById(R.id.recycler_view_result_topic_search)
    FastScrollRecyclerView mRecyclerView;





    @AfterViews
    void init() {

//        AllExpDataCreator creator = AllExpDataCreator.get(this);
//        List<ParentObject> parentObjectList = creator.getAll();


        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        //mRecyclerView.setHasFixedSize(true);

//        // используем linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        // создаем адаптер
//        mAdapter = new RecyclerAdapter(mDoubleItems);
//        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        AllExpAdapter adapter = new AllExpAdapter(this, parentObjectList);
//        adapter.setParentClickableViewAnimationDefaultDuration();
//        adapter.setParentAndIconExpandOnClick(true);
//
//        mRecyclerView.setAdapter(adapter);

        new GetAllExpTask(this).execute();

        setupAdapter();

    }

    private class GetAllExpTask extends AsyncTask<Void, Void, List<ParentObject>> {

        private Context mContext;

        public GetAllExpTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<ParentObject> doInBackground(Void... params) {
            AllExpDataCreator creator = AllExpDataCreator.get(mContext);
            return creator.getAll();
        }

        @Override
        protected void onPostExecute(List<ParentObject> items) {
            mItems = items;
            setupAdapter();
        }
    }

    private void setupAdapter() {

        AllExpAdapter adapter = new AllExpAdapter(this, mItems);
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        mRecyclerView.setAdapter(adapter);
    }


}
