package android.example.finalproject.model;

import android.graphics.Bitmap;

public class RecipePojo {

    private long id;
    private String title;
    private Bitmap image;
    private  String url;

    public RecipePojo( String title, Bitmap image, String url) {
        this.title = title;
        this.image = image;
        this.url = url;
    }
    public RecipePojo(long id, String title, Bitmap image, String url) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
