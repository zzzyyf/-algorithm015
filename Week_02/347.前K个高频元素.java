/*
 * @lc app=leetcode.cn id=347 lang=java
 *
 * [347] 前 K 个高频元素
 */

// @lc code=start
import java.util.*;
class Solution {
    /**
     * Hash+桶排序
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * @param nums
     * @param k
     * @return
     */
    public int[] topKFrequent(int[] nums, int k) {
        // 暴力：Hash统计出现次数，然后找出前k高的
        // 优化：使用桶排序法，直接以出现频率为下标，倒序输出后k个即可
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer>[] frequent = new List[nums.length+1];
        // 把所有元素的频率添加到hashMap
        for(int i=0; i<nums.length; i++){
            Integer n = map.get(nums[i]);
            if(n==null){
                map.put(nums[i], 1);
            }else{
                map.put(nums[i], n+1);
            }
        }
        // 桶排序，注意考虑相同频率元素的存放
        for(int i: map.keySet()){
            List<Integer> tmp = frequent[map.get(i)];
            if(tmp==null){
                tmp = new ArrayList<>();
                tmp.add(i);
                frequent[map.get(i)] = tmp;
            }else{
                tmp.add(i);
            }
        }
        int cnt = 0;
        List<Integer> ans = new ArrayList<>();
        for(int i=nums.length; i>=0 && cnt<k; i--){
            List<Integer> tmp = frequent[i];
            if(tmp!=null) {
                ans.addAll(tmp);
                cnt+=tmp.size();
            }
        }
        return ans.stream().mapToInt(Integer::valueOf).toArray();
    }
}
// @lc code=end
