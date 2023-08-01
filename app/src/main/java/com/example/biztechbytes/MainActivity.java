package com.example.biztechbytes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnToggleViewVisibilityListener {

    List<SliderItems> sliderItems = new ArrayList<>();

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> newslinks = new ArrayList<>();
    ArrayList<String> heads = new ArrayList<>();

    DatabaseReference mRef;
    VerticalViewPager verticalViewPager;

    FragmentContainerView fragmentContainerView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verticalViewPager = findViewById(R.id.verticalViewPager);

        mRef = FirebaseDatabase.getInstance().getReference("News");

        fragmentContainerView = findViewById(R.id.fragmentContainerView);



        loadFragment();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren())
                {
                    titles.add(ds.child("title").getValue(String.class));
                    desc.add(ds.child("desc").getValue(String.class));
                    images.add(ds.child("imagelink").getValue(String.class));
                    newslinks.add(ds.child("newslink").getValue(String.class));
                    heads.add(ds.child("head").getValue(String.class));
                }
                for(int i=0;i<images.size();i++)
                //for(int i=0;i<images.size();i++)
                {
                    sliderItems.add(new SliderItems(images.get(i)));


                }
                verticalViewPager.setAdapter(new ViewPagerAdapter(MainActivity.this, sliderItems, titles, desc, newslinks, heads, verticalViewPager));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void loadFragment(){
        Fragment fragment = LoginFragment.newInstance();
        ((LoginFragment) fragment).setOnToggleViewVisibilityListener(MainActivity.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
    }




    @Override
    public void onToggleViewVisibility(boolean isVisible) {
        if (isVisible) {
            verticalViewPager.setVisibility(View.VISIBLE);
            fragmentContainerView.setVisibility(View.GONE);

        } else {
//            verticalViewPager.setVisibility(View.GONE);
//            fragmentContainerView.setVisibility(View.VISIBLE);
        }
    }
}