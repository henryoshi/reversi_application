import org.junit.Before;
import org.junit.Test;

import model.FillType;
import model.IReversiModel;
import model.PlayerTurn;
import model.SquareReversiModel;
import model.SquareTile;
import view.SquareReversiTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testing class to test Square-tile Reversi Model.
 */
public class TestSquareReversiModel {
  SquareReversiModel model4;
  SquareReversiTextualView view;
  SquareTile tile;

  @Before
  public void init() {
    model4 = new SquareReversiModel(4);
    view = new SquareReversiTextualView(model4);
    tile = new SquareTile(0, 1);
  }

  @Test
  public void testTextualViewRender() {
    model4.startGame();
    String expectedRender = "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ X O _ _ _ \n" +
            "_ _ _ O X _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n";
    assertEquals(expectedRender, view.textRender());
    model4.placeTile(4, 2, 0);
    expectedRender = "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ X _ _ _ \n" +
            "_ _ _ X X _ _ _ \n" +
            "_ _ _ O X _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n";
    assertEquals(expectedRender, view.textRender());
    model4.placeTile(5, 2, 0);
    expectedRender = "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ X O _ _ \n" +
            "_ _ _ X O _ _ _ \n" +
            "_ _ _ O X _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n";
    assertEquals(expectedRender, view.textRender());
  }

  @Test
  public void testFlippingValidMoves() {
    model4.startGame();
    model4.placeTile(4, 2, 0);
    model4.placeTile(5, 2, 0);
    assertEquals(FillType.WHITE, model4.getTileAt(4, 2, 0).getFT());
    assertEquals(FillType.BLACK, model4.getTileAt(5, 2, 0).getFT());
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
    assertEquals(FillType.WHITE, model4.getTileAt(3, 3, 0).getFT());
    assertEquals(FillType.BLACK, model4.getTileAt(4, 3, 0).getFT());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTileOffTheBoardThrows() {
    model4.startGame();
    model4.getTileAt(5, 10, 0);
  }

  @Test
  public void testPass() {
    model4.startGame();
    String expectedRender = "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ X O _ _ _ \n" +
            "_ _ _ O X _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n" +
            "_ _ _ _ _ _ _ _ \n";
    assertEquals(view.textRender(), expectedRender);
    model4.pass();
    assertEquals(view.textRender(), expectedRender);
  }

  @Test (expected = IllegalStateException.class)
  public void testPlacingTileOnOtherTile() {
    model4.startGame();
    model4.placeTile(4, 4, 0);
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
    assertFalse(tile.hasCoords(3, 4, 0));
    assertTrue(tile.hasCoords(0, 1, 0));
    assertTrue(tile.hasCoords(0, 1, 1));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testReversiModelStartGameWithOddRadius() {
    SquareReversiModel model = new SquareReversiModel(1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testReversiModelStartGameWithTooSmallRadius() {
    SquareReversiModel model = new SquareReversiModel(1);
  }

  @Test (expected = IllegalStateException.class)
  public void testReversiModelStartGameAlreadyStartedThrowsError() {
    model4.startGame();
    model4.startGame();
  }

  @Test
  public void testIsGameOverPassTwice() {
    model4.startGame();
    assertFalse(model4.isGameOver());
    model4.placeTile(4, 2, 0);
    assertFalse(model4.isGameOver());
    model4.pass();
    assertFalse(model4.isGameOver());
    model4.pass();
    assertTrue(model4.isGameOver());
  }

  @Test
  public void testGetScore() {
    model4.startGame();
    assertEquals(model4.getScore(PlayerTurn.BLACK), 2);
    assertEquals(model4.getScore(PlayerTurn.WHITE), 2);
    model4.placeTile(4, 2, 0);
    model4.placeTile(5, 2, 0);
    assertEquals(model4.getScore(PlayerTurn.BLACK), 3);
    assertEquals(model4.getScore(PlayerTurn.WHITE), 3);
  }

  @Test
  public void testTileFlips() {
    SquareTile black = new SquareTile(0,0);
    black.setFillType(FillType.BLACK);
    SquareTile white = new SquareTile(0, 0);
    white.setFillType(FillType.WHITE);
    SquareTile empty = new SquareTile(0, 0);
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
    assertEquals(2, reversiCopy.getScore(PlayerTurn.WHITE));
    reversiCopy.placeTile(4, 2, 0);
    assertEquals(4, reversiCopy.getScore(PlayerTurn.WHITE));
    assertEquals(2, model4.getScore(PlayerTurn.WHITE));
  }
}
