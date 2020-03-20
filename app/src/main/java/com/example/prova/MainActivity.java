package com.example.prova;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MainActivity extends AppCompatActivity {

    String url="http://prova.pk/PhpScriptForConnectivityandroid/asadphpscript.php";
    // String url="https://cpanel.prova.pk/cpsess2489149852/frontend/paper_lantern/filemanager/index.html";
    String emailverificationurl="http://prova.pk/PhpScriptForConnectivityandroid/phpverification.php";

    TextInputEditText txt,txtpassword;
    Button btnlogin;
    ArrayList<String>arrayListemailandpassword;
    TextView txtregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(MainActivity.this,"Main screen",Toast.LENGTH_LONG).show();
      //  startActivity(new Intent(MainActivity.this,Signup.class));
        txtregister=findViewById(R.id.txtregisterhere);
        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Signup.class));

            }
        });

        txt=findViewById(R.id.txtemail);
        txtpassword=findViewById(R.id.txtpassword);
        btnlogin=findViewById(R.id.loginin);
        arrayListemailandpassword=new ArrayList<>();
        listners();

        }
        private void userlogin ( final String mypassword, final String emailu)
        {
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String getsms = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        HashMap hashMap=new HashMap();


                        if (getsms.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                       JSONObject         jsonObject1     = jsonArray.getJSONObject(i);
                                String email = jsonObject1.getString("email");
                                String password = jsonObject1.getString("password");

                                arrayListemailandpassword.add(email);
                                arrayListemailandpassword.add(password);

                                if (email.equals(emailu.trim())&&password.equals(mypassword.trim()))
                                {
                                    String verkey=jsonObject1.getString("ver_key");
                                    String isem = jsonObject1.getString("is_email_verified");
                                    hashMap.put("verkey",verkey);
                                    hashMap.put("isemailverified",isem);

                         //   Toast.makeText(getApplicationContext(),"veris"+isem+"verkey"+verkey,Toast.LENGTH_LONG).show();
                                }
                               // arrayListemailandpassword.add(isemailverfied);

                            }
                            if (arrayListemailandpassword.contains(mypassword.trim()) && arrayListemailandpassword.contains(emailu.trim()) ) {
                      //          Toast.makeText(getApplicationContext(),"1st key"+hashMap.get("verkey").toString(),Toast.LENGTH_LONG).show();
                       //         Toast.makeText(getApplicationContext(),"2nd key"+hashMap.get("isemailverified").toString(),Toast.LENGTH_LONG).show();


                        if (hashMap.get("isemailverified").equals("0"))
                        {

                            sendemail(emailu.trim(),mypassword.trim(),hashMap.get("verkey").toString());
                        }
                        else {
                           Intent intent=new Intent(getApplicationContext(),WelcomeScreen.class);
                            intent.putExtra("Email",emailu.trim());
                           startActivity(intent);
                            finish();


                        }




                            } else {
                                Toast.makeText(getApplicationContext(), "User not exists", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_LONG).show();

                        }


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();


                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "response is" + error, Toast.LENGTH_LONG).show();

                }
            });
            requestQueue.add(stringRequest);


        }
        private void listners () {

            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (TextUtils.isEmpty(txtpassword.getText().toString())&&TextUtils.isEmpty(txt.getText().toString()))
                        {
                            Toast.makeText(getApplicationContext(),"Please fill required fields",Toast.LENGTH_LONG).show();
                        }
                        else {
                            String textpassword = md5(txtpassword.getText().toString().trim());
                            userlogin(textpassword, txt.getText().toString());

                        }
                          } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                  // sendemail();


                }
            });


        }
        private static String md5 (String data)
            throws NoSuchAlgorithmException {
            // Get the algorithm:
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // Calculate Message Digest as bytes:
            byte[] digest = md5.digest(data.getBytes(UTF_8));
            // Convert to 32-char long String:
            return String.format("%032x%n", new BigInteger(1, digest));
        }

    private void sendemail(final String email, final String password,final String verficationkey) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, emailverificationurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Verification code has been sent to your email kindly check spam folder",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),ConfrimationEmailCode.class);
                intent.putExtra("Email",email);
                intent.putExtra("Password",password);
                intent.putExtra("verification",verficationkey);
                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error is"+error.getMessage()  ,Toast.LENGTH_LONG).show();


            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>map=new HashMap<String, String>();
                map.put("email",txt.getText().toString());

                return map;




            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }



}
