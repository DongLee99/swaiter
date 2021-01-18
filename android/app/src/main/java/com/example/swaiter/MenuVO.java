package com.example.swaiter;

import java.io.Serializable;

public class MenuVO implements Serializable {
    private int _id;
    private String imgUrl;
    private String title;
    private String desc;
    private String price;
    private String option;

    public MenuVO() {

    }

    public MenuVO(String imgUrl, String title, String desc, String price, String option) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.option = option;
    }

    public MenuVO(int _id, String imgUrl, String title, String desc, String price, String option) {
        this._id = _id;
        this.imgUrl = imgUrl;
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.option = option;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

}
