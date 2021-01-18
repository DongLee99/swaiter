package com.example.swaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static java.sql.DriverManager.println;

public class LoginActivity extends AppCompatActivity {
    EditText editText_id;
    EditText editText_pass;
    TextView textView;
    info info = new info();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        textView = (TextView) findViewById(R.id.textView);

        Button button = (Button) findViewById(R.id.button_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });

    }

    public void request() {
        JSONObject testjson = new JSONObject();

        editText_id = (EditText) findViewById (R.id.editText_ID);
        editText_pass = (EditText) findViewById (R.id.editText_PWD);

        try {
            testjson.put("email", editText_id.getText().toString());
            testjson.put("password", editText_pass.getText().toString());
            String jsonString = testjson.toString();

            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, info.getUrl() + "auth/login2", testjson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        println("데이터 전송 성공");
                        JSONObject jsonObject = new JSONObject(response.toString());

                        String resultID = jsonObject.getString("approve_id");
                        String resultPassword = jsonObject.getString("approve_pw");

                        if (resultID.equals("OK") & resultPassword.equals("OK")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,"로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
