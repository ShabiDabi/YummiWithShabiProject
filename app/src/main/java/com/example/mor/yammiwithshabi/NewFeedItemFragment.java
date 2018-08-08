package com.example.mor.yammiwithshabi;

import android.app.Activity;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;


import com.example.mor.yammiwithshabi.model.FeedItem;
import com.example.mor.yammiwithshabi.model.Model;
import com.google.firebase.auth.FirebaseAuth;

import static android.app.Activity.RESULT_OK;



public class NewFeedItemFragment extends Fragment {
    private static final String ARG_NAME = "ARG_NAME";
    private static final String ARG_ID = "ARG_ID";

    public NewFeedItemFragment() {
        // Required empty public constructor
    }

    EditText recipe;
    ImageView dessertPicture;
    TextView emialView;
    static final int SELECT_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_feeditem, container, false);

        emialView = view.findViewById(R.id.email_view);
        recipe = view.findViewById(R.id.text_recipe);

        // Set Email to user Email
        emialView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Button save = view.findViewById(R.id.send_feeditem_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FeedItem feedItem = new FeedItem();
                feedItem.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                feedItem.setText(recipe.getText().toString());
                feedItem.setbDateCreated(java.time.LocalDate.now().toString());

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();

                feedItem.setId(randomUUIDString);

                //save image
                if (imageBitmap != null) {
                    Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            //save student obj
                            feedItem.setPicture(url);
                            Model.instance.addFeedItem(feedItem);
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
                }
            }
        });


        Button cancel = view.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        /*if (savedInstanceState != null) {
            String name = savedInstanceState.getString(ARG_NAME);
            if (name != null) {
                nameEt.setText(name);
            }
            String id = savedInstanceState.getString(ARG_ID);
            if (id != null) {
                idEt.setText(id);
            }
        }*/

        Button editImage = view.findViewById(R.id.set_image);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //select picture
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
            }
        });
        dessertPicture = view.findViewById(R.id.new_dessert_picture);
        return view;
    }

    Bitmap imageBitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {

                        imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        dessertPicture.setImageBitmap(imageBitmap);

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    /*@Override
    public void  onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putString(ARG_NAME, nameEt.getText().toString());
        bundle.putString(ARG_ID, idEt.getText().toString());
    }*/

}

