import java.util.*;

class Solution {
    public int maxProfit(int[] prices) {
        // 第一天先模拟买入，之后若价低则取消之前的买入，买入低价的；若价高则卖出手中股票，再模拟买入当前高价的
        // 最后以最后一天的价格卖出手中全部股票
        int last = prices[0];
        int sum = -last;
        for(int i=1; i<prices.length; i++){
            if(prices[i]<last){
                // 撤销上一笔
                sum+=last;
            }else{
                // 上一笔生效
                sum+=prices[i];
            }
            // 先买进这一笔
            last = prices[i];
            sum -= last;
        }
        return sum+last;
    }
}