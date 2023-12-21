package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a HexReversiModel object, the hex tile basic model for a game of Reversi.
 * Keeps track of the board state, player turn, radius, and each turn's past state.
 */
public class HexReversiModel implements IReversiModel {
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
   * Basic HexReversiModel constructor.
   * INVARIANT: q + r + s == 0
   */
  public HexReversiModel(int radius) {
    if (this.isStarted) {
      throw new IllegalStateException("Game already started.");
    }
    if (radius < 2) {
      throw new IllegalArgumentException("Invalid radius parameter");
    }
    this.listeners = new ArrayList<>();
    this.lastPasses = new ArrayList<>();
    this.radius = radius;
    this.tiles = createBoard();
    this.player = PlayerTurn.WHITE;
  }

  /**
   * private method for strategy testing, has more specific parameters to setup the game manually.
   *
   * @param radius the radius of the hexagonal grid, with radius = 2 being a 7 cell hexagon.
   * @param tiles  tiles to be used as the board.
   * @param passes list of booleans representing which move has been a pass or not
   * @param pt     current playerTurn (player that starts and makes the first move)
   */
  private HexReversiModel(int radius, ArrayList<Tile> tiles, ArrayList<Boolean> passes,
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

  private List<Tile> createBoard() {
    List<Tile> newTiles = new ArrayList<>();
    for (int q = -this.radius + 1; q < this.radius; q++) {
      for (int r = this.radius - 1; r > -this.radius; r--) {
        int s = -q - r;
        if (Math.abs(s) < this.radius) {
          Tile tile = new HexTile(q, r, s);
          newTiles.add(tile);
          if (tile.hasCoords(0, -1, 1) || tile.hasCoords(1, 0, -1)
                  || tile.hasCoords(-1, 1, 0)) {
            tile.setFillType(FillType.BLACK);
          } else if (tile.hasCoords(0, 1, -1) || tile.hasCoords(1, -1, 0)
                  || tile.hasCoords(-1, 0, 1)) {
            tile.setFillType(FillType.WHITE);
          }
        }
      }
    }
    return newTiles;
  }

  /**
   * Throw an exception if there is no tile at position (q, r, s).
   *
   * @param q the q coordinate of the tile in a cube coordinate system
   * @param r the r coordinate of the tile in a cube coordinate system
   * @param s the s coordinate of the tile in a cube coordinate system
   * @throws IllegalArgumentException if (q, r, s) are invalid coordinates for a tile.
   */
  private void throwIfBadQRS(int q, int r, int s) throws IllegalArgumentException {
    if (!doCoordsExist(q, r, s)) {
      throw new IllegalArgumentException("Invalid passed q, r, or s: " + q + r + s);
    }
  }

  /**
   * Returns whether there is a tile at position (q, r, s).
   *
   * @param q the q coordinate of the tile in a cube coordinate system
   * @param r the r coordinate of the tile in a cube coordinate system
   * @param s the s coordinate of the tile in a cube coordinate system
   * @return whether (q, r, s) is a valid tile.
   */
  @Override
  public boolean doCoordsExist(int q, int r, int s) {
    boolean isValid = (q + r + s != 0 || Math.abs(q) >= this.radius
            || Math.abs(r) >= this.radius || Math.abs(s) >= this.radius);
    return !isValid;
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
  public boolean isGameOver() throws IllegalStateException {
    throwIfNotStarted();
    if (!this.doesValidMoveExist()) { // end game if neither player can move THIS
      emitPlayerTurnNotification(PlayerTurn.OVER);
      return true;
    }

    switch (this.lastPasses.size()) { // end game if both players have passed
      case (0):
        return false;
      case (1):
        return false;
      default:
        boolean over = this.lastPasses.get(this.lastPasses.size() - 1)
                && this.lastPasses.get(this.lastPasses.size() - 2);
        if (over) {
          emitPlayerTurnNotification(PlayerTurn.OVER);
        }
        return over;
    }
  }

  @Override
  public IReversiModel getCopy() {
    return new HexReversiModel(this.radius, this.copyTiles(),
            new ArrayList<>(this.lastPasses), this.player);
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
  public void placeTile(int q, int r, int s)
          throws IllegalArgumentException, IllegalStateException {
    throwIfNotStarted();
    this.isGameOver();

    if (!this.isMoveValid(q, r, s)) {
      throw new IllegalStateException("Invalid move (q, r, s): " + q + ", " + r + ", " + s);
    }

    Tile tilePlaced = this.getTileReferenceAt(q, r, s);
    FillType ft = this.getFillTypeOfPlayerTurn(this.player);
    this.updateTilesAround(tilePlaced, ft);

    tilePlaced.setFillType(ft);

    this.player = this.player.flip();
    this.lastPasses.add(false);
    emitPlayerTurnNotification(this.player);
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
    Tile tilePlaced = getTileReferenceAt(q, r, s);

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
  public FillType getFillTypeOfPlayerTurn(PlayerTurn pt) {
    FillType ft;
    if (pt.equals(PlayerTurn.WHITE)) {
      ft = FillType.WHITE;
    } else {
      ft = FillType.BLACK;
    }
    return ft;
  }

  /**
   * returns true if any player has a valid move.
   *
   * @return whether a valid move exists for EITHER PLAYER
   */
  private boolean doesValidMoveExist() {
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

  /**
   * returns true if current player has a valid move.
   *
   * @return whether a valid move exists for current player.
   */
  private boolean doesValidMoveExistForCurrentPlayer() {
    for (Tile tile : this.tiles) {
      if (this.isMoveValid(tile.getQorX(), tile.getRorY(), tile.getS())) {
        return true;
      }
    }
    return false;
  }

  // returns number of flipped tiles from updating
  private int updateTilesAround(Tile tilePlaced, FillType ft) {
    List<Tile> bounds = new ArrayList<>();
    findOneBound(bounds, ft, tilePlaced, new int[]{0, -1, 1});
    findOneBound(bounds, ft, tilePlaced, new int[]{0, 1, -1});
    findOneBound(bounds, ft, tilePlaced, new int[]{-1, 0, 1});
    findOneBound(bounds, ft, tilePlaced, new int[]{1, 0, -1});
    findOneBound(bounds, ft, tilePlaced, new int[]{1, -1, 0});
    findOneBound(bounds, ft, tilePlaced, new int[]{-1, 1, 0});
    return flipBetweenAll(tilePlaced, bounds);
  }

  private void findOneBound(List<Tile> bounds, FillType ft, Tile tilePlaced,
                            int[] scalars) {
    int q = tilePlaced.getQorX();
    int r = tilePlaced.getRorY();
    int s = tilePlaced.getS();
    for (int i = 1;
         doCoordsExist(q + scalars[0] * i, r + scalars[1] * i, s + scalars[2] * i); i++) {
      Tile currTile = getTileReferenceAt(q + scalars[0] * i, r + scalars[1] * i,
              s + scalars[2] * i);
      if (currTile.getFT().equals(ft)) {
        bounds.add(currTile);
        break;
      } else if (currTile.getFT().equals(FillType.EMPTY)) {
        break;
      }
    }
  }

  // returns number of flipped tiles total
  private int flipBetweenAll(Tile tilePlaced, List<Tile> bounds) {
    int count = 0;
    for (Tile tile : bounds) {
      count += flipBetween(tilePlaced, tile);
    }
    return count;
  }

  // return number of flips
  private int flipBetween(Tile tilePlaced, Tile tile) {
    int count = 0;
    int q = tilePlaced.getQorX();
    int r = tilePlaced.getRorY();
    int s = tilePlaced.getS();
    int diffQ = q - tile.getQorX();
    int diffR = r - tile.getRorY();
    int diffS = s - tile.getS();
    int incQ = Integer.compare(diffQ, 0);
    int incR = Integer.compare(diffR, 0);
    int incS = Integer.compare(diffS, 0);
    int range = Math.max(Math.max(diffQ, diffR), diffS);

    for (int i = 1; i < range; i++) {
      int newQ = q - (i * incQ);
      int newR = r - (i * incR);
      int newS = s - (i * incS);
      if (doCoordsExist(newQ, newR, newS)) {
        Tile curr = getTileReferenceAt(newQ, newR, newS);
        curr.flipTile();
        count++;
      }
    }
    return count;
  }

  /**
   * Return the tile at position (q, r, s).
   *
   * @param q the q coordinate of the tile in a cube coordinate system
   * @param r the r coordinate of the tile in a cube coordinate system
   * @param s the s coordinate of the tile in a cube coordinate system
   * @return direct reference to the tile at position (q, r, s)
   * @throws IllegalArgumentException if (q, r, s) are invalid coordinates for a tile.
   */
  private Tile getTileReferenceAt(int q, int r, int s) throws IllegalArgumentException {
    throwIfBadQRS(q, r, s);
    for (Tile tile : this.tiles) {
      if (tile.hasCoords(q, r, s)) {
        return tile;
      }
    }
    throw new IllegalArgumentException("No tile at coordinates (q, r, s): "
            + q + ", " + r + ", " + s);
  }

  @Override
  public Tile getTileAt(int q, int r, int s)
          throws IllegalArgumentException, IllegalStateException {
    Tile tile = getTileReferenceAt(q, r, s);
    Tile toReturn = new HexTile(tile.getQorX(), tile.getRorY(), tile.getS());
    toReturn.setFillType(tile.getFT());
    return toReturn;
  }

  @Override
  public int getRadius() {
    return this.radius;
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
  public void addModelListener(ModelNotificationListener listener) {
    this.listeners.add(listener);
  }

  private void emitPlayerTurnNotification(PlayerTurn pt) {
    if (this.listeners != null) {
      for (ModelNotificationListener listener : this.listeners) {
        listener.turnFor(pt);
      }
    }
  }
}
