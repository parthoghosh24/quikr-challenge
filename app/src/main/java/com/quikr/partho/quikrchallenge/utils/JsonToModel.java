package com.quikr.partho.quikrchallenge.utils;

import android.util.Log;

import com.quikr.partho.quikrchallenge.models.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by partho on 22/8/15.
 */
public class JsonToModel {

    private static final String TAG="json_to_model";

    /**
     * Parsing JSON Array and converting into car list
     *
     * @param list
     * @return
     */
    public static ArrayList<Car> getCars(JSONArray list)
    {
        ArrayList<Car> cars=new ArrayList<>();
        try {

            for(int index=0; index<list.length();++index)
            {
                JSONObject object=list.getJSONObject(index);
                Car car=getCar(object);
                cars.add(car);

            }
            return cars;

        }
        catch (JSONException jse)
        {
            jse.printStackTrace();
            Log.e(TAG,jse.getLocalizedMessage());
            return null;
        }
        catch (Exception e)
        {

            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }

    }

    /**
     * Parsing JSON Object and converting into car
     *
     * @param object
     * @return
     */
    public static Car getCar(JSONObject object)
    {
        Car car=null;
        try {
            car=new Car();
            car.setName(object.getString("name"));
            car.setImage(object.getString("image"));
            car.setPrice(Double.parseDouble(object.getString("price")));
            car.setBrand(object.getString("brand"));
            car.setType(object.getString("type"));
            car.setRating(Double.parseDouble(object.getString("rating")));
            car.setColor(object.getString("color"));
            car.setEngineCC(object.getString("engine_cc"));
            car.setMileage(object.getString("mileage"));
            car.setAbsExist(object.getString("abs_exist"));
            car.setDescription(object.getString("description"));
            car.setLink(object.getString("link"));
            ArrayList<Car.City> cities= new ArrayList<>();
            for(int index=0; index<object.getJSONArray("cities").length();++index)
            {
                JSONObject cityObject=object.getJSONArray("cities").getJSONObject(index);
                Car.City city=new Car.City();
                city.setCity(cityObject.getString("city"));
                city.setUsers(Integer.parseInt(cityObject.getString("users")));
                cities.add(city);

            }
            car.setCities(cities);
            car.setCarJson(object.toString());
            return car;


        }
        catch (JSONException jse)
        {
            jse.printStackTrace();
            Log.e(TAG, jse.getLocalizedMessage());

            return car;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
            return car;
        }

    }
}
