package com.example.swaiter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity implements MenuAdapter.OnItemClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

    info info = new info();

    TextView textView_result;
    String title, desc, imgurl, result, price;

    RecyclerView recycler_menu;
    MenuAdapter mMenuAdapter;
    Context mContext;

    String option_drink = "";
    String option_side = "";

    ArrayList<MenuVO> menu_list = new ArrayList<>();

    FloatingActionButton fab;

    Dialog dialog, progressDialog;
    Button dialog_button, button_option;

    RadioButton radioButton_set, radioButton_single;
    LinearLayout layout_sidemenu, layout_drink;
    RadioGroup radioGroup_set, radioGroup_side, radioGroup_drink;

    DBManager dbManager;

    TextView textView_option, textView_totalPrice;
    String price_drink= "0", price_side= "0";
    int price_total=0, price_burger=0;

    Boolean isSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setOwnerActivity(ResultActivity.this);
        dialog.setCanceledOnTouchOutside(true);
        radioGroup_set = (RadioGroup) dialog.findViewById(R.id.radiogroup_set);
        radioGroup_side = (RadioGroup) dialog.findViewById(R.id.radiogroup_side);
        radioGroup_drink = (RadioGroup) dialog.findViewById(R.id.radiogroup_drink);
        layout_drink = (LinearLayout) dialog.findViewById(R.id.layout_drink);
        layout_sidemenu = (LinearLayout) dialog.findViewById(R.id.layout_sidemenu);
        dialog_button = (Button) dialog.findViewById(R.id.button_dialog);
        textView_option = (TextView) dialog.findViewById(R.id.textView_option);
        textView_totalPrice = (TextView) dialog.findViewById(R.id.button_totalPrice);
        button_option = (Button) dialog.findViewById(R.id.button_option);

        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dbManager = DBManager.getInstance(this);

        mContext = this;

        Intent result_intent = getIntent();
        result = result_intent.getExtras().getString("text");
        textView_result = (TextView) findViewById(R.id.txt_result);
        textView_result.setText("\"" + result + "\" 검색결과");

        recycler_menu = (RecyclerView) findViewById(R.id.rcc_menu);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        recycler_menu.setLayoutManager(mLayoutManager);
        recycler_menu.addItemDecoration(new ItemDecoration(this));

        request();

        mMenuAdapter = new MenuAdapter(mContext, menu_list);
        mMenuAdapter.setOnItemClickListener(this);
        recycler_menu.setAdapter(mMenuAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_shopping = new Intent(ResultActivity.this, selectedActivity.class);
                startActivity(intent_shopping);
            }
        });
    }

    private void request() {

        progressON(ResultActivity.this, "");
        JSONObject send = new JSONObject();
        try {
            send.put("result", result);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, info.getUrl() + "Post/imgSel", send, this, this);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("Content1");

            for (int i = 0; i < jsonArray.length(); i++) {
                title = jsonArray.getJSONObject(i).getString("data_name");
                desc = jsonArray.getJSONObject(i).getString("data_text");
                imgurl = jsonArray.getJSONObject(i).getString("data_img");
                price = jsonArray.getJSONObject(i).getString("data_price");

                System.out.println(title);
                System.out.println(desc);
                System.out.println(imgurl);
                menu_list.add(new MenuVO(imgurl, title, desc, price,""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressOFF();
        mMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, MenuVO menuVO) {
        dialog.show();
        dialog_sidemenu(menuVO);
    }

    public void dialog_sidemenu(final MenuVO menuVO) {

        price_burger = Integer.parseInt(menuVO.getPrice());
        textView_totalPrice.setText(Integer.toString(price_burger));
        isSet = false;

        radioGroup_set.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton_set) {
                    layout_drink.setVisibility(View.VISIBLE);
                    layout_sidemenu.setVisibility(View.VISIBLE);
                    isSet = true;
                }
                if (checkedId == R.id.radioButton_single) {
                    layout_drink.setVisibility(View.INVISIBLE);
                    layout_sidemenu.setVisibility(View.INVISIBLE);
                    isSet = false;
                }
            }
        });

        radioGroup_drink.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) dialog.findViewById(checkedId);
                option_drink = radioButton.getText().toString();
                price_drink = option_drink.replaceAll("[^0-9]","");
                System.out.println(price_drink);
            }
        });

        radioGroup_side.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) dialog.findViewById(checkedId);
                option_side = radioButton.getText().toString();
                price_side = option_side.replaceAll("[^0-9]","");
                System.out.println(price_side);
            }
        });

        button_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSet == false) {
                    textView_option.setText("단품");
                    price_total = price_burger + Integer.parseInt(price_drink) + Integer.parseInt(price_side);
                }
                if (isSet == true) {
                    textView_option.setText(option_drink + " " + option_side);
                    price_total = price_burger + Integer.parseInt(price_drink) + Integer.parseInt(price_side) + 2200;
                }

                textView_totalPrice.setText(Integer.toString(price_total) + "원");
                addSelectedMenu(menuVO);
            }
        });
    }

    public void addSelectedMenu (MenuVO menuVO) {
        ContentValues addRowValue = new ContentValues();
        addRowValue.put("imgurl", menuVO.getImgUrl());
        addRowValue.put("title", menuVO.getTitle());
        addRowValue.put("option", textView_option.getText().toString());
        addRowValue.put("price", Integer.toString(price_total));
        dbManager.insert(addRowValue);
    }

    public void progressON (Activity activity, String message) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if(progressDialog != null && progressDialog.isShowing()) {

        }
        else {
            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.loading_layout);
            progressDialog.show();
        }

        final ImageView imageView = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        imageView.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });

        TextView textView = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
    }

    public void progressOFF() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}

