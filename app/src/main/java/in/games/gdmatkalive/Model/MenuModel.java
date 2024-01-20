package in.games.gdmatkalive.Model;

public class MenuModel {
    String title,id,status;
    int icon;
    public MenuModel(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public MenuModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
