package in.games.gdmatkalive.Interfaces;

import java.util.ArrayList;

import in.games.gdmatkalive.Model.GameStatusModel;


public interface OnAllGamesListener {
    void onMatkaGames(ArrayList<GameStatusModel> matkaGameList);
    void onStarlineGames(ArrayList<GameStatusModel> starlineGAmeList);

}
