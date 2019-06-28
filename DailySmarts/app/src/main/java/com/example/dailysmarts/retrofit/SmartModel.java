package com.example.dailysmarts.retrofit;

import java.util.Calendar;

public class SmartModel {

    private String quoteText;
    private String quoteAuthor;
    private String date;

    public SmartModel(String quoteText, String quoteAuthor) {
        this.quoteText = quoteText;
        this.quoteAuthor = quoteAuthor;
      setDate();
    }

    public String  getDate() {
        return date;
    }

    public void setDate() {
        this.date =  java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }



}
