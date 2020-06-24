import org.junit.Assert;
import org.junit.Test;

public class PokerTest {

    @Test
    public void WhiteWinsTestInput1 () {
        Poker poker = new Poker();
        String result = poker.judgeWhoWin("2H 3D 5S 9C KD","2C 3H 4S 8C AH");
        Assert.assertEquals("White wins", result);
    }

    @Test
    public void WhiteWinsTestInput2 () {
        Poker poker = new Poker();
        String result = poker.judgeWhoWin("2H 4S 4C 2D 4H","2S 8S AS QS 3S");
        Assert.assertEquals("White wins", result);
    }

    @Test
    public void BlackWinsTestInput3 () {
        Poker poker = new Poker();
        String result = poker.judgeWhoWin("2H 3H 5H 9H KH","2C 3H 4S 5C 6H");
        Assert.assertEquals("Black wins", result);
    }

    @Test
    public void TieTestInput4 () {
        Poker poker = new Poker();
        String result = poker.judgeWhoWin("2H 3D 5S 9C KD","2D 3H 5C 9S KH");
        Assert.assertEquals("Tie", result);
    }
}
