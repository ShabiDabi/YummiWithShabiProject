package com.example.mor.yammiwithshabi.model;

import android.os.AsyncTask;

import java.util.List;

public class FeedItemAsyncDao {

    interface FeedItemAsyncDaoListener<T>{
        void onComplete(T data);
    }
    static public void getAll(final FeedItemAsyncDaoListener<List<FeedItem>> listener) {
        class MyAsynchTask extends AsyncTask<String,String,List<FeedItem>>{
            @Override
            protected List<FeedItem> doInBackground(String... strings) {
                List<FeedItem> feedItems = AppLocalDb.db.studentDao().getAll();
                return feedItems;
            }

            @Override
            protected void onPostExecute(List<FeedItem> feedItem) {
                super.onPostExecute(feedItem);
                listener.onComplete(feedItem);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }


    static void insertAll(final List<FeedItem> feedItems, final FeedItemAsyncDaoListener<Boolean> listener){
        class MyAsynchTask extends AsyncTask<List<FeedItem>,String,Boolean>{
            @Override
            protected Boolean doInBackground(List<FeedItem>... feedItems) {
                for (FeedItem feedItem:feedItems[0]) {
                    AppLocalDb.db.studentDao().insertAll(feedItem);
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
