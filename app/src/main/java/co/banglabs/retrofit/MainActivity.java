package co.banglabs.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import co.banglabs.retrofit.network.ApiInterface;
import co.banglabs.retrofit.network.RetrofitApiClient;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView ipAddressTextView;
    private TextView cityTextView;
    private TextView countryTextView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddressTextView = findViewById(R.id.ip_address_textView);
        cityTextView = findViewById(R.id.city_textView);
        countryTextView = findViewById(R.id.country_textView);
        progressBar = findViewById(R.id.progressBar);
    }

    public void showMyIp(View view) {

        progressBar.setVisibility(View.VISIBLE); //network call will start. So, show progress bar

        ApiInterface apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);

        Call call = apiInterface.getMyIp();
        call.equals(new Callback<ServerResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ServerResponse> call, Response<ServerResponse> response) {
                progressBar.setVisibility(View.GONE); //network call success. So hide progress bar

                ServerResponse serverResponse = response.body();

                if (response.code() == 200 && serverResponse != null) { //response code 200 means server call successful
                    //data found. So place the data into TextView
                    ipAddressTextView.setText(serverResponse.getIp());
                    cityTextView.setText(serverResponse.getCity());
                    countryTextView.setText(serverResponse.getCountry());
                } else {
                    //somehow data not found. So error message showing in first TextView
                    ipAddressTextView.setText(response.message());
                    cityTextView.setText("");
                    countryTextView.setText("");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ServerResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE); //network call failed. So hide progress bar

                //network call failed due to disconnect internet connection or server error
                ipAddressTextView.setText(t.getMessage());
                cityTextView.setText("");
                countryTextView.setText("");
            }
        });
    }
}





