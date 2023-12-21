import org.junit.Test;
import org.junit.Before;

import model.FillType;
import model.PlayerTurn;
import model.HexReversiModel;
import model.HexTile;
import controller.SimpleAI;
import model.SquareReversiModel;
import view.HexReversiTextualView;
import view.SquareReversiTextualView;

import static org.junit.Assert.assertEquals;

/**
 * A class for testing SimpleAI's strategy and edge cases.
 */
public class TestSimpleAI {
  HexReversiModel model4;
  HexReversiModel model3;
  HexReversiModel model2;
  HexReversiTextualView view;
  HexTile tile;
  SimpleAI simpleAI;
  SquareReversiModel squareModel;
  SquareReversiTextualView squareView;

  @Before
  public void init() {
    model4 = new HexReversiModel(4);
    model3 = new HexReversiModel(3);
    model2 = new HexReversiModel(2);
    view = new HexReversiTextualView(model4);
    tile = new HexTile(0, 0, 0);
    squareModel = new SquareReversiModel(4);
    squareView = new SquareReversiTextualView(squareModel);
  }

  @Test
  public void testSimpleAIPicksBestMoveHex() {
    model4.startGame();
    model4.placeTile(-1, 2, -1);
    assertEquals(model4.getScore(PlayerTurn.BLACK), 2);
    assertEquals(model4.getScore(PlayerTurn.WHITE), 5);
    System.out.println(view.textRender()); // only one move that gets 2 tiles (all others give 1)
    simpleAI = new SimpleAI(this.model4, PlayerTurn.WHITE);
    simpleAI.playNextMove();
    assertEquals(model4.getScore(PlayerTurn.BLACK), 5);
    assertEquals(model4.getScore(PlayerTurn.WHITE), 3);
    System.out.println(view.textRender());
  }

  @Test
  public void testSimpleAIPicksBestMoveSquare() {
    squareModel.startGame();
    squareModel.placeTile(4, 2, 0);
    assertEquals(squareModel.getScore(PlayerTurn.BLACK), 1);
    assertEquals(squareModel.getScore(PlayerTurn.WHITE), 4);
    simpleAI = new SimpleAI(this.squareModel, PlayerTurn.WHITE);
    simpleAI.playNextMove();
    assertEquals(squareModel.getScore(PlayerTurn.BLACK), 3);
    assertEquals(squareModel.getScore(PlayerTurn.WHITE), 3);
    System.out.println(view.textRender());
  }

  @Test
  public void testSimpleAITieBreakerHex() {
    model3.startGame();
    assertEquals(FillType.EMPTY, model3.getTileAt(1, -2, 1).getFT());
    System.out.println(view.textRender()); // all moves are equal, so should prioritize one
    simpleAI = new SimpleAI(this.model3, PlayerTurn.WHITE);
    simpleAI.playNextMove();
    assertEquals(FillType.WHITE, model3.getTileAt(1, -2, 1).getFT());
    System.out.println(view.textRender());
  }

  @Test
  public void testSimpleAITieBreakerSquare() {
    squareModel.startGame();
    assertEquals(FillType.EMPTY, squareModel.getTileAt(4, 2, 0).getFT());
    System.out.println(squareView.textRender()); // all moves are equal, so should prioritize one
    simpleAI = new SimpleAI(this.squareModel, PlayerTurn.WHITE);
    simpleAI.playNextMove();
    System.out.println(squareView.textRender());
    assertEquals(FillType.WHITE, squareModel.getTileAt(4, 2, 0).getFT());
    System.out.println(squareView.textRender());
  }

  @Test
  public void testSimpleAINoMovesHex() {
    model2.startGame();
    System.out.println(view.textRender()); // no moves (radius 1)
    assertEquals(3, model2.getScore(PlayerTurn.BLACK));
    assertEquals(3, model2.getScore(PlayerTurn.WHITE));
    simpleAI = new SimpleAI(this.model2, PlayerTurn.WHITE);
    simpleAI.playNextMove();
    simpleAI.playNextMove();
    assertEquals(3, model2.getScore(PlayerTurn.BLACK));
    assertEquals(3, model2.getScore(PlayerTurn.WHITE)); // no gain because it can only pass
  }

  @Test
  public void testSimpleAIKeepsGoingUntilGameOverSquare() {
    squareModel.startGame();
    while (!squareModel.isGameOver()) {
      simpleAI = new SimpleAI(this.squareModel, PlayerTurn.WHITE);
      simpleAI.playNextMove();
    }
    int total = squareModel.getScore(PlayerTurn.WHITE) + squareModel.getScore(PlayerTurn.BLACK);
    assertEquals(64, total);
  }
}