package view;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

import model.Tile;
import model.HexReversiModel;

/**
 * A class for rendering a text view of a ReversiModel object.
 */
public class HexReversiTextualView implements IReversiTextualView {
  HexReversiModel model;

  /**
   * Constructor that takes in a HexReversiModel to render.
   *
   * @param model a ReversiModel object to be rendered.
   */
  public HexReversiTextualView(HexReversiModel model) {
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public String textRender() {
    int radius = this.model.getRadius();
    List<List<Tile>> allRows = new ArrayList<>();

    // get an ordered list of all rows of tiles, ordered left->right and top->bottom
    for (int r = 1 - radius; r <= radius - 1; r++) {
      List<Tile> row = new ArrayList<>();
      for (int q = Math.abs(r) + 1 - (2 * radius); q <= 2 * radius - Math.abs(r) - 1; q++) {
        int s = -q - r;
        if (Math.abs(q) < radius && Math.abs(r) < radius && Math.abs(s) < radius) {
          Tile tile = this.model.getTileAt(q, r, s);
          row.add(tile);
        }
      }
      allRows.add(row);
    }

    // create a textual view of the board, row by row
    StringBuilder builder = new StringBuilder();
    for (List<Tile> row : allRows) {
      // abs(r) (where r is leftmost tile's R coordinate) is the amount of spaces before that tile
      int whitespaces = Math.abs(row.get(0).getRorY());
      builder.append(" ".repeat(whitespaces));

      for (Tile tile : row) {
        builder.append(tile.toString());
        builder.append(" ");
      }
      builder.append("\n");
    }
    return builder.toString();
  }
}
