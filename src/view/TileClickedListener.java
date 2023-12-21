package view;

/**
 * An interface representing what each object listening for user
 * clicks on a tile should function as.
 */
public interface TileClickedListener {
  /**
   * Informs this object that the given axial coordinates
   * were clicked by the user.
   *
   * @param q the q-coordinate of axial system
   * @param r the r-coordinate of axial system
   */
  void clickedAt(int q, int r);

  /**
   * Informs this object that the given coordinates were
   * attempted to place a new tile at.
   *
   * @param q the q-coordinate of axial system
   * @param r the r-coordinate of axial system
   */
  void placeTileAttempt(int q, int r);

  /**
   * Informs this object that the user attempted to pass.
   */
  void passAttempt();
}
