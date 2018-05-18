package com.app.csulb.rentmybookfinal;

import java.io.Serializable;
import android.os.Parcelable;
import java.io.Serializable;


// Reward class
public class RowItem implements Serializable {
    private String name, point, info;
    private int image;

    public RowItem(String item_name, String req_point, String item_info, int item_image){
        this.name = item_name;
        this.point = req_point;
        this.info = item_info;
        this.image = item_image;
    }

    public String getName(){return name;}

    public void setName(String item_name){this.name = item_name;}

    public String getPoint(){return point;}

    public void setPoint(String req_point){this.point = req_point;}

    public String getInfo(){return info;}

    public void setInfo(String item_info){this.info = item_info;}

    public int getImage(){return image;}

    public void setImage(int item_image){this.image = item_image;}


}
