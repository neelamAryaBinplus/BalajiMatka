package in.games.gdmatkalive.Model;

public class StarlineGameModel {

    String id,s_game_time,s_game_end_time,s_game_number;

    public StarlineGameModel() {
    }

    public StarlineGameModel(String id, String s_game_time, String s_game_end_time, String s_game_number) {
        this.id = id;
        this.s_game_time = s_game_time;
        this.s_game_end_time = s_game_end_time;
        this.s_game_number = s_game_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getS_game_time() {
        return s_game_time;
    }

    public void setS_game_time(String s_game_time) {
        this.s_game_time = s_game_time;
    }

    public String getS_game_end_time() {
        return s_game_end_time;
    }

    public void setS_game_end_time(String s_game_end_time) {
        this.s_game_end_time = s_game_end_time;
    }

    public String getS_game_number() {
        return s_game_number;
    }

    public void setS_game_number(String s_game_number) {
        this.s_game_number = s_game_number;
    }
}


