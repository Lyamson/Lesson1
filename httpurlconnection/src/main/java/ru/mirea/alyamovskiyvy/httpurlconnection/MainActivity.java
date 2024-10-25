package ru.mirea.alyamovskiyvy.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.alyamovskiyvy.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v -> {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = null;
            if (connectivityManager != null) {
                networkinfo = connectivityManager.getActiveNetworkInfo();
            }
            if (networkinfo != null && networkinfo.isConnected()) {
                new DownloadPageTask().execute("https://ipinfo.io/json"); // запуск нового потока
            } else {
                Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return getRequest(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(TAG, "Response: " + responseJson);
                /*
                {
                  "ip": "91.134.94.62",
                  "hostname": "ns3234188.ip-91-134-94.eu",
                  "city": "Roubaix",
                  "region": "Hauts-de-France",
                  "country": "FR",
                  "loc": "50.6942,3.1746",
                  "org": "AS16276 OVH SAS",
                  "postal": "59051 CEDEX 1",
                  "timezone": "Europe/Paris",
                  "readme": "https://ipinfo.io/missingauth"
                }
                 */
                String ip = responseJson.getString("ip");

                String city = responseJson.getString("city");
                String region = responseJson.getString("region");
                String country = responseJson.getString("country");
                String org = responseJson.getString("org");
                String postal = responseJson.getString("postal");
                String timezone = responseJson.getString("timezone");

                binding.cityTextView.setText(city);
                binding.regionTextView.setText(region);
                binding.countryTextView.setText(country);
                binding.orgTextView.setText(org);
                binding.postalTextView.setText(postal);
                binding.timezoneTextView.setText(timezone);

                String loc = responseJson.getString("loc");
                String latitude = loc.split(",")[0];
                String longitude = loc.split(",")[1];

                new GetWeatherTask().execute("https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                        "&longitude=" + longitude + "&current_weather=true");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
    private class GetWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return getRequest(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(TAG, "Response: " + responseJson);
                JSONObject currentWeatherJson = responseJson.getJSONObject("current_weather");
                double temp = currentWeatherJson.getDouble("temperature");
                double wind = currentWeatherJson.getDouble("windspeed");

                wind = wind / 3.6;

                binding.tempTextView.setText(String.format("%.1f °C", temp));
                binding.windTextView.setText(String.format("%.2f м/с", wind));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
    private String getRequest(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read); }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage()+". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}