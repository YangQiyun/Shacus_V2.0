package shacus.edu.seu.com.shacus.Data.Model;

import java.io.Serializable;

/**
 * 摄影师招募场的tab的种类
 */

public class YuePaiGroupModel implements Serializable {
    private String type;             //相当于页数
    private String name;            //tab对应的title名字
    private String description;  //tab下textview的废话


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
