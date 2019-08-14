package domain.comany.venkat.srollview;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener,OnShowcaseEventListener {


    private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> data_bkup = new ArrayList<>();
    private ArrayList<ArrayAdapter<String>> aa = new ArrayList<ArrayAdapter<String>>();
    private ArrayList<ListView> mListView = new ArrayList<ListView>();
    private ArrayList<TextView> mTextView = new ArrayList<TextView>();
    private String[] mCategoryName;
    private String[] mCategoryType;
    private int idm=123;
    double mLatitude=0;
    double mLongitude=0;
    private EditText titleBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        titleBox = new EditText(MainActivity.this);
        titleBox.setId(R.id.edittextid);
        titleBox.setHint("Item Name");
        titleBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        titleBox.setPaddingRelative(100, 100, 100, 100);
        titleBox.setBackgroundResource(android.R.color.transparent);

        mCategoryName = getResources().getStringArray(R.array.category_name);
        mCategoryType = getResources().getStringArray(R.array.category_type);

        for (int i = 0; i < mCategoryName.length; i++) {
            data.add(new ArrayList<String>());
            data_bkup.add(new ArrayList<String>());
            aa.add(new ArrayAdapter<String>(this, R.layout.row_layout, R.id.txt_title, data.get(i)));
        }
        mListView.add((ListView) findViewById(R.id.listView1));
        mListView.add((ListView) findViewById(R.id.listView2));
        mListView.add((ListView) findViewById(R.id.listView3));
        mListView.add((ListView) findViewById(R.id.listView4));
        mListView.add((ListView) findViewById(R.id.listView5));
        mListView.add((ListView) findViewById(R.id.listView6));
        mListView.add((ListView) findViewById(R.id.listView7));
        mListView.add((ListView) findViewById(R.id.listView8));


        mTextView.add((TextView) findViewById(R.id.textView1));
        mTextView.add((TextView) findViewById(R.id.textView2));
        mTextView.add((TextView) findViewById(R.id.textView3));
        mTextView.add((TextView) findViewById(R.id.textView4));
        mTextView.add((TextView) findViewById(R.id.textView5));
        mTextView.add((TextView) findViewById(R.id.textView6));
        mTextView.add((TextView) findViewById(R.id.textView7));
        mTextView.add((TextView) findViewById(R.id.textView8));


        final TextView emptyTextView = (TextView)findViewById(R.id.emptyText);
        String emp ="Shopping List is Empty";
        emptyTextView.setText(emp);
        emptyTextView.setTextColor(Color.rgb(195,195,195));
        emptyTextView.setGravity(Gravity.CENTER|Gravity.BOTTOM);
        emptyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        emptyTextView.setVisibility(View.GONE);

        for (int i = 0; i < mListView.size(); i++) {
            mListView.get(i).setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mListView.get(i).setAdapter(aa.get(i));
            ListUtils.setDynamicHeight(mListView.get(i));

        }

        int counter=0;
        for (int i = 0; i < mTextView.size(); i++) {
            StringBuilder str = new StringBuilder(" ");

            str.append(mCategoryName[i]);
            mTextView.get(i).setText(str);
            mTextView.get(i).setTypeface(null, Typeface.BOLD_ITALIC);
            mTextView.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
//            mTextView.get(i).setHeight(100);
//            mTextView.get(i).setBackgroundColor(Color.BLACK);
            mTextView.get(i).setTextColor(Color.rgb(195,195,195));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                Drawable dr =  getResources().getDrawable(R.drawable.textview_border);
                mTextView.get(i).setBackground(dr); //Comment on lower API
            }
            if (mListView.get(i).getAdapter().getCount() == 0) {
                counter++;
                mTextView.get(i).setVisibility(View.GONE);
                mListView.get(i).setVisibility(View.GONE);
            }

        }
        if(counter==mTextView.size()){
            emptyTextView.setVisibility(View.VISIBLE);
        }


        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());


        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available


            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }


        } else { // Google Play Services are available
            // Getting LocationManager object from System Service LOCATION_SERVICE


            // Getting Current Location From GPS
            try {

                locationInit();




            } catch (SecurityException e) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
                }
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }

                Log.e("location", "security");
            }
        }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        //ALERT BOX
// 1. Instantiate an AlertDialog.Builder with its constructor
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics
                        builder.setTitle("Enter an Item");

//                final EditText edittext = new EditText(MainActivity.this);
//                builder.setView(edittext);


//From here
                        LinearLayout layout = new LinearLayout(MainActivity.this);
                        layout.setId(R.id.layout_id);
                        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
//                        final EditText titleBox = new EditText(MainActivity.this);
//                        titleBox.setHint("Item Name");
//                        titleBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
//                        titleBox.setPaddingRelative(100, 100, 100, 100);
//                        titleBox.setBackgroundResource(android.R.color.transparent);
                        layout.removeAllViews();
                        if(titleBox.getParent()!=null)
                            ((ViewGroup)titleBox.getParent()).removeView(titleBox);
                        titleBox.setText("");
                        layout.addView(titleBox); // Notice this is an add method

                        Button speech = new Button(MainActivity.this);
                        speech.setText("Click to record");
                        speech.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                                        "Say something");
                                try {
                                    startActivityForResult(intent, 100);
                                } catch (ActivityNotFoundException a) {
                                    Toast.makeText(getApplicationContext(),
                                            "Something unexpected happened",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        layout.addView(speech);
// Add another TextView here for the "Description" label


                        final Spinner spin = new Spinner(MainActivity.this);
                        spin.setPaddingRelative(100, 0, 100, 10);
                        spin.setScrollBarSize(10);
                        String[] mPlaceName = null;
                        mPlaceName = getResources().getStringArray(R.array.category_name);

//                spin.setBackgroundColor(Color.rgb(192,192,192));
                        // Creating an array adapter with an array of Place types
                        // to populate the spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, mPlaceName);

                        // Getting reference to the Spinner

                        // Setting adapter on Spinner to set place types
                        spin.setAdapter(adapter);
                        layout.addView(spin);
                        builder.setView(layout); // Again this is a set method, not add
//Till here


                        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String entered_string = titleBox.getText().toString();
                                int pos = spin.getSelectedItemPosition();
                                if (!entered_string.equals("")) {
                                    data.get(pos).add("  "+entered_string);
//                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                SharedPreferences.Editor editor = prefs.edit();
//                                Gson gson = new Gson();
//                                String json = gson.toJson(data);
//                                editor.putString("data", json);
//                                editor.commit();     // This line is IMPORTANT !!!
                                    ArrayAdapter new_aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_layout, R.id.txt_title, data.get(pos));
                                    aa.set(pos, new_aa);
                                    mListView.get(pos).setAdapter(aa.get(pos));
                                    aa.get(pos).notifyDataSetChanged();
                                    ListUtils.setDynamicHeight(mListView.get(pos));
                                    int mcount = mListView.get(pos).getAdapter().getCount();
                                    if (mcount != 0) {
                                        mTextView.get(pos).setVisibility(View.VISIBLE);
                                        mListView.get(pos).setVisibility(View.VISIBLE);
                                        emptyTextView.setVisibility(View.GONE);
                                    }
                                }
                                final Button button = new Button(MainActivity.this);
                                button.setText("Hide");
                                button.setVisibility(View.GONE);
                                ViewTarget target= new ViewTarget(R.id.dummy,MainActivity.this);
                                ShowcaseView showcaseView = new ShowcaseView.Builder(MainActivity.this)
                                        .setTarget(target)
                                        .setContentTitle("The Title takes You to Your Stores")
                                        .setContentText("Click here to show nearby stores for that category")
                                        .setStyle(R.style.CustomShowcaseTheme3)
                                        .withNewStyleShowcase()
                                        .hideOnTouchOutside()
                                        .replaceEndButton(button)
                                        .singleShot(1)
                                        .build();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

// 3. Get the AlertDialog from create()
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);


                    }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                }
            });


//        mTestView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mTestView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Hello too",Toast.LENGTH_SHORT).show();
//
//    }
//});

            FloatingActionButton del = (FloatingActionButton) findViewById(R.id.del);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = 0;
                    int counter = 0;
                    int del_counter=0;
//                    data_bkup=(ArrayList<ArrayList<String>>)data.clone();
                    for(int i=0;i<mListView.size();i++){
                        data_bkup.set(i,(ArrayList<String>)data.get(i).clone());

                    }
                    for (int j = 0; j < mListView.size(); j++) {
                        SparseBooleanArray checked = mListView.get(j).getCheckedItemPositions();
                        ArrayList<String> temp_list = new ArrayList<>();
                        count = mListView.get(j).getAdapter().getCount();

                        for (int k = 0; k < count; k++) {
                            if (checked.get(k)) {
                                del_counter++;
                                temp_list.add(mListView.get(j).getItemAtPosition(k).toString());

                            }
                        }
                        for (int q = 0; q < temp_list.size(); q++) {
                            data.get(j).remove(temp_list.get(q));
                        }
                        if (data.get(j).size() == 0) {
                            mTextView.get(j).setVisibility(View.GONE);
                            mListView.get(j).setVisibility(View.GONE);

                        }
                        aa.get(j).notifyDataSetChanged();
                        for (int i = 0; i < mListView.get(j).getChildCount(); i++) {
                            mListView.get(j).setItemChecked(i, false);
                        }
                        ListUtils.setDynamicHeight(mListView.get(j));

                        if(mListView.get(j).getAdapter().getCount() == 0){
                            counter++;
                        }

                    }
                    if(counter == mListView.size()){
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                    if(del_counter!=0){
                        data_bkup.get(0);
                        Snackbar snackbar = Snackbar.make(v,"Selected Items Deleted",Snackbar.LENGTH_LONG);
                        snackbar.setAction("Undo",new MyUndoListener());
                        snackbar.show();
                    }
                    else{
                        Snackbar.make(v,"No Items Selected",Snackbar.LENGTH_SHORT).show();

                    }
                }
            });


            FloatingActionButton share = (FloatingActionButton)findViewById(R.id.share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int count=0;
                    for(int k=0;k<data.size();k++){
                        if(data.get(k).size()!=0){
                            count++;
                        }
                    }
                    if(count!=0) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        StringBuilder stringBuilder = new StringBuilder();
                        int check_count =0;
                        for (int i = 0; i < mCategoryName.length; i++) {

                            if (data.get(i).size() != 0) {

                                SparseBooleanArray checked = mListView.get(i).getCheckedItemPositions();
                                ArrayList<String> temp_list = new ArrayList<>();
                                count = mListView.get(i).getAdapter().getCount();
                                int sel_count =0;

                                for (int k = 0; k < count; k++) {
                                    if (checked.get(k)) {
                                        temp_list.add(mListView.get(i).getItemAtPosition(k).toString());
                                        sel_count++;
                                        check_count++;
                                    }
                                }

                                if(sel_count!=0) {
                                    stringBuilder.append(mCategoryName[i]);
                                    stringBuilder.append("\n");

                                    for (int j = 0; j < temp_list.size(); j++) {
                                        stringBuilder.append(String.valueOf(j + 1));
                                        stringBuilder.append(") ");
                                        stringBuilder.append(temp_list.get(j));
                                        stringBuilder.append("\n");
                                    }

                                    stringBuilder.append("\n");
                                }
                            }

                        }

                        if(check_count==0){
                            for (int i = 0; i < mCategoryName.length; i++) {

                                if (data.get(i).size() != 0) {




                                        stringBuilder.append(mCategoryName[i]);
                                        stringBuilder.append("\n");

                                        for (int j = 0; j < data.get(i).size(); j++) {
                                            stringBuilder.append(String.valueOf(j + 1));
                                            stringBuilder.append(") ");
                                            stringBuilder.append(data.get(i).get(j));
                                            stringBuilder.append("\n");
                                        }

                                        stringBuilder.append("\n");

                                }

                            }
                        }
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shopping  List");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, stringBuilder.toString());
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                    else{
                        Snackbar.make(v,"Shopping List is Empty",Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),"Shopping List Empty",Toast.LENGTH_SHORT).show();
                    }
                }
            });


            for(int i=0;i<mTextView.size();i++) {
                final int position = i;
                mTextView.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                        performQuery(mCategoryType[position]);
                        }catch (IOException i){
                            Log.e("query", "onClick:position" );
                        }
                    }
                });
            }



            //Testing Overlay

        final Button button = new Button(this);
            button.setText("Hide");
            button.setVisibility(View.GONE);
            ViewTarget target= new ViewTarget(R.id.fab,this);
        ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle("Add New Items Here")
                .setContentText("Click > Add Item > Select Category")
                .setStyle(R.style.CustomShowcaseTheme3)
                .withNewStyleShowcase()
                .hideOnTouchOutside()
                .singleShot(2)
                .replaceEndButton(button)
                .setShowcaseEventListener(new SimpleShowcaseEventListener(){
                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        if(button.getParent()!=null)
                            ((ViewGroup)button.getParent()).removeView(button);
                        ViewTarget target = new ViewTarget(R.id.del,MainActivity.this);
                        new ShowcaseView.Builder(MainActivity.this)
                                .setTarget(target)
                                .setContentTitle("Delete Items")
                                .setContentText("Please Select the Items to be deleted while pressing this")
                                .setStyle(R.style.CustomShowcaseTheme3)
                                .withNewStyleShowcase()
                                .singleShot(3)
                                .hideOnTouchOutside()
                                .replaceEndButton(button)
                                .setShowcaseEventListener(new SimpleShowcaseEventListener(){
                                    @Override
                                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                                        if(button.getParent()!=null)
                                            ((ViewGroup)button.getParent()).removeView(button);
                                        ViewTarget target = new ViewTarget(R.id.share,MainActivity.this);
                                        new ShowcaseView.Builder(MainActivity.this)
                                                .setTarget(target)
                                                .setContentTitle("Share with Friends")
                                                .setContentText("Selected Items are shared or Everything will be shared by default")
                                                .setStyle(R.style.CustomShowcaseTheme3)
                                                .withNewStyleShowcase()
                                                .hideOnTouchOutside()
                                                .singleShot(4)
                                                .replaceEndButton(button)
                                                .build();

                                    }
                                })
                                .build();

                    }
                })
                .build();




    }


    public class MyUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int counter=0;
            TextView emptyTextView=(TextView)findViewById(R.id.emptyText);
            for(int j=0;j<mListView.size();j++) {
                data.set(j, data_bkup.get(j));
                ArrayAdapter new_aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_layout, R.id.txt_title, data.get(j));
                aa.set(j, new_aa);
                mListView.get(j).setAdapter(aa.get(j));
                aa.get(j).notifyDataSetChanged();
                ListUtils.setDynamicHeight(mListView.get(j));
                int mcount = mListView.get(j).getAdapter().getCount();
                if (mcount != 0) {
                    mTextView.get(j).setVisibility(View.VISIBLE);
                    mListView.get(j).setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            listView.setAlpha(1f);
//        }
//        buttonBlocked.setText(R.string.button_show);
        //buttonBlocked.setEnabled(false);
    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {
//        dimView(listView);
//        buttonBlocked.setText(R.string.button_hide);
        //buttonBlocked.setEnabled(true);
    }

    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    EditText editText = (EditText)findViewById(R.id.edittextid);
                    titleBox.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("data_all", json);

//        for (int pos =0; pos <aa.size();pos++){
//            aa.get(pos).notifyDataSetChanged();
//        }
        editor.commit();     // This line is IMPORTANT !!!
    }

    @Override
    protected void onResume(){
        super.onResume();



//        try {
//            if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},2);
//            }
//
//            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString("data_all", null);
        if(json !=null){
            Type type = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
            data = gson.fromJson(json, type);
            for (int pos =0; pos <mListView.size();pos++){
                ArrayAdapter new_aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_layout, R.id.txt_title, data.get(pos));
                aa.set(pos,new_aa);
                mListView.get(pos).setAdapter(aa.get(pos));
                aa.get(pos).notifyDataSetChanged();
                ListUtils.setDynamicHeight(mListView.get(pos));
                int mcount=mListView.get(pos).getAdapter().getCount();
                if ( mcount!= 0) {
                    mTextView.get(pos).setVisibility(View.VISIBLE);
                    mListView.get(pos).setVisibility(View.VISIBLE);
                    TextView emptyTextView = (TextView)findViewById(R.id.emptyText);
                    emptyTextView.setVisibility(View.GONE);
                }
            }
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationInit();
            } else {
                Toast.makeText(getApplicationContext(),"Location permission denied",Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 2) {
            if(grantResults.length == 2 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"Storage permission denied",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void  locationInit() throws SecurityException {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = null;
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }



            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            if(provider != null) {
                location = locationManager.getLastKnownLocation(provider);


                if (location != null) {
                    onLocationChanged(location);
                }

                locationManager.requestLocationUpdates(provider, 20000, 0, this);
            }
            else{
                if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
                }
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }

            }
    }
    /** DO a Query*/

    private void performQuery(String name) throws IOException{



        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius=5000");
        sb.append("&types="+"store");
        sb.append("&name="+name);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyDQOBan1SG77Z8SArDSXk_ckgeMybV_Ypw");


        // Creating a new non-ui thread task to download Google place json data
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());
    }


    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Except downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }


    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Clears all the existing markers
            //mGoogleMap.clear();
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<Double>all_lats = new ArrayList<Double>();
            ArrayList<Double>all_longs = new ArrayList<Double>();

            ArrayList<HashMap<String,String>> new_list = new ArrayList<>(list);

//            for(int i=0;i<list.size();i++){
//
//                // Creating a marker
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                // Getting a place from the places list
//                HashMap<String, String> hmPlace = list.get(i);
//
//                // Getting latitude of the place
//                double lat = Double.parseDouble(hmPlace.get("lat"));
//                all_lats.add(lat);
//
//                // Getting longitude of the place
//                double lng = Double.parseDouble(hmPlace.get("lng"));
//                all_longs.add(lng);
//                // Getting name
//                String name = hmPlace.get("place_name");
//                stringBuilder.append(name);
//
//                // Getting vicinity
//                String vicinity = hmPlace.get("vicinity");
//
//                LatLng latLng = new LatLng(lat, lng);
//
//                // Setting the position for the marker
//                markerOptions.position(latLng);
//
//                // Setting the title for the marker.
//                //This will be displayed on taping the marker
//                markerOptions.title(name + " : " + vicinity);
//
//                // Placing a marker on the touched position
//                //mGoogleMap.addMarker(markerOptions);
//
//            }
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Bundle bundle1 = new Bundle();
//            bundle1.putSerializable("latitude",all_lats);
                bundle1.putSerializable("mylat", mLatitude);
                bundle1.putSerializable("mylong", mLongitude);
                bundle1.putSerializable("all_values", new_list);
//
//            Bundle bundle2 =new Bundle();
//            bundle2.putSerializable("longitude",all_longs);
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("latitude", bundle1);
//            intent.putExtra("longitude",bundle2);
                startActivity(intent);


            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);

            }
        }

    }




    @Override
    public void onLocationChanged (Location location){
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onProviderDisabled (String provider){
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled (String provider){
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged (String provider,int status, Bundle extras){
        // TODO Auto-generated method stub

    }













        public static class ListUtils {
            public static void setDynamicHeight(ListView mListView) {
                ListAdapter mListAdapter = mListView.getAdapter();
                if (mListAdapter == null) {
                    // when adapter is null
                    return;
                }
                int height = 0;
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
                for (int i = 0; i < mListAdapter.getCount(); i++) {
                    View listItem = mListAdapter.getView(i, null, mListView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    height += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = mListView.getLayoutParams();
                params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
                mListView.setLayoutParams(params);
                mListView.requestLayout();
            }
        }


}