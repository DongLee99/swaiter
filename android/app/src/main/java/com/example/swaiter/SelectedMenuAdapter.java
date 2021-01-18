package com.example.swaiter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SelectedMenuAdapter extends RecyclerView.Adapter<SelectedMenuAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<MenuVO> selected_menu;
    DBManager dbManager;

    public OnItemClickListener onItemClickListener = null;
    public View.OnClickListener onClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, MenuVO menuVO);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public SelectedMenuAdapter(Context mContext, ArrayList<MenuVO> selected_menu) {
        this.mContext = mContext;
        this.selected_menu = selected_menu;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View converView = LayoutInflater.from(mContext).inflate(R.layout.selectedmenu_item, parent, false);
        return new ViewHolder(converView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MenuVO menuVO = selected_menu.get(position);

        Glide.with(mContext).load(menuVO.getImgUrl()).thumbnail(0.5f).into(holder.img_select);
        holder.txt_selectTitle.setText(menuVO.getTitle());
        holder.txt_selectOption.setText(menuVO.getOption());
        holder.txt_price.setText(menuVO.getPrice()+"원");

        Button button_delete = holder.button_delete;
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_menu.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(position, selected_menu.size());
                dbManager.delete("_id" + "=" + menuVO.get_id(), null);

            }
        });
    }

    @Override
    public int getItemCount() {
        return selected_menu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout_selectedMenu;
        private ImageView img_select;
        private TextView txt_selectTitle;
        private TextView txt_selectOption;
        private TextView txt_price;
        private Button button_delete;


        public ViewHolder (final View convertView) {
            super(convertView);

            dbManager = DBManager.getInstance(mContext);

            layout_selectedMenu = (LinearLayout) convertView.findViewById(R.id.layout_selectedMenu);
            img_select = (ImageView) convertView.findViewById(R.id.imageView_select);
            txt_selectTitle = (TextView) convertView.findViewById(R.id.textView_selectTitle);
            txt_selectOption = (TextView) convertView.findViewById(R.id.textView_selectOption);
            txt_price = (TextView) convertView.findViewById(R.id.textView_price);
            button_delete = (Button) convertView.findViewById(R.id.button_delete);

        }
    }

}
