package com.example.mor.yammiwithshabi.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.util.Log;

import com.example.mor.yammiwithshabi.MyApplication;

@Database(entities = {FeedItem.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract FeedItemDao feedItemDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db = Room.databaseBuilder(MyApplication.context,
            AppLocalDbRepository.class,
            "feedItems.db").fallbackToDestructiveMigration().build();
}
