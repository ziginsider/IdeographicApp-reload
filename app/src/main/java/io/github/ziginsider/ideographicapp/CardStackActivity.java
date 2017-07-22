package io.github.ziginsider.ideographicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardLayoutManager;
import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardPostLayout;

import java.util.ArrayList;

import app.AppController;
import data.Constants;
import data.DatabaseHandlerExternal;
import data.DatabaseHandlerInner;
import data.PersistantStorage;
import data.RecyclerViewCardStackAdapter;
import model.CardData;
import model.CardTopic;

public class CardStackActivity extends BaseCardStackActivity {

    DatabaseHandlerInner dbConnInner;
    DatabaseHandlerExternal dbConnExternal;

    String currentCard;

    PersistantStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_stack);

//        dbConnInner = new DatabaseHandlerInner(this);
//        dbConnExternal = new DatabaseHandlerExternal(this);

        dbConnExternal = AppController.getInstance().getSQLiteConnectionExternal();
        dbConnInner = AppController.getInstance().getSQLiteConnectionInner();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_stack_list);

//        ///////////////////////
//        ArrayList<CardData> data = new ArrayList<>();
//        for(int i = 0; i < 20; i++) {
//            String name;
//            int type;
//            ArrayList<Integer> childTypes = new ArrayList<Integer>();
//            ArrayList<String> childNames = new ArrayList<String>();
//            childNames.add("one one one");
//            childNames.add("two second two");
//            childNames.add("three...");
//            childNames.add("four");
//            childNames.add("five");
//            childNames.add("six");
//            childNames.add("seven");
//            childNames.add("eight");
//            childNames.add("nine");
//            childNames.add("ten pereten");
//            childNames.add("eleven");
//            childNames.add("twelve");
//            childNames.add("thirteen");
//            childNames.add("fourteen");
//            childNames.add("fifteen");
//            childNames.add("sixteen");
//            childNames.add("seventeen");
//            childNames.add("eighteen");
//            childNames.add("nineteen");
//            childNames.add("twenty");
//
//            if (i < 10) {
//                name = "Topics  Man  Behaviour";
//                type = Constants.CARD_TYPE_TOPIC;
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//            } else {
//                name = "Topics  Circumstances";
//                type = Constants.CARD_TYPE_EXP;
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//            }
//            data.add(new CardData(type, name, childNames, childTypes));
//
//        }



        /////////////////////////////

        //get current card id
        PersistantStorage.init(this);
        currentCard = PersistantStorage.getProperty(Constants.CURRENT_CARD);

        ArrayList<CardData> data = getCardsData();

        //setContentView(R.layout.activity_card_stack);
        StackCardLayoutManager stackCardLayoutManager =
                new StackCardLayoutManager
                        (StackCardLayoutManager.VERTICAL,
                                false,
                                new StackCardPostLayout());
        stackCardLayoutManager.setNumberOrder(StackCardLayoutManager.NEGATIVE_ORDER);
        stackCardLayoutManager.setStackOrder(StackCardLayoutManager.OUT_STACK_ORDER);
        RecyclerViewCardStackAdapter adapter = new RecyclerViewCardStackAdapter(data);

        initRecyclerView(recyclerView, stackCardLayoutManager, adapter);
    }

    private ArrayList<CardData> getCardsData() {
        ArrayList<CardData> data = new ArrayList<>();
        ArrayList<CardTopic> cardTopic = dbConnInner.getCardTopicList();

        for (CardTopic card: cardTopic) {
            ArrayList<String> childTopicsNames = new ArrayList<>();
            ArrayList<Integer> childTopicsImgType = new ArrayList<>();
            childTopicsNames = dbConnExternal.getTopicNamesByIdParent(card.getTopicId());

            if (childTopicsNames.isEmpty()) {
                childTopicsNames = dbConnExternal.getExpNamesByIdParent(card.getTopicId());

                for (int i = 0; i < childTopicsNames.size(); i++) {
                    childTopicsImgType.add(Constants.IMAGE_TYPE_EXP);
                }

                data.add(new CardData(card.getCardTopicId(),
                        card.getTopicId(),
                        card.getTopicText(), //TODO topics text >>>>
                        childTopicsNames,
                        childTopicsImgType));
            } else {

                ArrayList<Integer> idTopicsChild = dbConnExternal
                        .getTopicIdsByIdParent(card.getTopicId());

                for (int id : idTopicsChild) {

                    if (dbConnExternal.getTopicByIdParent(id).isEmpty()) {
                        childTopicsImgType.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                    } else  {
                        childTopicsImgType.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                    }
                }
                data.add(new CardData(card.getCardTopicId(),
                        card.getTopicId(),
                        card.getTopicText(),
                        childTopicsNames,
                        childTopicsImgType));
            }
        }

        return  data;
    }

    @Override
    protected void onDestroy() {
//        dbConnInner.close();
//        dbConnExternal.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (PersistantStorage.getProperty(Constants.CURRENT_CARD).equals(currentCard)) {

            super.onBackPressed();
        } else {

            int lastCardId = dbConnInner.getCardLastId();

            ArrayList<Integer> idTopicsPageList = new ArrayList<Integer>();
            idTopicsPageList.clear();

            //TODO add to recent topics
//            afterItemClickTask = new AfterItemClickTask(RecentTopicActivity.this);
//            afterItemClickTask.execute(currentId);

            idTopicsPageList.add(lastCardId);
            do {
                lastCardId = dbConnExternal.getTopicById(lastCardId).getParentId();
                idTopicsPageList.add(lastCardId);

            } while (lastCardId != 0);


            Intent i = new Intent(CardStackActivity.this,
                    WorkActivityRecycler_.class);
            i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, idTopicsPageList);
            startActivity(i);
            CardStackActivity.this.finish();
        }

    }
}
