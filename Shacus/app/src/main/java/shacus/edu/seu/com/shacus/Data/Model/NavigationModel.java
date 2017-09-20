package shacus.edu.seu.com.shacus.Data.Model;

import java.io.Serializable;

public class NavigationModel implements Serializable {

    private String img_url;
    private String web_url;

    public String getWeburl() {
        return web_url;
    }

    public void setWeburl(String weburl) {
        this.web_url = weburl;
    }

    public String getImgurl() {
        return img_url;
    }

    public void setImgurl(String imgurl) {
        this.img_url = imgurl;
    }
}
