package model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A Read-only implementation of Reversi, to be used for observation without mutating the original.
 */
public class ReadOnlyReversiImpl implements ReadOnlyReversiModel {
  private final IReversiModel model;

  /**
   * Basic ReversiModel constructor.
   * INVARIANT: q + r + s == 0
   */
  public ReadOnlyReversiImpl(IReversiModel model) {
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    return model.isGameOver();
  }

  @Override
  public Tile getTileAt(int q, int r, int s)
          throws IllegalArgumentException, IllegalStateException {
    return model.getTileAt(q, r, s);
  }

  @Override
  public int getRadius() {
    return model.getRadius();
  }

  @Override
  public ReadOnlyReversiModel getCopy() {
    return model.getCopy();
  }

  @Override
  public int getScore(PlayerTurn pt) {
    return model.getScore(pt);
  }

  @Override
  public PlayerTurn getPlayerTurn() {
    return model.getPlayerTurn();
  }

  @Override
  public boolean isMoveValid(int q, int r, int s) {
    return model.isMoveValid(q, r, s);
  }

  @Override
  public ArrayList<Tile> copyTiles() {
    return this.model.copyTiles();
  }

  @Override
  public int countFlipsForMove(int q, int r, int s) {
    return model.countFlipsForMove(q, r, s);
  }
}
