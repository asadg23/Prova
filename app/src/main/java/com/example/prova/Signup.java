package com.example.prova;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Signup extends AppCompatActivity {

    TextInputEditText txtfname, txtlastname, txtemail, txttypepassword, txtconfrimpassword;
    Button btnsignup;
    String url = "http://prova.pk/PhpScriptForConnectivityandroid/Signupscript.php";
    String emailverificationurl="http://prova.pk/PhpScriptForConnectivityandroid/phpverification.php";
    ProgressDialog progressDialog;
    Random rn=new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {
        progressDialog=new ProgressDialog(Signup.this);
        txtfname = findViewById(R.id.txtsignname);
        txtlastname = findViewById(R.id.txtsignlastname);
        txtemail = findViewById(R.id.txtsignEmail);
        txttypepassword = findViewById(R.id.txttypepassword);
        txtconfrimpassword = findViewById(R.id.txtsigncpassword);
        btnsignup = findViewById(R.id.txtsignbutton);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!TextUtils.isEmpty(txtfname.getText().toString())&&!TextUtils.isEmpty(txtlastname.getText().toString())&&!TextUtils.isEmpty(txtemail.getText().toString())&&!TextUtils.isEmpty(txttypepassword.getText().toString())&&!TextUtils.isEmpty(txtconfrimpassword.getText().toString()))
               {
                  // int num=rn.nextInt(1000000);
                   insertdata();
               }
               else {
                //   int num=rn.nextInt(1000000);

                   Toast.makeText(getApplicationContext(),"Please fill required fields",Toast.LENGTH_LONG).show();

               }


            }
        });


    }

    private void sendemail() {

    StringRequest stringRequest=new StringRequest(Request.Method.POST, emailverificationurl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (response.equalsIgnoreCase("successful"))
            {
                Toast.makeText(getApplicationContext(),"Email has been sent kindly check ",Toast.LENGTH_LONG).show();

            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),error.getMessage()  ,Toast.LENGTH_LONG).show();


        }
    })
    {

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String>map=new HashMap<String, String>();
            map.put("email",txtemail.getText().toString());

            return map;




        }
    };
    RequestQueue requestQueue=Volley.newRequestQueue(Signup.this);
    requestQueue.add(stringRequest);

    }

    private void insertdata() {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("datainserted")) {
                    Toast.makeText(getApplicationContext(), "Date Inserted Now", Toast.LENGTH_LONG).show();
                    sendemail();
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                } else {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response , Toast.LENGTH_LONG).show();

                }

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

                try {
                    int num=rn.nextInt(1000000);
                    Map<String,String>map=new HashMap<String, String>();
                    map.put("fname",txtfname.getText().toString());
                    map.put("lname",txtlastname.getText().toString());
                    map.put("email",txtemail.getText().toString());
                    map.put("ver_key",String.valueOf(num));

                    map.put("password",md5(txtconfrimpassword.getText().toString()).trim());
                    return map;

                }
                catch (NoSuchAlgorithmException e)
                {
                    Toast.makeText(Signup.this,e.getMessage(),Toast.LENGTH_LONG).show();

                }
                return super.getParams();
                 }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(Signup.this);
        requestQueue.add(stringRequest);

    }
    private static String md5(String data)
            throws NoSuchAlgorithmException {
        // Get the algorithm:
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // Calculate Message Digest as bytes:
        byte[] digest = md5.digest(data.getBytes(UTF_8));
        // Convert to 32-char long String:
        return String.format("%032x%n", new BigInteger(1, digest));
    }
}