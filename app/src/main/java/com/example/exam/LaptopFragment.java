package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Adapters.ProductAdapter;
import com.example.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LaptopFragment extends Fragment {

    private static final String JSON_URL = "http://10.0.2.2/ElectronicStore/GetLaptops.php" ;
    ListView listView;
    List<Product> LaptopList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.fragment_laptop, null);
    }
    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);


        listView = (ListView) getView().findViewById(R.id.listview3);
        LaptopList = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product selectedItem = (Product) listView.getItemAtPosition(position);

                String url = "http://10.0.2.2/ElectronicStore/uploads/";
                if(selectedItem != null){

                    Intent intent = new Intent(getContext(),show_item.class);
                    intent.putExtra("Name", selectedItem.getName());
                    intent.putExtra("Price", selectedItem.getPrice());
                    intent.putExtra("Description", selectedItem.getDescription());
                    intent.putExtra("Image", url+selectedItem.getImgPath());




                    startActivity(intent);
                }else{

                    Toast.makeText(getContext(),"no Item selected",Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadComputerList();

    }

    private void loadComputerList() {
        //getting the progressbar
        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progressBar3);

        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.INVISIBLE);


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray productArray = obj.getJSONArray("dishs");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < productArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject productObject = productArray.getJSONObject(i);


                                //creating a product object and giving them the values from json object
                                Product product = new Product(productObject.getString("Name"),
                                        productObject.getString("Price"),
                                        productObject.getString("Description"),
                                        productObject.getString("PicPath"));

                                //adding the product to productlist
                                LaptopList.add(product);
                            }

                            //creating custom adapter object
                            ProductAdapter adapter = new ProductAdapter(LaptopList, getContext());

                            //adding the adapter to listview
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

}


