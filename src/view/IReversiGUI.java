package view;


/**
 * An interface including the neccessary methods of any IReversiCanvas.
 * Displays and takes input for a game of Reversi.
 */
public interface IReversiGUI {
  /**
   * Adds a new tileClickedListener to monitor when a tile is selected by the
   * user interacting with this IReversiCanvas.
   *
   * @param e is a TileClickedListener
   */
  void addTileClickedListener(TileClickedListener e);

  /**
   * Sets the label at the top of the Canvas to the provided text.
   *
   * @param message a String to display to the user
   */
  void setLabel(String message);

  /**
   * Updates the current view of the board.
   */
  void updateBoard();

  /**
   * Notifies the player with a popup error message.
   *
   * @param message the string to be displayed to the user
   */
  void notifyPlayer(String message);
}
