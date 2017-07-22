package io.github.ziginsider.ideographicapp;

/**
 * Created by zigin on 26.10.2016.
 */


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import data.Constants;
import data.DatabaseHandlerExternal;
import data.RecyclerExpAdapter;
import data.RecyclerTopicAdapter;
import data.TimeTracker;
import model.Expressions;
import model.Topics;

public class FragmentWorkRecycler extends Fragment {

    private int mParentTopicId;

    RecyclerView listTopicContentRecycler;

    com.melnykov.fab.FloatingActionButton fab;
    com.melnykov.fab.FloatingActionButton fabAdd;

    TabLayout tabLayout;

    private DatabaseHandlerExternal dbConnExternal;

    public ArrayList<Topics> topicsFromDB = new ArrayList<>();
    private ArrayList<Topics> mFoundTopics;
    public ArrayList<Expressions> expFromDB = new ArrayList<>();
    private ArrayList<Expressions> mFoundExp;

    private FragmentActivity workContext;

    private RecyclerExpAdapter mAdapterExp;
    android.support.v7.widget.LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic_content_recycler, container, false);

        Bundle bundle = getArguments();
        mParentTopicId = bundle.getInt(Constants.BUNDLE_ID_TOPIC);

        listTopicContentRecycler = (RecyclerView) v.findViewById(R.id.list_topic_content_recycler);

        mLayoutManager = new LinearLayoutManager(getContext());
        listTopicContentRecycler.setLayoutManager(mLayoutManager);

        setAdapter();

        refreshData();

        return v;
    }

    private void setAdapter() {
        if (dbConnExternal.getTopicCountByIdParent(mParentTopicId) > 0) { //work with topics
            topicsFromDB = dbConnExternal.getTopicByIdParentAlphabet(mParentTopicId);
            setAdapterTopics();
        } else { //works with exp
            new GetAsyncExp().execute();// TODO: 12.07.2017 maybe don't async?
            setAdapterExp();
        }
    }

//    private void setItemClickEvent() {
//        listTopicContentRecycler.addOnItemTouchListener(
//                new RecyclerItemClickListener(getActivity(), listTopicContentRecycler, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        if (!topicsFromDB.isEmpty()) {
//
//
////                                //save select item position
////                                PersistantStorage.init(getContext());
////                                if (mParentTopicId == 0) {
////                                    PersistantStorage.addProperty(Constants.TOPICS_ROOT_NAME,
////                                            mFoundTopics.get(position).getText());
////                                } else {
////                                    PersistantStorage.addProperty(dbConnInner.getTopicById(mParentTopicId).getText(),
////                                            mFoundTopics.get(position).getText());
////                                }
//
//
//                            FragmentSlidingTabsRecycler fragmentSlidingTabsRecycler =
//                                    (FragmentSlidingTabsRecycler) workContext.
//                                            getSupportFragmentManager().
//                                            findFragmentById(R.id.fragment_sliding_tabs_recycler);
//                            //if do not have child topics
//                            if (fragmentSlidingTabsRecycler.getCountTabs() ==
//                                    (fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1)) {
//                                //get child topic
//                                Topics topic = mFoundTopics.get(position);
//                                fragmentSlidingTabsRecycler.addPage(topic.getId());
//                            } else {
//                                //remove child topics
//                                while (fragmentSlidingTabsRecycler.getCountTabs() !=
//                                        (fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1)) {
//                                    fragmentSlidingTabsRecycler.
//                                            removePage(fragmentSlidingTabsRecycler.
//                                                    getSelectedTabPosition() + 1);
//                                }
//                                //get child topic
//                                Topics topic = mFoundTopics.get(position);
//                                //Log.d("Zig", "press topic text = " + topic.get TopicText());
//                                fragmentSlidingTabsRecycler.addPage(topic.getId());
//                            }
//                        }
//                        //set recent topic
//                        afterItemClickTask = new AfterItemClickTask(workContext);
//                        afterItemClickTask.execute(mFoundTopics.get(position).getId());
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        // ...
//                        ScaleAnimation growAnim = new ScaleAnimation(1.0f,
//                                1.2f,
//                                1.0f,
//                                1.2f,
//                                Animation.RELATIVE_TO_SELF,
//                                0.5F,
//                                Animation.RELATIVE_TO_SELF,
//                                0.5F);
//                        growAnim.setDuration(300);
//                        view.startAnimation(growAnim);
//                        view.setHapticFeedbackEnabled(true);
//                    }
//                }));
//    }

    @Override
    public void onAttach(Context context) {
        workContext = (FragmentActivity) context;
        super.onAttach(context);
        dbConnExternal = new DatabaseHandlerExternal(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public int getmParentTopicId() {
        return mParentTopicId;
    }

    private void refreshData() {

        Log.d("Zig", "begin function refreshData()");
        //get child-topics
//        topicsFromDB = dbConnInner.getTopicByIdParentAlphabet(mParentTopicId);

        //get child-expressions
//        expFromDB = dbConnInner.getExpByIdParent(mParentTopicId);

        //clone fromDB -> foundItems
        //cloneItems();

        TimeTracker.start();
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_recycler);
        fabAdd = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_desk);
        TimeTracker.end();
        Log.d("FragmentWorkRecycler", "TimeTracker.howLong(): fab t = " + TimeTracker.howLong());

        //tabbar = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout_recycler);
        //appbar = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout_work_recycler);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.work_tab_layout_recycler);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                fabAdd.show();
                fab.show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //TimeTracker.start();
        //showListView();
        //TimeTracker.end();
        //Log.d("FragmentWorkRecycler", "showListView() in refresh t = " + TimeTracker.howLong());

        listTopicContentRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ////
                if (dy > 0) {
                    fabAdd.hide();
                    fab.hide();
                } else if (dy < 0) {
                    fabAdd.show();
                    fab.show();
                }

                ////


//                if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
//
////                    if (fab.getTag() == "show") {
////                        fab.animate().translationY(fab.getBottom()).
////                                setInterpolator(new AccelerateInterpolator()).
////                                setDuration(getResources().
////                                        getInteger(android.R.integer.config_mediumAnimTime)).
////                                start();
////                        fab.setTag("hide");
////                    }
////                    fabAdd.hide();
//                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
//                //} else if (dy < 0) {
//
////                    fab.animate().translationY(0).
////                            setInterpolator(new DecelerateInterpolator()).start();
////                    fab.setTag("show");
// //                   fabAdd.show();
//                }
//
//                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });
        Log.d("Zig", "End function RefreshData()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dbConnExternal.close();
    }

//    public void showListView() {
//
//        // Log.d("Zig", "showListView() begin, mQuerySearch = " + getQuerySearch());
//
////        if (topicsFromDB != null) {
////            if (!topicsFromDB.isEmpty()) {
//
//
//        // создаем адаптер
//        setAdapterTopics();
//
//        //recyclerview item click
//        listTopicContentRecycler.addOnItemTouchListener(
//                new RecyclerItemClickListener(getActivity(),
//                        listTopicContentRecycler,
//                        new RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//
//                                if (!topicsFromDB.isEmpty()) {
//
//                                    //save select item position
//                                    PersistantStorage.init(getContext());
//                                    if (mParentTopicId == 0) {
//
//                                        PersistantStorage.addProperty(Constants.TOPICS_ROOT_NAME,
//                                                mFoundTopics.get(position).getText());
//                                    } else {
//                                        PersistantStorage.addProperty(dbConnInner.getTopicById(mParentTopicId).getText(),
//                                                mFoundTopics.get(position).getText());
//                                    }
//
//                                    FragmentSlidingTabsRecycler fragmentSlidingTabsRecycler =
//                                            (FragmentSlidingTabsRecycler) workContext.
//                                                    getSupportFragmentManager().
//                                                    findFragmentById(R.id.fragment_sliding_tabs_recycler);
//
//                                    //if do not have child topics
//                                    if (fragmentSlidingTabsRecycler.getCountTabs() ==
//                                            (fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1)) {
//                                        //get child topic
//                                        Topics topic = mFoundTopics.get(position);
//                                        fragmentSlidingTabsRecycler.addPage(topic.getId());
//
//                                    } else {
//                                        //remove child topics
//                                        while (fragmentSlidingTabsRecycler.getCountTabs() !=
//                                                (fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1)) {
//                                            fragmentSlidingTabsRecycler.
//                                                    removePage(fragmentSlidingTabsRecycler.
//                                                            getSelectedTabPosition() + 1);
//                                        }
//                                        //get child topic
//                                        Topics topic = mFoundTopics.get(position);
//                                        //Log.d("Zig", "press topic text = " + topic.get TopicText());
//                                        fragmentSlidingTabsRecycler.addPage(topic.getId());
//                                    }
//                                }
////                     else {
////
////                        Expressions exp = mFoundExp.get(position);
////
////                        ClipboardManager clipboard = (ClipboardManager) getActivity().
////                                getSystemService(Context.CLIPBOARD_SERVICE);
////                        ClipData clip = ClipData.newPlainText(exp.getExpText(), exp.getExpText());
////                        clipboard.setPrimaryClip(clip);
////
////                    }
//                                //set recent topic
//                                afterItemClickTask = new AfterItemClickTask(workContext);
//                                afterItemClickTask.execute(mFoundTopics.get(position).getId());
//                            }
//
//                            @Override
//                            public void onItemLongClick(View view, int position) {
//                                // ...
//                                ScaleAnimation growAnim = new ScaleAnimation(1.0f,
//                                        1.2f,
//                                        1.0f,
//                                        1.2f,
//                                        Animation.RELATIVE_TO_SELF,
//                                        0.5F,
//                                        Animation.RELATIVE_TO_SELF,
//                                        0.5F);
//                                growAnim.setDuration(300);
//                                view.startAnimation(growAnim);
//                                view.setHapticFeedbackEnabled(true);
//
//
//                            }
//                        }));
//    } else
//
//    {
//
//        // создаем адаптер
//        //mAdapterExp = new RecyclerExpAdapter(expFromDB);
//        setAdapterExp();
//        //mAdapterExp.notifyDataSetChanged();
//    }
//}
    //}

    private void setAdapterExp() {
        listTopicContentRecycler.setAdapter(new RecyclerExpAdapter(expFromDB));
    }

    private void setAdapterTopics() {

        listTopicContentRecycler.setAdapter(new RecyclerTopicAdapter(topicsFromDB, workContext));
    }

    public void showSearchResult(String searchText) {


        if (searchText != null && !searchText.isEmpty() && topicsFromDB != null) {

            cloneItems();

            if (!topicsFromDB.isEmpty()) {

//                Log.d("Zig", "In showSearchResult()"
//                        + " topicsFromDB isn't empty ");

//                if (mFoundTopics != null) {
//
//                    mFoundTopics.clear();
//                }
//
//
//                for(Topics item:topicsFromDB) {
//
//                    //not case sensitive
//                    if (item.getText().toLowerCase().contains(searchText.toLowerCase())) {
//
//                        mFoundTopics.add(item);
//                    }
//                }
//
//                topicAdapterRecycler = new CustomListViewTopicAdapter(getActivity(),
//                        R.layout.adapter_topic_item,
//                        mFoundTopics); //send id topics current tabs
//                listTopicContentRecycler.setAdapter(topicAdapterRecycler);
//                topicAdapterRecycler.notifyDataSetChanged();
//
//                //textItemCount.setText("Number of subtopics");
//                //itemCount.setText(String.valueOf(mFoundTopics.size()));
            } else { // work with exp
//                Log.d("Zig", "In showSearchResult()"
//                        + " topicsFromDB is empty ");

                //show expressions search result
                if (mFoundTopics != null) {

                    mFoundExp.clear();
                }

                for (Expressions item : expFromDB) {
                    //not case sensitive
                    if (item.getExpText().toLowerCase().contains(searchText.toLowerCase())) {

                        mFoundExp.add(item);
                    }
                }

//                ExpAdapterRecycler = new CustomListViewExpAdapter(getActivity(),
//                        R.layout.adapter_exp_item,
//                        mFoundExp);
//                listTopicContentRecycler.setAdapter(ExpAdapterRecycler);
//                ExpAdapterRecycler.notifyDataSetChanged();

//                mLayoutManager = new LinearLayoutManager(getContext());
//                listTopicContentRecycler.setLayoutManager(mLayoutManager);
                // создаем адаптер
                mAdapterExp = new RecyclerExpAdapter(mFoundExp);
                listTopicContentRecycler.setAdapter(mAdapterExp);
                mAdapterExp.notifyDataSetChanged();
//                textItemCount.setText("Number of expressions:");
//                itemCount.setText(String.valueOf(mFoundExp.size()));
            }

        } else {
           // setAdapter();
        }
    }

    public void showSearchResultTopic(String searchText) {
        if (searchText != null && !searchText.isEmpty()) {

            if (!topicsFromDB.isEmpty()) {

                Toast.makeText(getActivity(), "TODO result of subtopic search: " + searchText,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

//    public void fabLauncher() {
//
//
//        if (!topicsFromDB.isEmpty()) {
//
//            Toast.makeText(getActivity(), "I'm a black cat! You have " +
//                    mFoundTopics.size() + " topics.", Toast.LENGTH_SHORT).show();
//
//        } else {
//
//            Toast.makeText(getActivity(), "I'm a black cat! You have " +
//                    mFoundExp.size() + " expessions.", Toast.LENGTH_SHORT).show();
//
//        }
//
//        //toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
//        //appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
//    }

    public void cloneItems() {

        if (topicsFromDB != null) {
            mFoundTopics = (ArrayList<Topics>) topicsFromDB.clone();
        }
        if (expFromDB != null) {
            mFoundExp = (ArrayList<Expressions>) expFromDB.clone();
        }
    }

//    private class AfterItemClickTask extends AsyncTask<Integer, Void, Void> {
//
//        private Context mContext;
//
//        public AfterItemClickTask(Context context) {
//            mContext = context;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//
//        @Override
//        protected Void doInBackground(Integer... params) {
//
//            PersistantStorage.init(mContext);
//            String currentCard;
//            int currentCardId;
//
//
//            AsyncProvider asyncProvider = new AsyncProvider();
//            asyncProvider.setRecentTopic(mContext, params[0]);
//            asyncProvider.setStatisticTopic(mContext, params[0]);
//
//            if (PersistantStorage.getProperty(Constants.CURRENT_CARD) == null) {
//                asyncProvider.setNewCard(mContext);
//            }
//
//            currentCard = PersistantStorage.getProperty(Constants.CURRENT_CARD);
//            currentCardId = Integer.valueOf(currentCard);
//            asyncProvider.updateCardByIdCard(mContext, currentCardId, params[0]);
//
//            return null;
//        }
//    }

//    private class GetAsyncTopics extends AsyncTask<Void, Void, ArrayList<Topics>> {
//
////        private Context mContext;
//
////        public GetAsyncTopics(Context context) {
////            mContext = context;
////        }
//
//        @Override
//        protected ArrayList<Topics> doInBackground(Void... params) {
//            return dbConnInner.getTopicByIdParentAlphabet(mParentTopicId);
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Topics> items) {
//            topicsFromDB = items;
//            //setItemClickEvent();
//            setAdapterTopics();
//        }
//    }

    private class GetAsyncExp extends AsyncTask<Void, Void, ArrayList<Expressions>> {

//        private Context mContext;

//        public GetAsyncExp(Context context) {
//            mContext = context;
//        }

        @Override
        protected ArrayList<Expressions> doInBackground(Void... params) {
            ArrayList<Expressions> items = dbConnExternal.getExpByIdParent(mParentTopicId);
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<Expressions> items) {
            expFromDB = items;
            setAdapterExp();
        }
    }
}
