package view;

import java.util.Objects;

import model.Tile;
import model.SquareReversiModel;

/**
 * A class for rendering a text view of a ReversiModel object.
 */
public class SquareReversiTextualView implements IReversiTextualView {
  SquareReversiModel model;

  /**
   * Constructor that takes in a SquareReversiModel to render.
   *
   * @param model a ReversiModel object to be rendered.
   */
  public SquareReversiTextualView(SquareReversiModel model) {
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public String textRender() {
    StringBuilder builder = new StringBuilder();
    int radius = this.model.getRadius();
    for (int y = 0; y < 2 * radius; y++) {
      for (int x = 0; x < 2 * radius; x++) {
        Tile tile = this.model.getTileAt(x, y, 0);
        builder.append(tile.toString());
        builder.append(" ");
      }
      builder.append("\n");
    }
    return builder.toString();
  }
}
