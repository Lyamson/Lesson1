package ru.mirea.alyamovskiyvy.mireaproject.ui.httpinfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.alyamovskiyvy.mireaproject.R;
import ru.mirea.alyamovskiyvy.mireaproject.databinding.FragmentHttpInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link httpInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class httpInfoFragment extends Fragment {
    public httpInfoFragment() {
        // Required empty public constructor
    }

    public static httpInfoFragment newInstance() {
        httpInfoFragment fragment = new httpInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static final String TAG = httpInfoFragment.class.getSimpleName();
    private FragmentHttpInfoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHttpInfoBinding.inflate(inflater, container, false);

        binding.requestButton.setOnClickListener(this::sendRequest);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void sendRequest(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.sampleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        Call<List<Coffee>> call = api.getCoffee();

        Log.d(TAG, call.toString());

        call.enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(Call<List<Coffee>> call, Response<List<Coffee>> response) {
                if (response.isSuccessful()) {
                    List<Coffee> coffee = response.body();
                    Log.d(TAG, "RESPONSE TEXT: " + coffee);
                    binding.responseTextView.setText(coffee.get(0).title + "\n" + coffee.get(0).description);
                } else {
                    Log.d(TAG, "ERROR in response");
                }
            }

            @Override
            public void onFailure(Call<List<Coffee>> call, Throwable t) {
                Log.e(TAG, "FAILURE in response");
            }
        });
    }
}