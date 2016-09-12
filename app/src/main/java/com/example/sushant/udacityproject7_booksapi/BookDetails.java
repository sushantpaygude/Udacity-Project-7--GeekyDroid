package com.example.sushant.udacityproject7_booksapi;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by sushant on 9/9/16.
 */
public class BookDetails {
    String book_title;
    String book_publisher;
    ArrayList<String> book_author;
    String book_thumbnail;

    public BookDetails(String book_title, String book_publisher, ArrayList<String> book_author, String book_thumbnail) {
        this.book_title = book_title;
        this.book_publisher = book_publisher;
        this.book_author = book_author;
        this.book_thumbnail = book_thumbnail;
    }

    public String getBook_title() {
        return book_title;
    }


    public String getBook_publisher() {
        return book_publisher;
    }


    public ArrayList<String> getBook_author() {
        return book_author;
    }


    public String getBook_thumbnail() {
        return book_thumbnail;
    }


}
