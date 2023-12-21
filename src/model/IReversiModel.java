package model;

/**
 * public-facing Interface for a ReversiModel.
 */
public interface IReversiModel extends ReadOnlyReversiModel {
  /**
   * Returns a boolean representing whether the given coordinates,
   * q r and s of the axial system, or x and y (where s is irrelevent) in
   * a cartesian system.
   * @param q the first coordinate
   * @param r the second coordinate
   * @param s the third coordinate
   * @return
   */
  boolean doCoordsExist(int q, int r, int s);

  /**
   * Returns a boolean representing whether the current Reversi
   * game is over or not. A game is defined as over when both players
   * pass one after the other, forced or not.
   *
   * @return whether the game is over
   * @throws IllegalStateException if the game hasn't started yet
   */
  boolean isGameOver() throws IllegalStateException;

  /**
   * Places a tile at the given coordinates if possible. The tile must result in at least
   * one tile flipped over and must be a valid coordinate.
   *
   * @param q the q coordinate of the tile in a cube coordinate system
   * @param r the r coordinate of the tile in a cube coordinate system
   * @param s the s coordinate of the tile in a cube coordinate system
   * @throws IllegalArgumentException if q, r, and s do not equal 0 after sum.
   * @throws IllegalStateException    if the game is not started.
   */
  void placeTile(int q, int r, int s) throws IllegalArgumentException, IllegalStateException;

  /**
   * returns a copy of the board.
   *
   * @return copy of tiles
   */
  @Override
  IReversiModel getCopy();

  /**
   * Starts a new Reversi game creating a hexagonal board of the provided
   * radius.
   *
   * @throws IllegalArgumentException if the provided parameters are invalid.
   * @throws IllegalStateException    if the game is already started.
   */
  void startGame() throws IllegalArgumentException, IllegalStateException;

  /**
   * Pass the current player's turn.
   *
   * @throws IllegalStateException if game is not started yet.
   */
  void pass() throws IllegalStateException;

  /**
   * Returns the FillType associated with the given player.
   *
   * @return FillType of the given player.
   */
  FillType getFillTypeOfPlayerTurn(PlayerTurn pt);

  /**
   * Adds the provided ModelNotificationListener to the list of
   * observers in this IReversiModel.
   *
   * @param listener a modelNotificationListener that receives input from this model
   */
  void addModelListener(ModelNotificationListener listener);
}