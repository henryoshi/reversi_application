package controller;

import model.PlayerTurn;

/**
 * Represents some Reversi Player and its promised feature functions.
 */
public interface IReversiPlayer {
  /**
   * Executes this players next move on it's model field.
   */
  void playNextMove();

  /**
   * A getter for this player's PlayerTurn.
   *
   * @return this player's PlayerTurn
   */
  PlayerTurn getPlayerTurn();

  /**
   * Add given PlayerListener to this player's listeners list.
   *
   * @param pl listener to add
   */
  void addPlayerListener(PlayerListener pl);
}
