/*
 * @lc app=leetcode.cn id=77 lang=java
 *
 * [77] 组合
 */

// @lc code=start
import java.util.*;
class Solution {
    private List<Integer> tmp;
    private int n;
    private int k;
    private List<List<Integer>> ans = new ArrayList<>();
    public List<List<Integer>> combine(int n, int k) {
        // 注意是无顺序，因此需要注意顺序。本次采取从小到大的顺序
        // 需要按照一定的顺序，比如1234，1235,1236，然后1345,1346,1356，然后1456，然后2345,
        // 感觉是分治思想，每次排除一个数。
        // 第一个数可以是[1,n-k+1]，依次类推，第i个数可以是[i,n-k+i]。
        this.n = n;
        this.k = k;
        tmp = new ArrayList<>(k);
        helper(new boolean[n+1], 1, -1);
        return ans;
    }

    // 根据lvl选择[lvl, n-k+lvl]中的每个未被选择的数作为第lvl个数。
    // 注意还要遵循每次选择更大的数的规则
    private void helper(boolean[] selection, int lvl, int last){
        for(int i=Math.max(lvl, last+1); i<=n-k+lvl; i++){
            if(selection[i]) continue;
            tmp.add(i);
            if(lvl==k) {
                ans.add(new ArrayList<>(tmp));
                tmp.remove(tmp.size()-1);
                continue;
            }
            selection[i] = true;
            helper(selection, lvl+1, i);
            selection[i] = false;
            tmp.remove(tmp.size()-1);
        }
    }
}
// @lc code=end