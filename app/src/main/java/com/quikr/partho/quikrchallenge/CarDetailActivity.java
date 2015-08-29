package com.quikr.partho.quikrchallenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.quikr.partho.quikrchallenge.models.Car;
import com.quikr.partho.quikrchallenge.utils.JsonToModel;
import com.quikr.partho.quikrchallenge.utils.ui.BitmapLruCache;
import com.quikr.partho.quikrchallenge.utils.ui.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CarDetailActivity extends AppCompatActivity {

    private JSONObject carJson;
    private Car car;
    private Toolbar toolbar;
    private static final String TAG="qukr_car_detail";
    private CardView carColor;
    private TextView rating, price, cc,abs,type,mileage,description;
    private ImageLoader imageLoader;
    private NetworkImageView carImage;
    private PieChart pie;
    private ArrayList<Float> yData;
    private ArrayList<String> xData;
    private CustomTextView share,link,message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        initWidgets();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        try {
            carJson=new JSONObject(getIntent().getStringExtra("car"));
            car= JsonToModel.getCar(carJson);
            getSupportActionBar().setTitle(car.getName());
            getSupportActionBar().setSubtitle(car.getBrand());
            carColor.setCardBackgroundColor(Color.parseColor(car.getColor()));
            rating.setText(car.getRating() + "");
            price.setText(car.getPrice() + " L");
            abs.setText(car.getAbsExist());
            cc.setText(car.getEngineCC());
            type.setText(car.getType());
            mileage.setText(car.getMileage());
            carImage.setImageUrl(car.getImage(), imageLoader);
            description.setText(car.getDescription());
            if(car.getCities().size()>0)
            {
                for(Car.City city: car.getCities())
                {
                    yData.add(city.getUsers()/1.0f);
                    xData.add(city.getCity());
                }
            }
            pie.setDrawSliceText(true);
            pie.setUsePercentValues(true);
            pie.setDrawHoleEnabled(true);
            pie.setHoleColorTransparent(true);
            pie.setHoleRadius(7);
            pie.setTransparentCircleRadius(10);
            pie.setRotationAngle(0);
            pie.setRotationEnabled(true);
            addData();
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, car.getLink());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(car.getLink()));
                    startActivity(browserIntent);
                }
            });
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater li = LayoutInflater.from(getApplicationContext());
                    View promptsView = li.inflate(R.layout.message_prompt, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());

                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.quikr_number_input);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("SEND",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(userInput.getText().toString(), null, car.getLink(), null, null);
                                        }
                                    })
                            .setNegativeButton("CANCEL",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            });


        }
        catch (JSONException jse)
        {
            Log.e(TAG, jse.getLocalizedMessage());
        }


    }

    private void addData()
    {
        ArrayList<Entry> yVals= new ArrayList<>();
        for(int index=0;index<yData.size();++index)
        {
            yVals.add(new Entry(yData.get(index),index));
        }

        PieDataSet dataSet=new PieDataSet(yVals,"Market Share");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.JOYFUL_COLORS)
        {
            colors.add(color);
        }
        dataSet.setColors(colors);
        PieData data = new PieData(xData,dataSet);
        data.setValueFormatter(new PercentFormatter());
        pie.setData(data);
        pie.highlightValues(null);
        pie.invalidate();
    }

    private void initWidgets()
    {
        toolbar=(Toolbar)findViewById(R.id.quikr_toolbar);
        carColor=(CardView)findViewById(R.id.quikr_car_color);
        rating=(TextView)findViewById(R.id.quikr_car_rating);
        price=(TextView)findViewById(R.id.quikr_car_price);
        abs=(TextView)findViewById(R.id.quikr_car_abs);
        cc=(TextView)findViewById(R.id.quikr_car_cc);
        type=(TextView)findViewById(R.id.quikr_car_type);
        mileage=(TextView)findViewById(R.id.quikr_car_mileage);
        carImage=(NetworkImageView)findViewById(R.id.quikr_car_image);
        imageLoader=new ImageLoader(QuikrCarHubApplication.getInstance().getAppRequestQueue(),new BitmapLruCache());
        description=(TextView)findViewById(R.id.quikr_car_description);
        pie=(PieChart)findViewById(R.id.quikr_pie);
        yData=new ArrayList<>();
        xData=new ArrayList<>();
        share=(CustomTextView)findViewById(R.id.quikr_car_share);
        link=(CustomTextView)findViewById(R.id.quikr_car_link);
        message=(CustomTextView)findViewById(R.id.quikr_car_sms);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem settings = menu.getItem(0);
        settings.setVisible(false);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
