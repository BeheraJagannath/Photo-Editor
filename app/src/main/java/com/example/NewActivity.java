package com.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.neweditor.R;
import com.example.neweditor.databinding.ActivityNewBinding;
import com.example.neweditor.newa.ItemClickListener;
import com.example.neweditor.newa.NewAdapter;
import com.example.neweditor.newa.User;

import java.util.ArrayList;

public class NewActivity extends AppCompatActivity implements ItemClickListener {
    private ActivityNewBinding binding ;
    private ArrayList<User> lngList;
    private NewAdapter lngRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this ,R.layout.activity_new);

        lngList = new ArrayList<User>();
        lngList.add(new User("dfdfdf", R.drawable.ic_crop));
        lngList.add(new User("fdsfsadf", R.drawable.ic_rotate_vector));
        lngList.add(new User("fdsafg", R.drawable.ic_edit_vector));
        lngRVAdapter = new NewAdapter(lngList,this,this);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(lngRVAdapter);
    }

    @Override
    public void onClick(View view, int position) {
        
        if (position == 0){
            Toast.makeText(this, "firstFragment", Toast.LENGTH_SHORT).show();
        }
        if (position ==1 ){
            Toast.makeText(this, "secondFragment", Toast.LENGTH_SHORT).show();
        }
        if (position ==2 ){
            Toast.makeText(this, "thirdFragment", Toast.LENGTH_SHORT).show();
        }

    }
}