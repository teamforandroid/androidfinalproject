package android.example.finalproject.model;

public class RecipePojo {

    private long id;
    private String title;
    private String urlImage;
    private  String url;

    public RecipePojo( String title, String urlImage, String url) {
        this.title = title;
        this.urlImage = urlImage;
        this.url = url;
    }
    public RecipePojo(long id, String title, String urlImage, String url) {
        this.id = id;
        this.title = title;
        this.urlImage = urlImage;
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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
