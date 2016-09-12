package com.example.sushant.udacityproject7_booksapi;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String BOOK_REQUEST_URL;
    CustomCardAdapter cardAdapter;
    ListView listView;
    EditText textQuery;
    ImageButton searchButton;
    String stringQuery;
    LinearLayout startLayout;
    ListView cardListview;
    TextView textTitle;
    TextView textResults;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textQuery=(EditText)findViewById(R.id.text_query);
        startLayout=(LinearLayout)findViewById(R.id.layout_start);
        cardListview=(ListView)findViewById(R.id.card_listView);
        searchButton=(ImageButton)findViewById(R.id.button_search);

        textTitle=(TextView)findViewById(R.id.text_title);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/comfortaabold.ttf");
        textTitle.setTypeface(typeface);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringQuery=textQuery.getText().toString();
                BOOK_REQUEST_URL="https://www.googleapis.com/books/v1/volumes?q="+stringQuery+"&maxResults=20";
                Log.e("URL:",""+BOOK_REQUEST_URL);
                BooksAsyncTask booksAsyncTask=new BooksAsyncTask();
                booksAsyncTask.execute();
                startLayout.setVisibility(View.INVISIBLE);
                cardListview.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Searching",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class BooksAsyncTask extends AsyncTask<URL,Void,ArrayList<BookDetails>>
    {

        @Override
        protected ArrayList<BookDetails> doInBackground(URL... params) {
            //URL OBJECT CREATION
            BOOK_REQUEST_URL=BOOK_REQUEST_URL.replaceAll("\\s","");
            URL url=createURLObject(BOOK_REQUEST_URL);
            String jsonResponse="";
            try{
                jsonResponse=makeHTTPRequest(url);
            }
            catch (IOException e)
            {
                Log.e("EXCEPTION:",""+e);
            }
            ArrayList<BookDetails> bookDetailsArray=extractBookDetails(jsonResponse);
            return bookDetailsArray;
        }

        @Override
        protected void onPostExecute(ArrayList<BookDetails> bookDetailsArray) {
            if(bookDetailsArray==null)
            {
                return;
            }

                    UpdateUI(bookDetailsArray);

        }

        private void UpdateUI( ArrayList<BookDetails> bookDetails){


            listView=(ListView)findViewById(R.id.card_listView);
            cardAdapter=new CustomCardAdapter(getApplicationContext(),R.layout.list_item_card);
            textResults=(TextView)findViewById(R.id.text_results);

            for(int i=0;i<bookDetails.size();i++) {
                cardAdapter.add(bookDetails.get(i));
            }
            listView.setAdapter(cardAdapter);
            if(listView.getCount()==0)
            {
                textResults.setText("No match found");
            }
            else{
                textResults.setText("Found "+listView.getCount()+" matches:");
            }
        }

        private URL createURLObject(String stringURL)
        {
            URL url=null;
            try {
                url=new URL(stringURL);
            }
            catch (MalformedURLException exception){
                Log.e("Error with URL:",""+exception);
            }
            Log.e("","URL OBJECT CREATED");

            return url;
        }

        private String makeHTTPRequest(URL url)throws IOException{
            String jsonResponse=null;
            InputStream inputStream=null;
            HttpURLConnection urlConnection=null;

            try{
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }
            catch (IOException e){
                Log.e("URL Connection Failed:",""+e);
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {

                    inputStream.close();
                }
            }
            Log.e("","HTTP REQUEST MADE");

            return  jsonResponse;
        }

        private String readFromStream(InputStream inputStream)throws IOException
        {
            StringBuilder streamOutput=new StringBuilder();
            if(inputStream!=null)
            {
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                String line=bufferedReader.readLine();
                while (line!=null)
                {
                    streamOutput.append(line);
                    line=bufferedReader.readLine();
                }
            }

            return  streamOutput.toString();

        }

        private ArrayList<BookDetails> extractBookDetails(String jsonResponse)
        {
            ArrayList<String> book_author=new ArrayList<>();
            BookDetails bookDetails=null;
            ArrayList<BookDetails> bookDetailsArray=new ArrayList<>();

            try{
                JSONObject rootObject=new JSONObject(jsonResponse);
                JSONArray itemArray=rootObject.getJSONArray("items");

                if (itemArray.length()>0)
                {

                        for(int j=0;j<itemArray.length();j++)
                        {

                        JSONObject firstBook=itemArray.getJSONObject(j);
                        JSONObject volumeInfo=firstBook.getJSONObject("volumeInfo");
                        String book_title=volumeInfo.getString("title");
                        String book_subtitle=volumeInfo.getString("publisher");
                        JSONArray authorArray=volumeInfo.getJSONArray("authors");

                            for (int i=0;i<authorArray.length();i++) {
                                book_author.add(authorArray.getString(i));
                            }

                        JSONObject imageLinks=volumeInfo.getJSONObject("imageLinks");
                        String book_thumbnail=imageLinks.getString("thumbnail");
                        bookDetails=new BookDetails(book_title,book_subtitle,book_author,book_thumbnail);
                        bookDetailsArray.add(bookDetails);
                        }
                }
            }
            catch (JSONException e)
            {
                Log.e("", "Problem parsing the book JSON results", e);
            }
           return bookDetailsArray;
        }
    }
}

