package com.example.dailysmarts.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dailysmarts.MainActivity;
import com.example.dailysmarts.R;
import com.example.dailysmarts.database.DatabaseInstance;
import com.example.dailysmarts.database.SmartEntity;
import com.example.dailysmarts.recyclerView.SmartAdapter;
import com.example.dailysmarts.recyclerView.SmartViewHolder;

import java.util.List;

public class MyQuotesFragment extends Fragment  {

    private final String LIKE = "like";
    private final String SHARE = "share";

    private DatabaseInstance db;
    private View viewRoot;

    public MyQuotesFragment() {
    }
    public static MyQuotesFragment newInstance() {
        return new MyQuotesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_my_quotes, container, false);
        db = DatabaseInstance.getInstance(this.getContext());
        createRecycleWithDataFromDB();
        return viewRoot;
    }

    private void createRecycleWithDataFromDB() {
        db.getAll(data -> {
            RecyclerView recyclerView = viewRoot.findViewById(R.id.rec_view);
            SmartAdapter smartAdapter = new SmartAdapter(getClickListener());
            smartAdapter.setSmartEntities(data);
            recyclerView.setAdapter(smartAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private SmartViewHolder.ItemListener getClickListener() {
        return (entity, imageButton) -> {
            String btnDescription = imageButton.getContentDescription().toString();
            if(btnDescription.equals(LIKE)) {
                    deleteFromDatabase(entity.getId());
            } else if(btnDescription.equals(SHARE)) {
             shareSmarty(entity);
            }
        };
    }

    private void deleteFromDatabase(int id) {
                db.deleteByEntityId(data -> createRecycleWithDataFromDB(), id);
        Toast.makeText(getContext(), "The smarty was deleted from your favorite smarties", Toast.LENGTH_SHORT).show();
    }

    private void shareSmarty(SmartEntity entity) {
        String body = "Hey there is an interesting thought for you:\n"  + entity.getQuoteText() +
                "\nfrom: " + entity.getQuoteAuthor();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(intent);

    }
}
