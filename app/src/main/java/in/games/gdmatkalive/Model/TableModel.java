package in.games.gdmatkalive.Model;

public class TableModel{
    String no,game,digits,points,type;

    public TableModel() {
    }

    public TableModel(String no,String game,String digits, String points, String type) {
        this.no = no;
        this.game = game;
        this.digits = digits;
        this.points = points;
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }}
