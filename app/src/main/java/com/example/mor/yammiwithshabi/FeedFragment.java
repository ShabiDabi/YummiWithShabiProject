package com.example.mor.yammiwithshabi;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mor.yammiwithshabi.model.FeedItem;
import com.example.mor.yammiwithshabi.model.Model;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class FeedFragment extends Fragment {
    //private OnFragmentInteractionListener mListener;

    ListView list;
    MyAdapter myAdapter = new MyAdapter();;
    FeedViewModel dataModel;

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        dataModel.getData().observe(this, new Observer<List<FeedItem>>() {
            @Override
            public void onChanged(@Nullable List<FeedItem> feedItems) {
                myAdapter.notifyDataSetChanged();
                Log.d("TAG","notifyDataSetChanged" + feedItems.size());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance.cancellGetAllFeedItens();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feed_items_list, container, false);

        list = view.findViewById(R.id.Feed);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","item selected:" + i);
            }
        });
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


    class MyAdapter extends BaseAdapter {
        public MyAdapter(){
        }

        @Override
        public int getCount() {
            Log.d("TAG","list size:" + dataModel.getData().getValue().size());

            return dataModel.getData().getValue().size();

        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.feed_item_view,null);
            }

            final FeedItem currFeedItem = dataModel.getData().getValue().get(i);

            TextView email = view.findViewById(R.id.user_email_text);
            TextView recipe = view.findViewById(R.id.recipe_text);
            EditText date = view.findViewById(R.id.date_created);
            final ImageView dessertPicture = view.findViewById(R.id.dessert_image);


            email.setText(currFeedItem.getEmail());
            recipe.setText(currFeedItem.getText());
            date.setText(currFeedItem.getDateCreated());

            if (currFeedItem.picture != null){
                Model.instance.getImage(currFeedItem.picture, new Model.GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (currFeedItem.id.equals(dessertPicture.getTag()) && imageBitmap != null) {
                            dessertPicture.setImageBitmap(imageBitmap);
                        }
                    }
                });
            }else{
                dessertPicture.setImageResource(R.drawable.strawberry);
            }
            return view;
        }
    }
}
