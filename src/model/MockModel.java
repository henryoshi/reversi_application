package model;

import java.util.ArrayList;

/**
 * A MockModel for an IReversiModel, records the actions of this model to a log.
 */
public class MockModel implements IReversiModel {
  private IReversiModel delegate;

  /**
   * Mock model constructor.
   *
   * @param delegate the real model to run the game on
   */
  public MockModel(IReversiModel delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean doCoordsExist(int q, int r, int s) {
    return delegate.doCoordsExist(q, r, s);
  }

  @Override
  public boolean isGameOver() {
    return delegate.isGameOver();
  }

  @Override
  public ArrayList<Tile> copyTiles() {
    return delegate.copyTiles();
  }

  @Override
  public IReversiModel getCopy() {
    return new MockModel(delegate.getCopy());
  }

  @Override
  public void pass() {
    delegate.pass();
  }

  @Override
  public void placeTile(int q, int r, int s) {
    System.out.println("Considering tile (q, r, s): (" + q + ", " + r + ", " + s + ")");
    delegate.placeTile(q, r, s);
  }

  @Override
  public int countFlipsForMove(int q, int r, int s) {
    return delegate.countFlipsForMove(q, r, s);
  }

  @Override
  public int getScore(PlayerTurn pt) {
    return delegate.getScore(pt);
  }

  @Override
  public PlayerTurn getPlayerTurn() {
    return delegate.getPlayerTurn();
  }

  @Override
  public boolean isMoveValid(int q, int r, int s) {
    System.out.println("Seeing if tile is valid (q, r, s): (" + q + ", " + r + ", " + s + ")");
    return delegate.isMoveValid(q, r, s);
  }

  @Override
  public FillType getFillTypeOfPlayerTurn(PlayerTurn pt) {
    return delegate.getFillTypeOfPlayerTurn(pt);
  }

  @Override
  public Tile getTileAt(int q, int r, int s) {
    return delegate.getTileAt(q, r, s);
  }

  @Override
  public int getRadius() {
    return delegate.getRadius();
  }

  @Override
  public void startGame() {
    delegate.startGame();
  }

  @Override
  public void addModelListener(ModelNotificationListener listener) {
    delegate.addModelListener(listener);
  }
}