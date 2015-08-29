package com.quikr.partho.quikrchallenge;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quikr.partho.quikrchallenge.adapters.CatalogAdapter;
import com.quikr.partho.quikrchallenge.adapters.HomeMenuAdapter;
import com.quikr.partho.quikrchallenge.models.Car;
import com.quikr.partho.quikrchallenge.utils.JsonToModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle menuToggle;
    private String catalog;
    private static final String QUIKR_URL="http://quikr.0x10.info/api/cars?type=json&query=list_cars";
    private static final String QUIKR_API_COUNT="https://quikr.0x10.info/api/cars?type=json&query=api_hits";
    private static final String TAG="qukr_home";
    private JSONArray carsCatalog;
    private ArrayList<Car> carModels;
    private RecyclerView carsCatalogView;
    private LinearLayoutManager carsCatalogLayoutManager;
    private CatalogAdapter catalogAdapter;
    private TextView carsCount, apiCount;
    private Toolbar toolbar;
    private HomeMenuAdapter homeMenuAdapter;
    private List<HashMap<String,String>> menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initWidget();
        menuToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };

        mDrawerLayout.setDrawerListener(menuToggle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        menuToggle.syncState();

    }

    private void initWidget()
    {
        toolbar=(Toolbar)findViewById(R.id.quikr_toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.quikr_drawer);
        mDrawerList=(ListView)findViewById(R.id.quikr_home_nav_menu);
        carsCatalogView=(RecyclerView)findViewById(R.id.quikr_cars_catalog);
        apiCount=(TextView)findViewById(R.id.quickr_api_count);
        carsCount=(TextView)findViewById(R.id.quickr_car_count);
        carsCatalogLayoutManager=new LinearLayoutManager(this);
        carsCatalogLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        carsCatalogView.setLayoutManager(carsCatalogLayoutManager);
        menus=new ArrayList<>();
        HashMap<String, String> homeMap = new HashMap<>();
        homeMap.put("icon", getResources().getString(R.string.fa_home));
        homeMap.put("title","Home");
        menus.add(homeMap);
        HashMap<String, String> portfolioMap = new HashMap<>();
        portfolioMap.put("icon", getResources().getString(R.string.fa_portfolio));
        portfolioMap.put("title","Portfolio");
        menus.add(portfolioMap);
        homeMenuAdapter=new HomeMenuAdapter(this,menus);
        mDrawerList.setAdapter(homeMenuAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 1)//portfolio
                {
                    Intent intent = new Intent(HomeActivity.this, PortfolioActivity.class);
                    startActivity(intent);
                } else {
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        catalog=getIntent().getStringExtra("cars");
        if(TextUtils.isEmpty(catalog))
        {
            FetchQuikrCarsDetails request = new FetchQuikrCarsDetails();
            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            QuikrCarHubApplication.getInstance().getAppRequestQueue().add(request);
        }
        else
        {
            try{
                carsCatalog =new JSONArray(catalog);
                carsCount.setText(carsCatalog.length()+"");
                carModels= JsonToModel.getCars(carsCatalog);
                catalogAdapter = new CatalogAdapter(this,carModels);
                carsCatalogView.setAdapter(catalogAdapter);
            }
            catch (JSONException jse)
            {
                Log.e(TAG,jse.getLocalizedMessage());
            }
        }

        FetchQuikrAPICount count = new FetchQuikrAPICount();
        count.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        QuikrCarHubApplication.getInstance().getAppRequestQueue().add(count);




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        menuToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        menuToggle.onConfigurationChanged(newConfig);
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
                if(mDrawerLayout.isDrawerOpen(mDrawerList))
                {
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                else
                {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                return true;
        }


        return super.onOptionsItemSelected(item);
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

                            Log.e(TAG, "IN HERE..............");
                            carsCatalog =response;
                            carsCount.setText(response.length()+"");


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

    class FetchQuikrAPICount extends JsonObjectRequest
    {
        public FetchQuikrAPICount()
        {
            super(Method.GET,
                    QUIKR_API_COUNT,
                    new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e(TAG, "IN HERE..............");
                            try {
                                int count = Integer.parseInt(response.getString("api_hits"));
                                apiCount.setText(count+"");
                            }
                            catch (JSONException jse)
                            {
                                Log.e(TAG,jse.getLocalizedMessage());
                            }

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
