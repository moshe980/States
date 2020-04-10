package com.example.ep.myapplication.Activitys.Services;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.ep.myapplication.Activitys.Adapters.StateAdapter;
import com.example.ep.myapplication.Activitys.Model.State;
import com.example.ep.myapplication.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public   class DataService {

    private  ArrayList<State> arrState2 = new ArrayList<>();


    public  State getNstateFromBstate(String stateCode) {

        ArrayList<String> arrS = new ArrayList<>();
        String sURL = "https://restcountries.eu/rest/v2/alpha/" + stateCode+"?fields=name;nativeName"; // gets a state by code
        String name = null;
        State s = null;
        // Connect to the URL using java's native library
        URL url = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            url = new URL(sURL);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection request = (HttpURLConnection) url.openConnection();

            request.connect();


            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element

            JsonObject rootobj = root.getAsJsonObject();

            JsonElement entriesname = rootobj.get("name");
            JsonElement entriesnative = rootobj.get("nativeName");

            String nameR = entriesname.toString().replace("\"",""); // convert to pure string
            String nativen = entriesnative.toString().replace("\"","");

            s = new State(nameR,nativen);

        } catch (IOException e1) {
            e1.printStackTrace();
        }


        return s;

    }
    public static class DownloadData extends AsyncTask<String, Void, ArrayList<State>> {
        ArrayList<State> arrState;
        RecyclerView recyclerView;
        View v;

        public DownloadData(View v) {
            this.v=v;
        }

        @Override
        protected ArrayList<State> doInBackground(String... strings) {
            System.out.println("doing background");
            arrState=new ArrayList<>();

            String sURL = "https://restcountries.eu/rest/v2/all?fields=name;nativeName;borders;flag"; // get all states

            // Connect to the URL using java's native library
            URL url = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try{
                url = new URL(sURL);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection request = (HttpURLConnection) url.openConnection();

                request.connect();

                // Convert to a JSON object to print data
                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element

                JsonArray rootobj = root.getAsJsonArray(); //May be an array, may be an object.


                for (JsonElement je : rootobj) {
                    JsonObject obj = je.getAsJsonObject(); //since you know it's a JsonObject
                    JsonElement entriesname = obj.get("name");//will return members of your object
                    JsonElement entriesnative = obj.get("nativeName");
                    JsonElement entriesborders = obj.get("borders");
                    JsonElement entriesflag = obj.get("flag");

                    String name = entriesname.toString().replace("\"","");
                    String nativen = entriesnative.toString().replace("\"","");
                    String flag = entriesflag.toString().replace("\"","");

                    ArrayList<String> arrBorders = new ArrayList<String>();
                    JsonArray entriesbordersArray = entriesborders.getAsJsonArray();

                    for(JsonElement j : entriesbordersArray){

                        String s = j.toString().replace("\"","");
                        arrBorders.add(s);
                    }

                    arrState.add(new State(name, arrBorders,nativen,flag));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (NetworkOnMainThreadException e)
            {
                e.printStackTrace();
            }

            return arrState;
        }

        @Override
        protected void onPostExecute(final ArrayList<State> s) {
            super.onPostExecute(s);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewsir);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

            final StateAdapter[] theAdapter = {new StateAdapter((FragmentActivity) v.getContext(), s)};

            recyclerView.setAdapter(theAdapter[0]);

            EditText inputSearch = (EditText) v.findViewById(R.id.inputSearch);


            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text

                    theAdapter[0] = new StateAdapter((FragmentActivity) v.getContext(), theAdapter[0].custumeFilter(s, cs.toString()));
                    recyclerView.setAdapter(theAdapter[0]);

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }


   
}



