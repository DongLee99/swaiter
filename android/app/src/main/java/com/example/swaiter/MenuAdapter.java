
package com.example.swaiter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
  private Context mContext;
  private ArrayList<MenuVO> list_menu;

  public OnItemClickListener onItemClickListener = null;

  public interface OnItemClickListener {
    void onItemClick(View view, MenuVO menuVO);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    onItemClickListener = listener;
  }

  public MenuAdapter(Context mContext, ArrayList<MenuVO> list_menu) {
    this.mContext = mContext;
    this.list_menu = list_menu;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View converView = LayoutInflater.from(mContext).inflate(R.layout.menu_item, parent, false);
    return new ViewHolder(converView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final MenuVO menuVO = list_menu.get(position);

    Glide.with(mContext).load(menuVO.getImgUrl()).thumbnail(0.5f).into(holder.img_menu);
    holder.txt_title.setText(menuVO.getTitle());
    holder.txt_desc.setText(menuVO.getDesc());
    holder.txt_price.setText("가격 : " + menuVO.getPrice() + "원");

    holder.layout_menu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemClickListener.onItemClick(v, menuVO);
      }
    });
  }

  @Override
  public int getItemCount() {
    return list_menu.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private LinearLayout layout_menu;
    private ImageView img_menu;
    private TextView txt_title;
    private TextView txt_desc;
    private TextView txt_price;

    public ViewHolder (View convertView) {
      super(convertView);

      layout_menu = (LinearLayout) convertView.findViewById(R.id.layout_menu);
      img_menu = (ImageView) convertView.findViewById(R.id.img_menu);
      txt_title = (TextView) convertView.findViewById(R.id.txt_title);
      txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);
      txt_price = (TextView) convertView.findViewById(R.id.txt_price);
    }
  }

}