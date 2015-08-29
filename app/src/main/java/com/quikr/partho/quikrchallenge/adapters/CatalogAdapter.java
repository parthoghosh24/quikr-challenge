package com.quikr.partho.quikrchallenge.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.quikr.partho.quikrchallenge.CarDetailActivity;
import com.quikr.partho.quikrchallenge.QuikrCarHubApplication;
import com.quikr.partho.quikrchallenge.R;
import com.quikr.partho.quikrchallenge.models.Car;
import com.quikr.partho.quikrchallenge.utils.ui.BitmapLruCache;

import java.util.List;

/**
 * Created by partho on 22/8/15.
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogItemViewHolder>{

    private Activity mContext;
    private List<Car> cars;
    private ImageLoader imageLoader;
    private static final String TAG="qkr_cat_adptr";

    public CatalogAdapter(Activity mContext, List<Car> cars)
    {
        this.mContext=mContext;
        this.cars=cars;
        imageLoader=new ImageLoader(QuikrCarHubApplication.getInstance().getAppRequestQueue(),new BitmapLruCache());
    }

    @Override
    public CatalogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_catalog_item,parent,false);
        return new CatalogItemViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(CatalogItemViewHolder holder, final int position) {
            holder.carImage.setImageUrl(cars.get(position).getImage(),imageLoader);
            holder.carName.setText(cars.get(position).getName());
            holder.carRating.setText(cars.get(position).getRating() + "");
            holder.carPrice.setText(cars.get(position).getPrice()+" L");
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CarDetailActivity.class);
                    intent.putExtra("car",cars.get(position).getCarJson());
                    mContext.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public static class CatalogItemViewHolder extends RecyclerView.ViewHolder
    {
        public NetworkImageView carImage;
        public TextView carName, carRating, carPrice;
        public View view;

        public CatalogItemViewHolder(View itemView)
        {
            super(itemView);
            carImage=(NetworkImageView)itemView.findViewById(R.id.quikr_catalog_image);
            carRating=(TextView)itemView.findViewById(R.id.quikr_catalog_rating);
            carName=(TextView)itemView.findViewById(R.id.quikr_catalog_title);
            carPrice=(TextView)itemView.findViewById(R.id.quikr_catalog_price);
            view=itemView;
        }


    }
}
