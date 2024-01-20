package in.games.gdmatkalive.Model;

public class RateModel {
    String id,rate_range,name,rate,type,jackpot_rate;

    public RateModel() {
    }

    public String getJackpot_rate() {
        return jackpot_rate;
    }

    public void setJackpot_rate(String jackpot_rate) {
        this.jackpot_rate = jackpot_rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate_range() {
        return rate_range;
    }

    public void setRate_range(String rate_range) {
        this.rate_range = rate_range;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
