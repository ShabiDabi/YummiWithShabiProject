package com.example.mor.yammiwithshabi;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;


import com.example.mor.yammiwithshabi.model.FeedItem;
import com.example.mor.yammiwithshabi.model.Model;

import java.util.LinkedList;
import java.util.List;

public class FeedViewModel extends ViewModel {
    LiveData<List<FeedItem>> data;

    public LiveData<List<FeedItem>> getData(){
        data = Model.instance.getAllFeedItems();
        return data;
    }


}
