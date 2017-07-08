package io.github.ziginsider.ideographicapp;

/**
 * Created by zigin on 06.06.2017.
 */

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ckenergy.stackcard.stackcardlayoutmanager.CenterScrollListener;
import com.ckenergy.stackcard.stackcardlayoutmanager.DefaultChildSelectionListener;
import com.ckenergy.stackcard.stackcardlayoutmanager.ItemTouchHelperCallBack;
import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaseCardStackActivity extends AppCompatActivity {

    List<StackCardLayoutManager> managers = new ArrayList<>();

    public void initRecyclerView(final RecyclerView recyclerView,
                                 final StackCardLayoutManager layoutManager,
                                 final RecyclerView.Adapter adapter) {

        managers.add(layoutManager);

        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(adapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new CenterScrollListener());

        /**
         * remove item
         */
        if (adapter instanceof ItemTouchHelperCallBack.onSwipListener) {
            ItemTouchHelperCallBack.onSwipListener swipListener =
                    (ItemTouchHelperCallBack.onSwipListener) adapter;
            ItemTouchHelperCallBack itemTouchHelperCallBack = new ItemTouchHelperCallBack();
            itemTouchHelperCallBack.setOnSwipListener(swipListener);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallBack);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

        // enable center post touching on item and item click listener
        DefaultChildSelectionListener.initCenterItemListener(new DefaultChildSelectionListener.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClicked(@NonNull final RecyclerView recyclerView,
                                            @NonNull final StackCardLayoutManager stackCardLayoutManager,
                                            @NonNull final View v) {
                final int position = recyclerView.getChildLayoutPosition(v);
                final String msg = String.format(Locale.US, "Item %1$d was clicked", position + 1);
                Log.d("onCenterItemClicked", msg);
                Toast.makeText(BaseCardStackActivity.this, msg, Toast.LENGTH_SHORT).show();


            }


        }, recyclerView, layoutManager);



        layoutManager.addOnItemSelectionListener(new StackCardLayoutManager.OnCenterItemSelectionListener() {

            @Override
            public void onCenterItemChanged(final int adapterPosition) {
                if (StackCardLayoutManager.INVALID_POSITION != adapterPosition) {
//                    final int value = adapter.cards.get(adapterPosition).mPosition;
/*
                    adapter.mPosition[adapterPosition] = (value % 10) + (value / 10 + 1) * 10;
                    adapter.notifyItemChanged(adapterPosition);
*/
                }
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int stackOrder = 0; // = StackCardLayoutManager.IN_STACK_ORDER;
        int numberOrder = 0;
        int stringID = 0;
        switch (item.getItemId()) {
            case R.id.stack:
                if(managers.get(0).getStackOrder() == StackCardLayoutManager.IN_STACK_ORDER) {
                    stackOrder = StackCardLayoutManager.OUT_STACK_ORDER;
                    stringID = R.string.out_stack;
                }else {
                    stackOrder = StackCardLayoutManager.IN_STACK_ORDER;
                    stringID = R.string.in_stack;
                }
                break;
            case R.id.number:
                if(managers.get(0).getNumberOrder() == StackCardLayoutManager.POSITIVE_ORDER) {
                    numberOrder = StackCardLayoutManager.NEGATIVE_ORDER;
                    stringID = R.string.negative;
                }else {
                    numberOrder = StackCardLayoutManager.POSITIVE_ORDER;
                    stringID = R.string.positive;
                }
                break;
        }
        Toast.makeText(this,getString(stringID),Toast.LENGTH_SHORT).show();

        for (StackCardLayoutManager manager : managers) {
            if (stackOrder != 0) {
                manager.setStackOrder(stackOrder);
            }
            if (numberOrder != 0) {
                manager.setNumberOrder(numberOrder);
            }
        }
        return true;

    }*/


}
