package com.example.dailysmarts.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailysmarts.MainActivity;
import com.example.dailysmarts.R;
import com.example.dailysmarts.database.DatabaseInstance;
import com.example.dailysmarts.database.SmartEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DailyQuoteFragment extends Fragment {

    private final String EMPTY = "empty";
    private final String FULL = "full";

    private static String text;
    private static String author;
    private View view;
    private DatabaseInstance db;

    public DailyQuoteFragment() {

    }

    public static DailyQuoteFragment newInstance(String text, String author) {
        DailyQuoteFragment.text = text;
        DailyQuoteFragment.author = author;
        MyQuotesFragment fragment = new MyQuotesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return new DailyQuoteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseInstance.getInstance(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dayli_quote, container, false);
        TextView txtSmart = view.findViewById(R.id.txt_smart);
        TextView txtAuthor = view.findViewById(R.id.txt_author);
        txtSmart.setText(text);
        txtAuthor.setText(author);
        initButtons(view);
        return view;
    }

    private void initButtons(View view) {
        ImageButton buttonLike = view.findViewById(R.id.btn_like);
        ImageButton buttonShare = view.findViewById(R.id.btn_share);
        setLike(buttonLike);
        setShare(buttonShare);
    }

    private void setShare(ImageButton buttonShare) {
        buttonShare.setOnClickListener(v -> shareSmarty());
    }

    private void shareSmarty() {
        String body = "Hey there is an interesting thought for you:\n" + text +
                "\nfrom: " + author;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(intent);
    }

    private void setLike(ImageButton buttonLike) {
        buttonLike.setOnClickListener(v -> {
            if (buttonLike.getTag().equals(EMPTY)) {
                db.getSmartiesWithText(data -> {
                    if (data.size() > 1) {
                        Toast.makeText(getContext(), "The smarty is already saved", Toast.LENGTH_SHORT).show();
                    } else {
                        buttonLike.setImageResource(R.drawable.ic_favorite_green_48px);
                        saveToDatabase();
                        buttonLike.setTag(FULL);
                    }
                }, text);
            } else if (buttonLike.getTag().equals(FULL)) {
                deleteLastAdded(buttonLike);
            }
        });
    }

    private void saveToDatabase() {
        SmartEntity smartEntity = new SmartEntity(text, author);
        db.insertSingleAsync(smartEntity);
        Toast.makeText(getContext(), "The smarty was saved to your favorite smarties", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void deleteLastAdded(ImageButton buttonLike) {
        db.getTheLastAdded(data -> {
            int smartId = data.get(0).getId();
            db.deleteByEntityId(data1 -> {
                buttonLike.setImageResource(R.drawable.ic_favorite_border_green_48px);
                buttonLike.setTag(EMPTY);
                Toast.makeText(getContext(), "The smarty was deleted from your favorite smarties", Toast.LENGTH_SHORT).show();
            }, smartId);
        });
    }
}
