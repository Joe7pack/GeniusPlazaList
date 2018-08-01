package com.guzzardo.genuisplazalist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;

public class AddEmployeeActivityJava extends AppCompatActivity {

    static private Resources mResources;
    EditText firstName;
    private EditText lastName;
    static Context mContext;
    static String firstNameText;
    static String lastNameText;
    private static MyApplication myApplication;
    static private MainPresenter presenter;
    static ConstraintLayout constraintLayout;
    static AddEmployeeActivity addEmployeeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mResources = getResources();
        myApplication = ((MyApplication) this.getApplication());
        mContext = getApplicationContext();
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        boolean keep = intent.getExtras().getBoolean("keep");
        if(keep!=true) {
            this.finish();
        }

        setContentView(R.layout.add_employee_detail);
        constraintLayout = (ConstraintLayout)findViewById(R.id.add_employee_layout);
        presenter = ((MyApplication)this.getApplication()).getPresenter();

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_employee_tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(mBackIconPressed);

        // Capture Add button from layout
        Button addButton = (Button)findViewById(R.id.add_employee_button);
        // Register the onClick listener with the implementation above
        addButton.setOnClickListener(mAddContactListener);

        // Capture Cancel button from layout
        Button cancelButton = (Button)findViewById(R.id.cancel_add_employee_button);
        // Register the onClick listener with the implementation above
        cancelButton.setOnClickListener(mCancelAddContactListener);

        firstName = this.findViewById(R.id.edit_first_name);
        lastName = this.findViewById(R.id.edit_last_name);
    }

    public static void done() {
        System.out.println("add done");
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mBackIconPressed = new View.OnClickListener() {
        public void onClick(View v) {
            onBackPressed();
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mAddContactListener = new View.OnClickListener() {
        public void onClick(View v) {
            firstNameText = firstName.getText().toString();
            lastNameText = lastName.getText().toString();
            System.out.println("got to here!");
            if (firstNameText.trim().length() == 0 || lastNameText.trim().length() == 0) {
                String result = "Please enter a first and a last name";
                Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                return;
            }
            addNewContact();
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mCancelAddContactListener = new View.OnClickListener() {
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addNewContact() {
        new SendPostRequest(mContext).execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean  keep = intent.getExtras().getBoolean("keep");
        if (keep==false)
            this.finish();
    }

    static private class SendPostRequest extends AsyncTask<String, String, String> {
        Context mContext;

        SendPostRequest(Context context) {
            mContext = context;
        }

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("https://reqres.in/api/users"); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("first_name", firstNameText);
                postDataParams.put("last_name", lastNameText);
                postDataParams.put("avatar", "https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg");
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                    BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();
                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            presenter.getModel().setEmployeeList();
            MainActivity mainActivity = (MainActivity)myApplication.getContext();
            mainActivity.loadListView2(presenter.getModel().getEmployeeList());
            done();
        }
    }

    public static String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();

        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}
