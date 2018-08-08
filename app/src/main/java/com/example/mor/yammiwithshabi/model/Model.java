package com.example.mor.yammiwithshabi.model;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;



public class Model {
    public static Model instance = new Model();
    ModelFirebase modelFirebase;
    private Model(){
        Log.d("TAG", "Model.ctor: start");
        modelFirebase = new ModelFirebase();
        Log.d("TAG", "Model.ctor: after creating ModelFirebase");
    }

    public void cancellGetAllFeedItens() {
        modelFirebase.cancelGetAllFeedItems();
    }

    class FeedItemsListData extends  MutableLiveData<List<FeedItem>>{
        @Override
        protected void onActive() {
            super.onActive();
            // new thread tsks
            // 1. get the FeedItems list from the local DB
            FeedItemAsyncDao.getAll(new FeedItemAsyncDao.FeedItemAsyncDaoListener<List<FeedItem>>() {
                @Override
                public void onComplete(List<FeedItem> data) {
                    // 2. update the live data with the new FeedItem list
                    setValue(data);
                    Log.d("TAG","Model::FeedItemsListData.AsyncDao.onComplete: got feedItems from local DB " + data.size());

                    for (FeedItem fi:data) {
                        Log.d("TAG", "Model::FeedItemsListData.AsyncDao.onComplete: current feedItem: id = " + fi.id + ", picture = " + fi.picture);
                    }

                    // 3. get the feedItem list from firebase
                    modelFirebase.getAllFeedItems(new ModelFirebase.GetAllFeedItemsListener() {
                        @Override
                        public void onSuccess(List<FeedItem> feedItems) {
                            // 4. update the live data with the new feedItems list
                            setValue(feedItems);
                            Log.d("TAG","got feedItems from firebase, length = " + feedItems.size());

                            // 5. update the local DB
                            FeedItemAsyncDao.insertAll(feedItems, new FeedItemAsyncDao.FeedItemAsyncDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            modelFirebase.cancelGetAllFeedItems();
            Log.d("TAG","cancellGetAllFeedItems");
        }

        public FeedItemsListData() {
            super();
            setValue(new LinkedList<FeedItem>());
        }
    }

    FeedItemsListData feedItemListData = new FeedItemsListData();

    public LiveData<List<FeedItem>> getAllFeedItems(){
        Log.d("TAG", "Model.getAllFeedItems");
        return feedItemListData;
    }

    public void addFeedItem(FeedItem feedItem){
        Log.d("TAG", "Model.addFeedItem start");
        modelFirebase.addSFeedItem(feedItem);
    }



    ////////////////////////////////////////////////////////
    //  Handle Image Files
    ////////////////////////////////////////////////////////

    public interface SaveImageListener{
        void onDone(String url);
    }

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap,listener);
    }



    public interface GetImageListener{
        void onDone(Bitmap imageBitmap);
    }

    public void getImage(final String url, final GetImageListener listener ){
        String localFileName = URLUtil.guessFileName(url, null, null);
        Log.d("TAG","Model.getImage: url = " + url + ", localFilename = " + localFileName);
        final Bitmap image = loadImageFromFile(localFileName);
        if (image == null) {                                      //if image not found - try downloading it from parse
            modelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (imageBitmap == null) {
                        listener.onDone(null);
                    }else {
                        //2.  save the image localy
                        String localFileName = URLUtil.guessFileName(url, null, null);
                        Log.d("TAG", "save image to cache: " + localFileName);
                        saveImageToFile(imageBitmap, localFileName);
                        //3. return the image using the listener
                        listener.onDone(imageBitmap);
                    }
                }
            });
        }else {
            Log.d("TAG","OK reading cache image: " + localFileName);
            listener.onDone(image);
        }
    }

    // Store / Get from local mem
    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        if (imageBitmap == null) return;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            Log.d("TAG","Model.loadImageFromFile: imageFileName = " + imageFileName + ", dir = " + dir.getAbsolutePath().toString());
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}





