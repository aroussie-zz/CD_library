package com.example.alexandreroussiere.parser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends Activity {

    XMLGettersSetters data;
    ProgressDialog waitProgress;
    ListView mListView;
    CheckBox box;
    boolean showAll = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("Main activity status : ", "BEGIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BackgroundTask().execute();


    }

    public class BackgroundTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (waitProgress != null) {
                waitProgress.dismiss();
            }
        }

        @Override
        //Will be execute once the background process is over
        protected void onPostExecute(Void aVoid) {

            box = (CheckBox) findViewById(R.id.showAllBox);
            mListView = (ListView) findViewById(R.id.CD_list);
            mListView.setAdapter(new CDAdapter(getApplicationContext()));


            //This view will be the footer of the listView
            //It will show how many CDs are sold out
            TextView numberCD = new TextView(getApplicationContext());
            numberCD.setText("CD sold out: " + data.CountCdSoldOut());
            numberCD.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            numberCD.setTextSize(35);
            numberCD.setTextColor(Color.parseColor("#d50000"));

            //Add the footer
           mListView.addFooterView(numberCD);

            //The user can choose to see only sold out CDs or all of them thanks to the checkbox
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Log.i("Box checked: ", "Show all CDs");
                        box.setText("Show all Cds");
                        showAll = true;
                        mListView.setAdapter(new CDAdapter(getApplicationContext()));
                    } else {
                        Log.i("Box unchecked: ", "Show Sold out CDs");
                        box.setText("See only sold out CDs");
                        showAll = false;
                        mListView.setAdapter(new CDAdapter(getApplicationContext()));

                    }

                }
            });


            super.onPostExecute(aVoid);
            if (waitProgress != null) {
                waitProgress.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                synchronized (this) {
                    Log.i("Main Activity status: ", "Data process begins");
                    saxParser();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void saxParser() {

        try {

            /**
             * Create a new instance of the SAX parser
             **/
            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();

            // URL of the XML
            URL url = new URL("http://www.papademas.net/cd_catalog3.xml");

            /**
             * Create the Handler to handle each of the XML tags.
             **/
            XMLHandler myXMLHandler = new XMLHandler();
            xmlR.setContentHandler(myXMLHandler);
            xmlR.parse(new InputSource(url.openStream()));

            Log.i("Main Activity process: ", "Data load successfully");
            data = XMLHandler.data;

        } catch (Exception e) {
            System.out.println(e);
        }

    }

        public class CDAdapter extends BaseAdapter {

            private LayoutInflater mInflator;
            private Context mContext;
            private TextView title;
            private TextView artist;
            private TextView country;
            private TextView price;
            private TextView availibility;
            private TextView company;
            private TextView year;
            private ImageView cdView;


            public CDAdapter(Context c) {
                mContext = c;
                mInflator = (LayoutInflater)
                        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                Log.i("Main Activity process: ","New adapter create");
            }

            @Override
            public int getCount() {
                if(showAll){
                    //We want to show everything so the data size is the size of any arrays
                    return data.getTitle().size();
                }else
                    //We only want to display the CDs which are sold out
                    return data.CountCdSoldOut();

            }

            /*
            Only use if we want to show the CDs which are soldout
            Return the good index of a sold out CD
             */
            public int realPosition(int position){

                //If the position locates to an available CD we go to the next CD
                if(data.getSoldOut().get(position).equalsIgnoreCase("no")){
                    position++;
                    realPosition(position);
                }
                //Otherwise, it means the position locates to a sold out cd so it's OK
                    return position;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {


                if (convertView == null) {
                    convertView = mInflator.inflate(R.layout.cd_layout, parent,
                            false);
                }
                //If we just want to show the sold ou CDs
               if(!showAll){
                   position=realPosition(position);
               }

               cdView = (ImageView) convertView.findViewById(R.id.picture_CD);
               cdView.setImageResource(R.drawable.cd);

               title = (TextView) convertView.findViewById(R.id.titleView);
               title.setText("Title: " + data.getTitle().get(position));

               artist = (TextView) convertView.findViewById(R.id.artistView);
               artist.setText("Artist: " + data.getArtist().get(position));

               country = (TextView) convertView.findViewById(R.id.countryView);
               country.setText("Country: " + data.getCountry().get(position));

                price = (TextView) convertView.findViewById(R.id.priceView);
                price.setText("Price: " + data.getPrice().get(position));

                company = (TextView) convertView.findViewById(R.id.companyView);
                company.setText("Price: " + data.getCompany().get(position));

                year = (TextView) convertView.findViewById(R.id.yearView);
                year.setText("Price: " + data.getYear().get(position));

               availibility = (TextView) convertView.findViewById(R.id.availibilityView);

                //We display the availability of the CD
                if(data.getSoldOut().get(position).equalsIgnoreCase("yes")){
                    availibility.setTextColor(Color.parseColor("#d50000"));
                    availibility.setText("SOLD OUT");
                }else{
                    availibility.setTextColor(Color.parseColor("#00c853"));
                    availibility.setText("AVAILABLE");
                }

                return convertView;

            }
        }
}

