import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Poker {
    /*2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A
    * 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
    * 不满足规则返回[0]
    */

    public String judgeWhoWin(String black, String white) {
        String[] blackcards = black.split(" ");
        String[] whitecards = white.split(" ");
        String result;

        result = judge(straightFlush(blackcards), straightFlush(whitecards));
        if(!"".equals(result)) return result;

        result = judge(flush(blackcards), flush(whitecards));
        if(!"".equals(result)) return result;

        result = judge(straight(blackcards), straight(whitecards));
        if(!"".equals(result)) return result;

        result = judge(threeCard(blackcards), threeCard(whitecards));
        if(!"".equals(result)) return result;

        result = judge(twoPair(blackcards), twoPair(whitecards));
        if(!"".equals(result)) return result;

        result = judge(pair(blackcards), pair(whitecards));
        if(!"".equals(result)) return result;

        return judge(highCard(blackcards), highCard(whitecards));

    }

    private String judge(int[] blackValue, int[] whiteValue) { //比较
        if(blackValue[0] == 0 && whiteValue[0] == 0)
            return "";
        if(whiteValue[0] == 0)
            return "Black wins";
        if(blackValue[0] == 0)
            return "White wins";

        int len = blackValue.length;
        for (int i=0;i<len; i++) {
            if(blackValue[i] > whiteValue[i])
                return "Black wins";
            if(blackValue[i] < whiteValue[i])
                return "White wins";
        }
        return "Tie";
    }


    private int[] straightFlush(String[] cardsList) { //同花顺
        int[] value = new int[5];
        int i;

        value[0] = valueCharToInt(cardsList[0].charAt(0));
        char suit = cardsList[0].charAt(1);

        for (i=1; i<5; i++) {
            if (suit != cardsList[i].charAt(1))
                return new int[]{0};
            value[i] = valueCharToInt(cardsList[i].charAt(0));
        }

        Arrays.sort(value);

        for (i=0; i<4; i++) {
            if (value[i]+1 != value[i+1])
                return new int[]{0};
        }

        return new int[]{value[4]};
    }

    private int[] flush(String[] cardsList) { //同花
        int[] value = new int[5];
        int tmp, i;

        value[0] = valueCharToInt(cardsList[0].charAt(0));
        char suit = cardsList[0].charAt(1);

        for (i=1; i<5; i++) {
            if (suit != cardsList[i].charAt(1))
                return new int[]{0};
            value[i] = valueCharToInt(cardsList[i].charAt(0));
        }

        Arrays.sort(value);

        for (i=0; i<2; i++) {//降序
            tmp = value[i];
            value[i] = value[4-i];
            value[4-i] = tmp;
        }

        return value;
    }

    private int[] straight(String[] cardsList) { //顺子
        int[] value = new int[5];
        int i;

        for (i=0; i<5; i++) {
            value[i] = valueCharToInt(cardsList[i].charAt(0));
        }

        Arrays.sort(value);

        for (i=0; i<4; i++) {
            if (value[i]+1 != value[i+1])
                return new int[]{0};
        }

        return new int[]{value[4]};
    }

    private int[] threeCard(String[] cardsList) { //三条
        Map<Integer, Integer> valueMap=new HashMap<>();
        int[] value = new int[3];
        int tmp, i;

        for (i=0; i<5; i++) {
            tmp = valueCharToInt(cardsList[i].charAt(0));
            if(valueMap.get(tmp) == null)
                valueMap.put(tmp, 1);
            else valueMap.put(tmp, valueMap.get(tmp) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : valueMap.entrySet()) {
            switch (entry.getValue()) {
                case 3: //3+?
                    value[0] = entry.getKey();
                    valueMap.remove(entry.getKey());
                    if(valueMap.size()>1){ //3+1+1
                        i = 1;
                        for (Map.Entry<Integer, Integer> entry0 : valueMap.entrySet()) { //剩余两项
                            value[i] = entry0.getKey();
                            i++;
                        }

                        if(value[1] < value[2]){
                            tmp = value[1];
                            value[1] = value[2];
                            value[2] = tmp;
                        }
                        return value;
                    }
                    else { //3+2
                        for (Map.Entry<Integer, Integer> entry0 : valueMap.entrySet()) {//剩余一项
                            value[1] = entry0.getKey();
                            value[2] = value[1];
                            return value;
                        }
                    }

                case 4: //4+1
                    value[0] = entry.getKey();
                    valueMap.remove(entry.getKey());
                    for (Map.Entry<Integer, Integer> entry0 : valueMap.entrySet()) { //剩余一项
                        if(entry0.getKey()>value[0]){
                            value[1] = entry0.getKey();
                            value[2] = value[0];
                        }
                        else {
                            value[1] = value[0];
                            value[2] = entry0.getKey();
                        }

                        return value;
                    }

                case 5: //5
                    Arrays.fill(value,entry.getKey());
                    return value;
            }
        }

        return new int[]{0};
    }

    private int[] twoPair(String[] cardsList) { //两对
        Map<Integer, Integer> valueMap=new HashMap<>();
        int[] value = new int[3];
        int tmp, i;

        for (i=0; i<5; i++) {
            tmp = valueCharToInt(cardsList[i].charAt(0));
            if(valueMap.get(tmp) == null)
                valueMap.put(tmp, 1);
            else valueMap.put(tmp, valueMap.get(tmp) + 1);
        }

        //只有3+1+1和2+2+1长度为3，两对只可能为2+2+1，而3+1+1为三条，不可能执行到此处
        if(valueMap.size() != 3) return new int[]{0};

        i=0;
        for (Map.Entry<Integer, Integer> entry : valueMap.entrySet()) { //2+2+1
            if(entry.getValue() == 2) {
                value[i] = entry.getKey();
                i++;
            }
            else
                value[2] = entry.getKey();
        }

        if(value[1] < value[2]){
            tmp = value[1];
            value[1] = value[2];
            value[2] = tmp;
        }
        return value;
    }

    private int[] pair(String[] cardsList) { //对子
        Map<Integer, Integer> valueMap=new HashMap<>();
        int[] value = new int[4];
        int tmp, i;
        Integer doubleValue = 0;

        for (i=0; i<5; i++) {
            tmp = valueCharToInt(cardsList[i].charAt(0));
            if(valueMap.get(tmp) == null)
                valueMap.put(tmp, 1);
            else {
                valueMap.put(tmp, valueMap.get(tmp) + 1);
            }
        }

        //只有2+1+1+1长度为4
        if(valueMap.size() != 4) return new int[]{0};

        i = 0;
        for (Map.Entry<Integer, Integer> entry : valueMap.entrySet()) { //2+1+1+1
            if(entry.getValue() == 1) {
                value[i] = entry.getKey();
                i++;
            }
            else
                doubleValue = entry.getKey();
        }

        Arrays.sort(value);

        tmp = value[1];
        value[1] = value[3];
        value[3] = tmp;

        value[0] = doubleValue;

        return value;
    }

    private int[] highCard(String[] cardsList) { //散牌
        int[] value = new int[5];
        int tmp, i;

        for (i=0; i<5; i++) {
            value[i] = valueCharToInt(cardsList[i].charAt(0));
        }

        Arrays.sort(value);

        for (i=0; i<2; i++) {//降序
            tmp = value[i];
            value[i] = value[4-i];
            value[4-i] = tmp;
        }

        return value;
    }

    private int valueCharToInt(char valueChar) { //转值
        if(valueChar <= '9')
            return valueChar-'0';
        if(valueChar == 'T')
            return  10;
        if(valueChar == 'J')
            return  11;
        if(valueChar == 'Q')
            return  12;
        if(valueChar == 'K')
            return  13;
        return  14;
    }
}
