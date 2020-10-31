class Solution {
    public int lengthOfLIS(int[] nums) {
        // 和LCS类似，也是从左到右看，因此左边是分治的子问题。
        // 可以对每个位置记录LIS长和LIS的末尾值，这样在看后一个的时候可以直接通过该位的LIS和末尾值判断出新的LIS
        int[] lens = new int[nums.length];
        if(nums.length<1) return 0;
        for(int i=0; i<nums.length; i++){
            // 对每一位，首先倒推前面的所有位，获取LIS，然后再设置自己的LIS
            int maxlen = 1;
            for(int j=i-1; j>=0; j--){
                if(nums[j]<nums[i] && maxlen < lens[j]+1)
                    maxlen = lens[j]+1;
            }
            lens[i] = maxlen;
        }
        int maxlen = 1;
        for(int i=nums.length-1; i>=0; i--){
            maxlen = Math.max(maxlen, lens[i]);
        }
        return maxlen;
    }
}