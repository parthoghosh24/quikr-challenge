package com.quikr.partho.quikrchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.quikr.partho.quikrchallenge.utils.ui.CustomTextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {

    Animation fadeIn = null;
    CustomTextView car = null;
    private static final String QUIKR_URL="http://quikr.0x10.info/api/cars?type=json&query=list_cars";
    private static final String TAG="qkr_splash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fadeIn= AnimationUtils.loadAnimation(this,R.anim.fab_in);
        car=(CustomTextView)findViewById(R.id.quickr_car_intro);
        car.startAnimation(fadeIn);
        FetchQuikrCarsDetails request = new FetchQuikrCarsDetails();
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        QuikrCarHubApplication.getInstance().getAppRequestQueue().add(request);

    }


    class FetchQuikrCarsDetails extends JsonArrayRequest
    {
        public FetchQuikrCarsDetails()
        {
            super(Method.GET,
                    QUIKR_URL,
                    new JSONObject(),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            Log.e(TAG,"IN HERE..............");
                            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                            intent.putExtra("cars",response.toString());
                            startActivity(intent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG,"here "+error.getLocalizedMessage());
                            if(error.networkResponse!=null)
                            {
                                Log.e(TAG,"from server->"+new String(error.networkResponse.data));
                            }
                            Toast.makeText(getApplicationContext(),"Please try again!",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
