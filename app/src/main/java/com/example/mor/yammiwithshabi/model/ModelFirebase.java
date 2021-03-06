package com.example.mor.yammiwithshabi.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {
    public void addSFeedItem(FeedItem feedItem){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("feedItems").child(feedItem.id).setValue(feedItem);
    }

    public void cancelGetAllFeedItems() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("feedItems");
        stRef.removeEventListener(eventListener);
    }

    interface GetAllFeedItemsListener{
        public void onSuccess(List<FeedItem> feedItems);
    }

    ValueEventListener eventListener;

    public void getAllFeedItems(final GetAllFeedItemsListener listener) {
        Log.d("TAG", "ModelFirebase.getAllFeedItems start");
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("feedItems");

        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG", "ModelFirebase.getAllFeedItems.eventListener.onDataChange start");
                List<FeedItem> feedItems = new LinkedList<>();

                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    FeedItem feedItem = stSnapshot.getValue(FeedItem.class);
                    Log.d("TAG", "ModelFirebase.getAllFeedItems.eventListener.onDataChange in snapshot loop, feedItem: id = " + feedItem.id + ", picture = " + feedItem.picture);
                    feedItems.add(feedItem);
                }
                listener.onSuccess(feedItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Managing Files
    public void saveImage(Bitmap imageBitmap, final Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        Date d = new Date();
        String name = ""+ d.getTime();
        StorageReference imagesRef = storage.getReference().child("pictures").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onDone(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.onDone(downloadUrl.toString());
            }
        });

    }


    public void getImage(String url, final Model.GetImageListener listener){
        Log.d("TAG","ModelFirebase.getImage: url = " + url + ", starting");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024 *5;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Log.d("TAG","get image from firebase success");
                listener.onDone(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                Log.d("TAG","get image from firebase Failed");
                listener.onDone(null);
            }
        });
    }

}
