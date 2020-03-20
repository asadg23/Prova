package com.example.prova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ConfrimationEmailCode extends AppCompatActivity {

    TextInputEditText txtconfrimcode;
    Button btnverify,logout;
    Intent intent;
String email;
String url="http://prova.pk/PhpScriptForConnectivityandroid/updateisverfied.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrimation_email_code);
    txtconfrimcode=findViewById(R.id.confirmationcode);
    btnverify=findViewById(R.id.verfyclick);
    logout=findViewById(R.id.logoutclick);
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    });
    intent=getIntent();
   final String key=intent.getStringExtra("verification");
   email=intent.getStringExtra("Email");
     //   Toast.makeText(getApplicationContext(),"key is"+key,Toast.LENGTH_LONG).show();

        btnverify.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (txtconfrimcode.getText().toString().equals(key.trim()))
            {
                Toast.makeText(getApplicationContext(),"Verified",Toast.LENGTH_LONG).show();
              //  Toast.makeText(getApplicationContext(),"Verified"+email,Toast.LENGTH_LONG).show();

                updatestatus(email.trim());

            }
            else {

                Toast.makeText(getApplicationContext(),"Code Not Match"+key,Toast.LENGTH_LONG).show();


            }
        }
    });


    }

    private void updatestatus(final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent=new Intent(getApplicationContext(),WelcomeScreen.class);
                intent.putExtra("Email",email.trim());
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>map=new HashMap<String, String>();
                map.put("email",email);
                return map;

            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(ConfrimationEmailCode.this);
        requestQueue.add(stringRequest);


    }
}
