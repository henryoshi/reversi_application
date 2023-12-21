package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a SquareReversiModel object, the square tile basic model for a game of Reversi.
 * Keeps track of the board state, player turn, radius, and each turn's past state.
 */
public class SquareReversiModel implements IReversiModel {
  /**
   * Maintains a list of all tiles.
   */
  private List<Tile> tiles;
  /**
   * The radius in hexagons of the game board.
   */
  private int radius;
  /**
   * A boolean to monitor whether the game has been started or not.
   */
  private boolean isStarted;
  /**
   * A list of booleans representing whether each move in the game has been
   * a pass (true) or a place (false).
   */
  private List<Boolean> lastPasses;
  /**
   * An enum to track whose turn it is currently to play.
   */
  private PlayerTurn player;
  /**
   * list of listeners of the model.
   */
  private List<ModelNotificationListener> listeners;

  /**
   * Basic SquareReversiModel constructor.
   */
  public SquareReversiModel(int radius) {
    if (radius < 2 || radius % 2 == 1) {
      throw new IllegalArgumentException("Invalid radius parameter");
    }
    this.listeners = new ArrayList<>();
    this.lastPasses = new ArrayList<>();
    this.radius = radius;
    this.tiles = createBoard();
    this.player = PlayerTurn.WHITE;
  }

  private SquareReversiModel(int radius, ArrayList<Tile> tiles, ArrayList<Boolean> passes,
                             PlayerTurn pt) {
    if (radius < 2) {
      throw new IllegalArgumentException("Invalid radius parameter");
    }
    this.isStarted = true;
    this.lastPasses = Objects.requireNonNull(passes);
    this.radius = radius;
    this.tiles = Objects.requireNonNull(tiles);
    this.player = Objects.requireNonNull(pt);
  }

  private ArrayList<Tile> createBoard() {
    ArrayList<Tile> toReturn = new ArrayList<>();

    for (int row = 0; row <= 2 * this.radius - 1; row++) {
      for (int col = 0; col <= 2 * this.radius - 1; col++) {
        Tile t = new SquareTile(row, col);
        toReturn.add(t);
        if (t.hasCoords(this.radius - 1, this.radius - 1, 0)
                || t.hasCoords(this.radius, this.radius, 0)) {
          t.setFillType(FillType.WHITE);
        } else if (t.hasCoords(this.radius, this.radius - 1, 0)
                || t.hasCoords(this.radius - 1, this.radius, 0)) {
          t.setFillType(FillType.BLACK);
        }
      }
    }

    return toReturn;
  }


  @Override
  public boolean isGameOver() throws IllegalStateException {
    throwIfNotStarted();
    if (!this.anyMovesLeft()) { // end game if neither player can move THIS
      System.out.println("no moves left");
      emitPlayerTurnNotification(PlayerTurn.OVER);
      return true;
    }

    if (this.lastPasses.isEmpty() || this.lastPasses.size() == 1) {
      return false;
    }

    boolean over = this.lastPasses.get(this.lastPasses.size() - 1)
            && this.lastPasses.get(this.lastPasses.size() - 2);
    if (over) {
      emitPlayerTurnNotification(PlayerTurn.OVER);
    }
    return over;
  }

  private boolean anyMovesLeft() {
    if (this.doesValidMoveExistForCurrentPlayer()) { // THIS
      return true;
    }
    this.player = this.player.flip();
    if (this.doesValidMoveExistForCurrentPlayer()) {
      this.player = this.player.flip();
      return true;
    }
    return false;
  }

  private boolean doesValidMoveExistForCurrentPlayer() {
    for (Tile tile : this.tiles) {
      if (this.isMoveValid(tile.getQorX(), tile.getRorY(), tile.getS())) {
        return true;
      }
    }
    return false;
  }

  private void emitPlayerTurnNotification(PlayerTurn playerTurn) {
    if (this.listeners != null) {
      for (ModelNotificationListener listener : this.listeners) {
        listener.turnFor(playerTurn);
      }
    }
  }

  @Override
  public Tile getTileAt(int q, int r, int s)
          throws IllegalArgumentException, IllegalStateException {
    Tile tile = getTileReferenceAt(q, r);
    Tile toReturn = new SquareTile(tile.getQorX(), tile.getRorY());
    toReturn.setFillType(tile.getFT());
    return toReturn;
  }

  private Tile getTileReferenceAt(int x, int y) {
    throwIfBadXY(x, y);
    for (Tile tile : this.tiles) {
      if (tile.hasCoords(x, y, 0)) {
        return tile;
      }
    }
    throw new IllegalArgumentException("No tile at coordinates (x, y): "
            + x + ", " + y);
  }

  private void throwIfBadXY(int x, int y) {
    if (!doCoordsExist(x, y, 0)) {
      throw new IllegalArgumentException("Invalid x " + x + " and y " + y + " given.");
    }
  }

  /**
   * Throw an exception if the game has not started yet.
   *
   * @throws IllegalStateException if the game has not started
   */
  private void throwIfNotStarted() throws IllegalStateException {
    if (!this.isStarted) {
      throw new IllegalStateException("Game has not been started.");
    }
  }

  @Override
  public int getRadius() {
    return this.radius;
  }

  @Override
  public void placeTile(int q, int r, int s)
          throws IllegalArgumentException, IllegalStateException {
    throwIfNotStarted();
    this.isGameOver();

    if (!this.isMoveValid(q, r, s)) {
      throw new IllegalStateException("Invalid move (x, y): " + q + ", " + r);
    }

    Tile tilePlaced = this.getTileReferenceAt(q, r);
    FillType ft = this.getFillTypeOfPlayerTurn(this.player);
    this.updateTilesAround(tilePlaced, ft);

    tilePlaced.setFillType(ft);

    this.player = this.player.flip();
    this.lastPasses.add(false);
    emitPlayerTurnNotification(this.player);
  }

  private int updateTilesAround(Tile target, FillType ft) {
    List<Tile> bounds = new ArrayList<>();
    findOneBound(bounds, ft, target, new int[]{0, -1});
    findOneBound(bounds, ft, target, new int[]{0, 1});
    findOneBound(bounds, ft, target, new int[]{-1, 0});
    findOneBound(bounds, ft, target, new int[]{1, 0});
    findOneBound(bounds, ft, target, new int[]{1, -1});
    findOneBound(bounds, ft, target, new int[]{-1, 1});
    findOneBound(bounds, ft, target, new int[]{-1, -1});
    findOneBound(bounds, ft, target, new int[]{1, 1});
    return flipBetweenAll(target, bounds);
  }

  private void findOneBound(List<Tile> bounds, FillType ft, Tile target, int[] dir) {
    int x = target.getQorX();
    int y = target.getRorY();
    for (int i = 1; doCoordsExist(x + dir[0] * i, y + dir[1] * i, 0); i++) {
      Tile currTile = getTileReferenceAt(x + dir[0] * i, y + dir[1] * i);
      if (currTile.getFT().equals(ft)) {
        bounds.add(currTile);
        break;
      } else if (currTile.getFT().equals(FillType.EMPTY)) {
        break;
      }
    }
  }

  private int flipBetween(Tile target, Tile other) {
    int count = 0;
    int x = target.getQorX();
    int y = target.getRorY();
    int diffX = x - other.getQorX();
    int diffY = y - other.getRorY();
    int incX = Integer.compare(diffX, 0);
    int incY = Integer.compare(diffY, 0);

    int range = Math.max(Math.abs(diffX), Math.abs(diffY));

    for (int i = 1; i < range; i++) {
      int newX = x - (i * incX);
      int newY = y - (i * incY);
      if (doCoordsExist(newX, newY, 0)) {
        Tile curr = getTileReferenceAt(newX, newY);
        if (!curr.getFT().equals(target.getFT())) {
          curr.flipTile();
          count++;
        }
      }
    }
    return count;
  }

  private int flipBetweenAll(Tile target, List<Tile> bounds) {
    int count = 0;
    for (Tile tile : bounds) {
      count += flipBetween(target, tile);
    }
    return count;
  }

  @Override
  public int countFlipsForMove(int q, int r, int s) {
    IReversiModel copiedModel = this.getCopy();
    PlayerTurn aboutToPlay = copiedModel.getPlayerTurn();
    int priorScore = copiedModel.getScore(aboutToPlay);
    if (copiedModel.isMoveValid(q, r, -q - r)) {
      copiedModel.placeTile(q, r, -q - r);
    }
    int postScore = copiedModel.getScore(aboutToPlay);
    return postScore - priorScore;
  }

  @Override
  public IReversiModel getCopy() {
    return new SquareReversiModel(this.radius, this.copyTiles(),
            new ArrayList<>(this.lastPasses), this.player);
  }

  @Override
  public int getScore(PlayerTurn pt) {
    FillType ft = this.getFillTypeOfPlayerTurn(pt);
    int score = 0;
    for (Tile tile : this.tiles) {
      if (ft.equals(tile.getFT())) {
        score += 1;
      }
    }
    return score;
  }

  @Override
  public PlayerTurn getPlayerTurn() {
    return this.player;
  }

  @Override
  public boolean isMoveValid(int q, int r, int s) {
    List<Tile> backupTiles = this.copyTiles();
    Tile tilePlaced = getTileReferenceAt(q, r);

    if (!tilePlaced.getFT().equals(FillType.EMPTY)) {
      return false;
    }

    FillType ft = this.getFillTypeOfPlayerTurn(this.player);
    tilePlaced.setFillType(ft);
    int numTotalFlips = updateTilesAround(tilePlaced, ft);
    this.tiles = backupTiles; // restore tiles to old backed up tiles

    return numTotalFlips != 0;
  }

  @Override
  public ArrayList<Tile> copyTiles() {
    ArrayList<Tile> toReturn = new ArrayList<>();
    for (Tile tile : this.tiles) { // make a deep copy of current tiles
      toReturn.add(tile.getClone());
    }
    return toReturn;
  }

  @Override
  public void startGame() throws IllegalArgumentException, IllegalStateException {
    if (this.isStarted) {
      throw new IllegalStateException("Game already started.");
    }
    this.isStarted = true;
    emitPlayerTurnNotification(this.player);
  }

  @Override
  public void pass() throws IllegalStateException {
    throwIfNotStarted();
    this.lastPasses.add(true);
    boolean gameOver = isGameOver();
    if (gameOver) {
      emitPlayerTurnNotification(PlayerTurn.OVER);
    } else {
      // Changes whose turn it is
      this.player = this.player.flip();
      emitPlayerTurnNotification(this.player);
    }
  }

  @Override
  public FillType getFillTypeOfPlayerTurn(PlayerTurn pt) {
    FillType ft;
    if (pt.equals(PlayerTurn.WHITE)) {
      ft = FillType.WHITE;
    } else {
      ft = FillType.BLACK;
    }
    return ft;
  }

  @Override
  public void addModelListener(ModelNotificationListener listener) {
    this.listeners.add(Objects.requireNonNull(listener));
  }

  @Override
  public boolean doCoordsExist(int x, int y, int extra) {
    return !(x < 0 || x >= 2 * this.radius || y < 0 || y >= 2 * this.radius);
  }
}
