package com.example.sushant.udacityproject7_booksapi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sushant on 10/9/16.
 */
public class CustomCardAdapter extends ArrayAdapter<BookDetails>{

    List<BookDetails> cardList=new ArrayList<BookDetails>();

    public CustomCardAdapter(Context context, int resource) {
        super(context, resource);
    }
    static class CardViewHolder{
        ImageView bookThumbnail;
        TextView bookTitle;
        TextView bookAuthors;
        TextView bookPublisher;
    }

    @Override
    public void add(BookDetails object) {

        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public BookDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        CardViewHolder cardViewHolder;
        if(row==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.list_item_card,parent,false);
            cardViewHolder=new CardViewHolder();
            cardViewHolder.bookTitle=(TextView)row.findViewById(R.id.book_title);
            cardViewHolder.bookAuthors=(TextView)row.findViewById(R.id.book_author);
            cardViewHolder.bookPublisher=(TextView)row.findViewById(R.id.book_publisher);
            cardViewHolder.bookThumbnail=(ImageView)row.findViewById(R.id.book_thumbnail);
            row.setTag(cardViewHolder);
        }
        else{
            cardViewHolder=(CardViewHolder)row.getTag();
        }

        BookDetails bookItem=getItem(position);
        cardViewHolder.bookTitle.setText(bookItem.getBook_title());
        cardViewHolder.bookAuthors.setText(bookItem.book_author.get(position));
        cardViewHolder.bookPublisher.setText(bookItem.getBook_publisher());
        Picasso.with(getContext()).load(""+bookItem.getBook_thumbnail()).into(cardViewHolder.bookThumbnail);
        cardViewHolder.bookTitle.setTextColor(Color.BLACK);
        cardViewHolder.bookAuthors.setTextColor(Color.BLACK);
        cardViewHolder.bookPublisher.setTextColor(Color.BLACK);
        return row;
    }
}
