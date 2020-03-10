package mobile.thomasianJourney.main.home;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import mobile.thomasianJourney.main.interfaces.AsyncResponse;
import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EventStreamAsync extends AsyncTask<String, Void, String> {
    private AsyncResponse asyncResponse;

    public EventStreamAsync(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            RequestBody requestBody =
                    new MultipartBody.Builder()
                            .addFormDataPart(IntentExtrasAddresses.INTENT_EXTRA_MOBILE_NUMBER, strings[0])
                            .setType(MultipartBody.FORM).build();

            Request.Builder builder = new Request.Builder();
            builder.url(strings[0]).post(requestBody);
            Request request = builder.build();

            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS)).build();

            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        asyncResponse.doWhenFinished(s);
    }
}
