package io.github.ziginsider.ideographicapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import app.AppController;
import data.AsyncProvider;
import data.Constants;
import data.DatabaseHandlerExternal;
import data.DatabaseHandlerInner;
import data.RecyclerItemClickListener;
import data.StatisticAdapter;
import model.StatisticTopic;

@EActivity(R.layout.activity_statistic_topic)
public class StatisticTopicActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHandlerInner dbConnInner;
    DatabaseHandlerExternal dbConnExternal;
    ArrayList<StatisticTopic> statisticTopicList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StatisticTopicActivity.AfterItemClickTask afterItemClickTask;

    @ViewById(R.id.recycler_view_statistic_topic)
    RecyclerView mRecyclerView;

    @AfterViews
    void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        dbConnInner = new DatabaseHandlerInner(this);
//        dbConnExternal = new DatabaseHandlerExternal(this);
        dbConnExternal = AppController.getInstance().getSQLiteConnectionExternal();
        dbConnInner = AppController.getInstance().getSQLiteConnectionInner();

        //get all expressions
        statisticTopicList = dbConnInner.getStatisticTopicList();

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        mRecyclerView.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // создаем адаптер
        mAdapter = new StatisticAdapter(statisticTopicList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this,
                        mRecyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                ArrayList<Integer> idTopicsPageList = new ArrayList<Integer>();
                                idTopicsPageList.clear();

                                int currentId = statisticTopicList.get(position).getIdTopic();
                                //add to recent topics
                                afterItemClickTask = new StatisticTopicActivity.
                                        AfterItemClickTask(StatisticTopicActivity.this);
                                afterItemClickTask.execute(currentId);

                                idTopicsPageList.add(currentId);
                                do {
                                    currentId = dbConnExternal.getTopicById(currentId).getParentId();
                                    idTopicsPageList.add(currentId);

                                } while (currentId != 0);



                                Intent i = new Intent(StatisticTopicActivity.this,
                                        WorkActivityRecycler_.class);
                                i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, idTopicsPageList);
                                startActivity(i);
                                StatisticTopicActivity.this.finish();
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                // ...
                            }
                        }
                )
        );



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statistic_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_start) {

            Intent i = new Intent(this, WorkActivityRecycler_.class);
            ArrayList<Integer> startTopicsList = new ArrayList<>();
            startTopicsList.add(0); //set topics root = "Topics"
            i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, startTopicsList);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_recent_open) {

            ArrayList<Integer> idTopicsPageList = new ArrayList<Integer>();
            idTopicsPageList.clear();

            int currentId = dbConnInner.getIdTopicTopRecentTopics();

            idTopicsPageList.add(currentId);

            if (currentId != 0) {
                do {
                    currentId = dbConnExternal.getTopicById(currentId).getParentId();
                    idTopicsPageList.add(currentId);

                } while (currentId != 0);
            }
            Intent i = new Intent(this,
                    WorkActivityRecycler_.class);
            i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, idTopicsPageList);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_recent_show) {

            Intent i = new Intent(this, RecentTopicActivity_.class);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_statistic) {


        } else if (id == R.id.nav_favorite) {

            Intent i = new Intent(this, FavoriteExpActivity_.class);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_all_exp) {

            Intent i = new Intent(this, ResultTopicSearchActivity_.class);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_help) {

            //  Launch app intro
            Intent i = new Intent(this, Intro.class);
            startActivity(i);

        } else if (id == R.id.nav_options) {

            Intent i = new Intent(this, OptionsActivity.class);
            this.startActivity(i);

        } else if (id == R.id.nav_rate) {

            Toast.makeText(this, "TODO: rate it", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_exit) {

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class AfterItemClickTask extends AsyncTask<Integer, Void, Void> {

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

            AsyncProvider asyncProvider = new AsyncProvider();
            asyncProvider.setRecentTopic(mContext, params[0]);
            asyncProvider.setStatisticTopic(mContext, params[0]);

            return null;
        }
    }

    @Override
    protected void onDestroy() {
//        dbConnInner.close();
//        dbConnExternal.close();
        super.onDestroy();
    }
}
