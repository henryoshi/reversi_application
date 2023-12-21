import org.junit.Test;
import org.junit.Before;

import model.FillType;
import model.IReversiModel;
import model.PlayerTurn;
import model.HexReversiModel;
import model.Tile;
import model.HexTile;
import view.HexReversiTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A class for testing IReversiModel, ITile, and IReversiTextualView
 * commands and edge cases.
 */
public class TestHexReversiModel {
  HexReversiModel model5;
  HexReversiModel model3;
  HexReversiModel model4;
  HexReversiTextualView view;
  HexTile tile;

  @Before
  public void init() {
    model5 = new HexReversiModel(5);
    model3 = new HexReversiModel(3);
    model4 = new HexReversiModel(4);
    view = new HexReversiTextualView(model5);
    tile = new HexTile(0, 0, 0);
  }

  @Test
  public void testTextualViewRender() {
    model5.startGame();
    HexReversiTextualView bigView = new HexReversiTextualView(model5);
    String expectedRender =
            "    _ _ _ _ _ \n"
                    + "   _ _ _ _ _ _ \n"
                    + "  _ _ _ _ _ _ _ \n"
                    + " _ _ _ O X _ _ _ \n"
                    + "_ _ _ X _ O _ _ _ \n"
                    + " _ _ _ O X _ _ _ \n"
                    + "  _ _ _ _ _ _ _ \n"
                    + "   _ _ _ _ _ _ \n"
                    + "    _ _ _ _ _ \n"; // weird indent but ok
    assertEquals(expectedRender, bigView.textRender());
  }

  @Test
  public void testFlippingValidMoves() {
    model4.startGame();
    model4.placeTile(1, 1, -2);
    model4.placeTile(2, 1, -3);
    assertEquals(FillType.BLACK, model4.getTileAt(1, 1, -2).getFT());
  }

  @Test (expected = IllegalStateException.class)
  public void testNoFlipsFromPlacementThrows() {
    model4.startGame();
    model4.placeTile(0, 0, 0);
  }

  @Test
  public void testGetTileAt() {
    model4.startGame();
    assertEquals(FillType.EMPTY, model4.getTileAt(0, 0, 0).getFT());
    assertEquals(FillType.BLACK, model4.getTileAt(1, 0, -1).getFT());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTileAtBadCoordsThrows() {
    model4.startGame();
    model4.getTileAt(2, 0, -5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTileOffTheBoardThrows() {
    model4.startGame();
    model4.getTileAt(5, 0, -5);
  }

  @Test
  public void testPass() {
    model5.startGame();
    String expectedRender =
            "    _ _ _ _ _ \n"
                    + "   _ _ _ _ _ _ \n"
                    + "  _ _ _ _ _ _ _ \n"
                    + " _ _ _ O X _ _ _ \n"
                    + "_ _ _ X _ O _ _ _ \n"
                    + " _ _ _ O X _ _ _ \n"
                    + "  _ _ _ _ _ _ _ \n"
                    + "   _ _ _ _ _ _ \n"
                    + "    _ _ _ _ _ \n";
    assertEquals(view.textRender(), expectedRender);
    model5.pass();
    assertEquals(view.textRender(), expectedRender);
  }

  @Test (expected = IllegalStateException.class)
  public void testNonAdjacentPlacementThrows() {
    model4.startGame();
    model4.placeTile(3, 0, -3);
  }

  @Test (expected = IllegalStateException.class)
  public void testPlacingTileOnOtherTile() {
    model4.startGame();
    model4.placeTile(1, 0, -1);
  }

  @Test
  public void testTileDoesntFlipWhenEmpty() {
    assertEquals(tile.getFT(), FillType.EMPTY);
    tile.flipTile();
    assertEquals(tile.getFT(), FillType.EMPTY);
  }

  @Test
  public void testTileFlipsWhenNonEmpty() {
    assertEquals(tile.getFT(), FillType.EMPTY);
    tile.setFillType(FillType.WHITE);
    assertEquals(tile.getFT(), FillType.WHITE);
    tile.flipTile();
    assertEquals(tile.getFT(), FillType.BLACK);
    tile.flipTile();
    assertEquals(tile.getFT(), FillType.WHITE);
  }

  @Test
  public void testHasCoordsProperInput() {
    assertFalse(tile.hasCoords(1, -1, 0));
    assertTrue(tile.hasCoords(0, 0, 0));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testHasCoordsImproperInput() {
    assertFalse(tile.hasCoords(1, 1, 0));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testTileConstructorException() {
    Tile newTile = new HexTile(1, 1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testReversiModelStartGameWithTooSmallRadiusThrowsError() {
    HexReversiModel model = new HexReversiModel(1);
  }

  @Test (expected = IllegalStateException.class)
  public void testReversiModelStartGameAlreadyStartedThrowsError() {
    model5.startGame();
    model5.startGame();
  }

  @Test
  public void testIsGameOverPassTwice() {
    model4.startGame();
    assertFalse(model4.isGameOver());
    model4.pass();
    assertFalse(model4.isGameOver());
    model4.placeTile(2, -1, -1);
    assertFalse(model4.isGameOver());
    model4.pass();
    assertFalse(model4.isGameOver());
    model4.pass();
    assertTrue(model4.isGameOver());
  }

  @Test
  public void testIsGameOverNoMoves() {
    model3.startGame();
    model3.placeTile(-1, 2, -1);
    model3.placeTile(-2, 1, 1);
    model3.placeTile(1, 1, -2);
    model3.placeTile(2, -1, -1);
    model3.placeTile(1, -2, 1);
    model3.pass();
    assertEquals(false, model3.isGameOver());
    model3.placeTile(-1, -1, 2);
    assertEquals(true, model3.isGameOver());
    assertEquals(10, model3.getScore(PlayerTurn.WHITE));
    assertEquals(2, model3.getScore(PlayerTurn.BLACK));
  }

  @Test
  public void testGetScore() {
    model3.startGame();
    assertEquals(model3.getScore(PlayerTurn.BLACK), 3);
    assertEquals(model3.getScore(PlayerTurn.WHITE), 3);
    model3.placeTile(-1, 2, -1);
    model3.placeTile(-2, 1, 1);
    assertEquals(model3.getScore(PlayerTurn.BLACK), 4);
    assertEquals(model3.getScore(PlayerTurn.WHITE), 4);
  }

  @Test
  public void testTileFlips() {
    Tile black = new HexTile(0,0,0);
    black.setFillType(FillType.BLACK);
    Tile white = new HexTile(0, 0, 0);
    white.setFillType(FillType.WHITE);
    Tile empty = new HexTile(0, 0, 0);
    assertEquals(black.getFT(), FillType.BLACK);
    assertEquals(white.getFT(), FillType.WHITE);
    assertEquals(empty.getFT(), FillType.EMPTY);
    black.flipTile();
    white.flipTile();
    empty.flipTile();
    assertEquals(black.getFT(), FillType.WHITE);
    assertEquals(white.getFT(), FillType.BLACK);
    assertEquals(empty.getFT(), FillType.EMPTY);
  }

  @Test
  public void testGetCopyDoesNotMutateOriginal() {
    model4.startGame();
    IReversiModel reversiCopy = model4.getCopy();
    assertEquals(3, reversiCopy.getScore(PlayerTurn.BLACK));
    reversiCopy.placeTile(-1, 2, -1);
    assertEquals(2, reversiCopy.getScore(PlayerTurn.BLACK));
    assertEquals(3, model4.getScore(PlayerTurn.BLACK));
  }
}
