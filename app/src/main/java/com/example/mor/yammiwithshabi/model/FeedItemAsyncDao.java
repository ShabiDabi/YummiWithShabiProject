package com.example.mor.yammiwithshabi.model;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class FeedItemAsyncDao {

    interface FeedItemAsyncDaoListener<T>{
        void onComplete(T data);
    }

    static public void getAll(final FeedItemAsyncDaoListener<List<FeedItem>> listener) {
        Log.d("TAG", "FeedItemAsyncDao.getAll: start");
        class MyAsynchTask extends AsyncTask<String,String,List<FeedItem>>{
            @Override
            protected List<FeedItem> doInBackground(String... strings) {
                Log.d("TAG", "FeedItemAsyncDao.getAll, class MyAsynchTask::doInBackground, strings = " + strings);
                List<FeedItem> feedItems = AppLocalDb.db.feedItemDao().getAll();
                Log.d("TAG", "FeedItemAsyncDao.getAll, MyAsynchTask::doInBackground, after AppLocalDb getAll");
                return feedItems;
            }

            @Override
            protected void onPostExecute(List<FeedItem> feedItem) {
                Log.d("TAG", "FeedItemAsyncDao.getAll, MyAsynchTask::onPostExecute start");
                super.onPostExecute(feedItem);
                listener.onComplete(feedItem);
                Log.d("TAG", "FeedItemAsyncDao.getAll, MyAsynchTask::onPostExecute after listener.onComplete");
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute();
        Log.d("TAG", "FeedItemAsyncDao.getAll, after task execute");
    }


    static void insertAll(final List<FeedItem> feedItems, final FeedItemAsyncDaoListener<Boolean> listener){
        class MyAsynchTask extends AsyncTask<List<FeedItem>,String,Boolean>{
            @Override
            protected Boolean doInBackground(List<FeedItem>... feedItems) {
                for (FeedItem feedItem:feedItems[0]) {
                    AppLocalDb.db.feedItemDao().insertAll(feedItem);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute(feedItems);
    }

}
