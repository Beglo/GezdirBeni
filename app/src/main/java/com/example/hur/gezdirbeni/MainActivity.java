package com.example.hur.gezdirbeni;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String exetext;
    Boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etPW_label = (EditText)findViewById(R.id.etPW_label);
        final EditText etID_label = (EditText)findViewById(R.id.etID_label);
        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        pulldata();



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testt = exetext;
                pulldata();
                if(connected) {
                    if (etID_label.getText().toString().equals(exetext) &&
                            etPW_label.getText().toString().equals("siegopls")) {
                        Toast.makeText(getApplicationContext(), "right!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "WRONG!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "dsadda!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void pulldata (){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
                getJSON("http://www.hurtursen.com/content2/getdata.php");
                getJSON("http://www.hurtursen.com/content2/getdata.php");
            } else {
                connected = false;
                Toast.makeText(getApplicationContext(), "Internet Connection is not established!!!",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getJSON(final String urlWebService){

        class GetJSON extends AsyncTask<Void, Void, String> {
            protected void onPreExecute(){
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
             //   Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject obj = jsonArray.getJSONObject(0);
                    exetext = obj.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            protected String doInBackground(Void... voids) {
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                            (con.getInputStream()));
                    String json;

                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

}
