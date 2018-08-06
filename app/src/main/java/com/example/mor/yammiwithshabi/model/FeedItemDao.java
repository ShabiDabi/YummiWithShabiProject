package com.example.mor.yammiwithshabi.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FeedItemDao {
    @Query("select * from FeedItem")
    List<FeedItem> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FeedItem... feedItems);

    @Delete
    void delete(FeedItem feedItem);
}
