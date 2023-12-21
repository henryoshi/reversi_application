package model;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import provider.cs3500.reversi.controller.ModelFeatures;
import provider.cs3500.reversi.model.MutableReversiModel;
import provider.cs3500.reversi.model.Hexagon;
import provider.cs3500.reversi.model.Disc;

/**
 *  A model to connect with the provider view and controller, but connect the IReversiModel
 *  logic to complete the adaption.
 */
public class ProviderModelToReversiModelAdapter implements MutableReversiModel {
  private IReversiModel model;

  /**
   * Constructor that takes in an IReversiModel.
   * @param model an IReversiModel to play on.
   */
  public ProviderModelToReversiModelAdapter(IReversiModel model) {
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public Map<Hexagon, Disc> getBoard() {
    Map<Hexagon, Disc> boardMap = new HashMap<>();
    for (Tile tile : this.model.copyTiles()) {
      // convert tile's FillType to disc
      Disc disc = new Disc(fillTypeToDiscColor(tile.getFT()));
      // convert tile's coordinates to hexagon using the following conversion:
      // x = -s and y = -r
      Hexagon hexagon = new Hexagon(-tile.getS(), -tile.getRorY());
      boardMap.put(hexagon, disc);
    }
    return boardMap;
  }

  /**
   * Convert FillType to DiscColor.
   *
   * @param ft FillType to convert
   * @return DiscColor associated with FillType
   */
  private Disc.DiscColor fillTypeToDiscColor(FillType ft) {
    Disc.DiscColor discEnum;
    switch (ft) {
      case BLACK: // FillType.BLACK, but it screams at me if I write it all out
        discEnum = Disc.DiscColor.BLACK;
        break;
      case WHITE: // FillType.WHITE, but it screams at me if I write it all out
        discEnum = Disc.DiscColor.WHITE;
        break;
      default:
        discEnum = Disc.DiscColor.NONE;
        break;
    }
    return discEnum;
  }

  /**
   * Adds Listeners to the model.
   * @param ml Listener to add to the model
   */
  public void addListener(ModelNotificationListener ml) {
    this.model.addModelListener(ml);
  }

  @Override
  public boolean canPlaceDisc(Disc color) {
    FillType ft = model.getFillTypeOfPlayerTurn(this.model.getPlayerTurn());
    return fillTypeToDiscColor(ft).equals(color.getDiscColor());
  }

  @Override
  public boolean gameOver() {
    return this.model.isGameOver();
  }

  @Override
  public Disc getDisc(Hexagon tile) throws IllegalArgumentException {
    int tileR = - tile.getY();
    int tileS = - tile.getX();
    int tileQ = - tileR - tileS;
    Tile modelTile = this.model.getTileAt(tileQ, tileR, tileS);
    return new Disc(this.fillTypeToDiscColor(modelTile.getFT()));
  }

  @Override
  public Disc getTurn() {
    return this.playerTurnToDisc(model.getPlayerTurn());
  }

  /**
   * Convert PlayerTurn to Disc.
   * @param pt PlayerTurn to convert
   * @return Disc associated with PlayerTurn color
   */
  private Disc playerTurnToDisc(PlayerTurn pt) {
    Disc.DiscColor discColor = fillTypeToDiscColor(model.getFillTypeOfPlayerTurn(pt));
    return new Disc(discColor);
  }

  @Override
  public int getScore(Disc turn) {
    if (fillTypeToDiscColor(model.getFillTypeOfPlayerTurn(PlayerTurn.WHITE))
            .equals(turn.getDiscColor())) {
      return this.model.getScore(PlayerTurn.WHITE);
    }
    else if (fillTypeToDiscColor(model.getFillTypeOfPlayerTurn(PlayerTurn.BLACK))
            .equals(turn.getDiscColor())) {
      return this.model.getScore(PlayerTurn.BLACK);
    }
    return 0;
  }

  @Override
  public int getGameBoardSideLength() {
    return this.model.getRadius();
  }

  @Override
  public int getConsecutivePasses() {
    return 0; // unsure how to do, since lastPasses is private
  }

  @Override
  public Optional<Integer> getMaxNumConsecutivePassesAllowed() {
    return Optional.of(2);
  }

  @Override
  public Disc getWinner() throws IllegalStateException {
    if (!this.model.isGameOver()) {
      throw new IllegalStateException();
    }
    Disc whiteDisc = new Disc(Disc.DiscColor.WHITE);
    Disc blackDisc = new Disc(Disc.DiscColor.BLACK);
    if (this.getScore(whiteDisc) > this.getScore(blackDisc)) {
      return whiteDisc;
    }
    return blackDisc; // not sure what to return if it's a tie, so will prioritize black
  }

  @Override
  public MutableReversiModel copyGame() {
    return new ProviderModelToReversiModelAdapter(this.model.getCopy());
  }

  @Override
  public void placeDisc(Hexagon tile) throws IllegalStateException, IllegalArgumentException {
    gameOver();
    int tileR = -tile.getY();
    int tileS = -tile.getX();
    int tileQ = -tileR - tileS;
    System.out.println("provider placing");
    this.model.placeTile(tileQ, tileR, tileS);
  }

  @Override
  public void passTurn() throws IllegalStateException {
    gameOver();
    System.out.println("provider placing");
    if (!model.isGameOver()) {
      this.model.pass();
    }
    gameOver();
  }

  @Override
  public int addFeatures(ModelFeatures feature) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public void startGame() throws IllegalStateException {
    this.model.startGame();
  }
}
