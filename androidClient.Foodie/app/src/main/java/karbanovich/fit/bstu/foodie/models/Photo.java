package karbanovich.fit.bstu.foodie.models;

public class Photo {

    private int id;
    private String url;

    public Photo(int id, String url) {
        this.id = id;
        this.url  = url;
    }

    public Photo() { }

    public int getId() { return id; }
    public String getUrl() { return url; }

    public void setId(int id) { this.id = id; }
    public void setUrl(String url) { this.url = url; }
}
