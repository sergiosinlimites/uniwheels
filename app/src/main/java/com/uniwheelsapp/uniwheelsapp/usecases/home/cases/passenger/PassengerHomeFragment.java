package com.uniwheelsapp.uniwheelsapp.usecases.home.cases.passenger;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.uniwheelsapp.uniwheelsapp.PlanTripsActivity;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.usecases.onboarding.EntranceActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerHomeFragment extends Fragment {
    private View root;
    private PassengerHomeViewModel viewModel;
    private Button logout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerHomeFragment newInstance(String param1, String param2) {
        PassengerHomeFragment fragment = new PassengerHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PassengerHomeViewModel.class);
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_passenger_home, container, false);

        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });

        return root;
    }

    public void onGoNow(View view){
        Intent i = new Intent(getContext(), PlanTripsActivity.class);
        startActivity(i);
    }

    public void onPlanRide(View view){
        try {
            System.out.println("entra");
            Intent i = new Intent(getContext(), PlanTripsActivity.class);
            startActivity(i);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void SignOut(){
//        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                finish();
//                startActivity(new Intent(getApplicationContext(), EntranceActivity.class));
//            }
//        });
    }
}