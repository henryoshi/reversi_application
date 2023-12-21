package view;

/**
 * Interface for ReversiTextualView, which is able to render the board/game state using ASCII.
 */
public interface IReversiTextualView {
  /**
   * Renders the current model as a String.
   *
   * @return a String representation of the current ReversiModel
   */
  public String textRender();
}
