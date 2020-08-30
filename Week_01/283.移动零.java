/*
 * @lc app=leetcode.cn id=283 lang=java
 *
 * [283] 移动零
 */

// @lc code=start
class Solution {
    public void moveZeroes(int[] nums) {
        int cnt = 0;
        int p=0;
        for(int i=0; i<nums.length; i++){
            if(nums[i]!=0){
                nums[p]=nums[i];
                if(p!=i) nums[i]=0;
                p++;
            }
        }
    }
}
// @lc code=end

