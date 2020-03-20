package com.example.prova;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ModelClass modelClass=new ModelClass();
    ModelClass model1,model3;
    ArrayList<ModelClass>arrayList;
    String url="http://prova.pk/PhpScriptForConnectivityandroid/Usersposts.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
       // init();
        displayposts();
    }

    private void displayposts() {

        RequestQueue requestQueue= Volley.newRequestQueue(PostActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("allposts");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String post_data = jsonObject1.getString("post_data");
                        String post_views = jsonObject1.getString("post_views");
                        Toast.makeText(getApplicationContext(),post_data,Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),post_views,Toast.LENGTH_LONG).show();


                        // Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),password,Toast.LENGTH_LONG).show();

                    }


                }


                catch (JSONException e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PostActivity.this,"response is"+error,Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(stringRequest);


    }

    private void init() {
   recyclerView=findViewById(R.id.rec2);
   recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
   recyclerView.setHasFixedSize(true);
   arrayList=new ArrayList<>();
   model1=new ModelClass();
   model3=new ModelClass();
   modelClass.setName("Lion");
   modelClass.setName1("Lion1");
   modelClass.setName2("Lion2");
   modelClass.setImage(R.drawable.fl);

        model1.setImage2(R.drawable.lionimg);
        model1.setImage3(R.drawable.lionimgs);
        model1.setName("Lion");
        model1.setName1("Lion1");
        model3.setName2("Lion2");
        model3.setImage(R.drawable.lion);
        model3.setImage2(R.drawable.fl);
        model3.setImage3(R.drawable.flo);

        arrayList.add(modelClass);
      //  arrayList.add(model1);
        model3.setName("Lion2");

        arrayList.add(model3);

        recyclerView.setAdapter(new MyAdapterClass(getApplicationContext(),arrayList));

    }
}
