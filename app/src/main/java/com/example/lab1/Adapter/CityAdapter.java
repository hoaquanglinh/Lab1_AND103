package com.example.lab1.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.Modal.City;
import com.example.lab1.R;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<City> cityList;

    public CityAdapter(List<City> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.txtCityName.setText(city.getName());
        holder.txtState.setText(city.getState());
        holder.txtCountry.setText(city.getCountry());
        holder.txtCapital.setChecked(city.isCapital());
        holder.txtPopulation.setText(String.valueOf(city.getPopulation()));
        holder.txtRegions.setText(TextUtils.join(", ", city.getRegions()));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCityName;
        TextView txtState;
        TextView txtCountry;
        CheckBox txtCapital;
        TextView txtPopulation;
        TextView txtRegions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCityName = itemView.findViewById(R.id.txtCityName);
            txtState = itemView.findViewById(R.id.txtState);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            txtCapital = itemView.findViewById(R.id.txtCapital);
            txtPopulation = itemView.findViewById(R.id.txtPopulation);
            txtRegions = itemView.findViewById(R.id.txtRegions);
        }
    }

}
