package shacus.edu.seu.com.shacus.Data.Model;

/**
 * Created by iris1211 on 2017/9/15.
 */

public class MyRecord {
    private int id;
    private String label;
    private String imageUrl;

    public MyRecord(int id, String label, String imageUrl) {
        super();
        this.id = id;
        this.label = label;
        this.imageUrl = imageUrl;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
