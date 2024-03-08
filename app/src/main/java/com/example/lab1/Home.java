package com.example.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.lab1.Adapter.CityAdapter;
import com.example.lab1.Modal.City;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {

    FirebaseFirestore db;
    CityAdapter adapter;
    RecyclerView recyclerView;

    List<City> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        ghiDulieu();

        docDulieu();

        recyclerView = findViewById(R.id.recyclerViewCities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Hoặc GridLayoutManager nếu bạn muốn hiển thị theo lưới
        recyclerView.setAdapter(adapter); // adapter là đối tượng của CityAdapter đã được khởi tạo và có dữ liệu

        findViewById(R.id.btnlogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, DangNhap.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnadd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    private void ghiDulieu () {
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data2.put("state", "CA");
        data2.put("country", "USA");
        data2.put("capital", false);
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data3.put("state", null);
        data3.put("country", "USA");
        data3.put("capital", true);
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data4.put("state", null);
        data4.put("country", "Japan");
        data4.put("capital", true);
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data5.put("state", null);
        data5.put("country", "China");
        data5.put("capital", true);
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);
    }

    String TAG = "HomeActivity";
    private void docDulieu () {
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<City> cityList= new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                City city = document.toObject(City.class);
                                cityList.add(city);
                            }
                            CityAdapter adapter = new CityAdapter(cityList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void add(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=(this.getLayoutInflater());
        View view=inflater.inflate(R.layout.dialog_add,null);
        builder.setView(view);
        Dialog dialog=builder.create();
        dialog.show();
        //anh xa
        EditText name = view.findViewById(R.id.edtCityName);
        EditText state = view.findViewById(R.id.edtState);
        EditText country = view.findViewById(R.id.edtCountry);
        CheckBox capital = view.findViewById(R.id.edtCapital);
        EditText population = view.findViewById(R.id.edtPopulation);
        EditText regions = view.findViewById(R.id.edtRegions);
        Button save =view.findViewById(R.id.btnsave);
        Button cancle =view.findViewById(R.id.btncancle);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = name.getText().toString();
                String cityState = state.getText().toString();
                String cityCountry = country.getText().toString();
                boolean isCapital = capital.isChecked();
                int cityPopulation = Integer.parseInt(population.getText().toString());
                List<String> cityRegions = Arrays.asList(regions.getText().toString().split(", "));

                City newCity = new City(cityName, cityState, cityCountry, isCapital, cityPopulation, cityRegions);

                db.collection("cities")
                        .add(newCity)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                docDulieu();
                                Toast.makeText(Home.this, "Thêm thành phố thành công!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(Home.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}