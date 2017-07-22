package data;

import android.content.Context;

import java.util.ArrayList;

import model.CardTopic;
import model.RecentTopics;
import model.StatisticTopic;

/**
 * Created by zigin on 08.11.2016.
 */


public class AsyncProvider {

    //private PersistantStorage storage;

    public AsyncProvider() {
    }

    public void setRecentTopic(Context context, int topicId)
    {
        int currentWeight = 0;
        int setWeight = 0;
        RecentTopics currentRecentTopic;
        int maxTotalRecent = 20;

        DatabaseHandlerInner dbConnInner = new DatabaseHandlerInner(context);
        DatabaseHandlerExternal dbConnExternal = new DatabaseHandlerExternal(context);

        ArrayList<RecentTopics> recentList = dbConnInner.getRecentTopicsList();

        //if the topic is on the recent
        if (dbConnInner.getRecentCountByIdTopic(topicId) > 0) {

            currentRecentTopic = dbConnInner.getRecentTopicByTopicId(topicId);
            currentWeight = currentRecentTopic.getTopicWeight();
            setWeight = dbConnInner.getRecentMaxWeight();

            for(int i = 0; i < recentList.size(); i++) {

                if (recentList.get(i).getTopicWeight() > currentWeight) {

                    //recentList.get(i).downTopicWeight();
                    dbConnInner.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(),
                            (recentList.get(i).getTopicWeight() - 1));

                } else if(recentList.get(i).getTopicWeight() == currentWeight) {

                    //recentList.get(i).setTopicWeight(setWeight);
                    dbConnInner.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(), setWeight);
                }
            }
        } else { //if the topic isn't on the recent


            int totalRecent = dbConnInner.getTotalRecentTopics();
            //if it is first recent
            if (totalRecent == 0) {

                currentRecentTopic = new RecentTopics(
                        dbConnExternal.getTopicById(topicId).getText(),
                        topicId,
                        1);
                dbConnInner.addRecentTopic(currentRecentTopic);

            } else if (totalRecent < maxTotalRecent) {

                setWeight = dbConnInner.getRecentMaxWeight() + 1;

                currentRecentTopic = new RecentTopics(
                        dbConnExternal.getTopicById(topicId).getText(),
                        topicId,
                        setWeight);
                dbConnInner.addRecentTopic(currentRecentTopic);
            } else {

                for(int i = 0; i < recentList.size(); i++) {

                    dbConnInner.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(),
                            (recentList.get(i).getTopicWeight() - 1));
                }

                dbConnInner.deleteLastRecentTopic();

                setWeight = maxTotalRecent;

                currentRecentTopic = new RecentTopics(
                        dbConnExternal.getTopicById(topicId).getText(),
                        topicId,
                        setWeight);
                dbConnInner.addRecentTopic(currentRecentTopic);
            }
        }
        dbConnInner.close();
        dbConnExternal.close();
    }

    public void setStatisticTopic(Context context, int topicId) {

        DatabaseHandlerInner dbConnInner = new DatabaseHandlerInner(context);
        DatabaseHandlerExternal dbConnExternal = new DatabaseHandlerExternal(context);

        if (dbConnInner.isTopicInStatisticList(topicId)) {

            dbConnInner.upTopicCounterByIdTopic(topicId);

        } else {
            StatisticTopic statisticTopic = new StatisticTopic();

            statisticTopic.setTextTopic(dbConnExternal.getTopicById(topicId).getText());
            statisticTopic.setIdTopic(topicId);
            statisticTopic.setCounterTopic(1);

            dbConnInner.addStatisticTopic(statisticTopic);
        }
        dbConnExternal.close();
        dbConnInner.close();
    }

    public void setNewCard(Context context) {
        DatabaseHandlerInner dbConnInner = new DatabaseHandlerInner(context);
        int newId;

        newId = dbConnInner.addCardTopic(new CardTopic("Topics", 0));

        PersistantStorage.init(context);
        PersistantStorage.addProperty(Constants.CURRENT_CARD, String.valueOf(newId));

        int currentCountCards = 1;
        if (PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD) != null) {
            currentCountCards = Integer.valueOf(PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD)) + 1;
        }
        PersistantStorage.addProperty(Constants.CURRENT_COUNT_CARD, String.valueOf(currentCountCards));

        dbConnInner.close();
    }

    public void updateCardByIdCard(Context context, int idCard, int idTopic) {
        DatabaseHandlerInner dbConnInner = new DatabaseHandlerInner(context);
        DatabaseHandlerExternal dbConnExternal = new DatabaseHandlerExternal(context);

        dbConnInner.updateCardTopicByIdCardTopic(idCard,
                idTopic,
                dbConnExternal.getTopicNameById(idTopic));

        dbConnInner.close();
        dbConnExternal.close();
    }

    public void deleteCardByIdCard(Context context, int idCard) {
        DatabaseHandlerInner dbConnInner = new DatabaseHandlerInner(context);

        dbConnInner.deleteCardTopicByIdCardTopic(idCard);

        int currentCountCards = 1;
        PersistantStorage.init(context);
        if (PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD) != null) {
            currentCountCards = Integer.valueOf(PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD)) - 1;
        }
        PersistantStorage.addProperty(Constants.CURRENT_COUNT_CARD, String.valueOf(currentCountCards));

        dbConnInner.close();
    }
}

