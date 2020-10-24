import java.util.*;

class Solution {
    public int[][] merge(int[][] intervals) {
        // 重叠是前者的结束>=后者的开始
        // 所以按照开始排序
        List<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (a, b) -> a[0]!=b[0] ? a[0]-b[0] : a[1]-b[1]);
        if(intervals.length<1) return intervals;
        int lastR = intervals[0][1];
        int lastL = intervals[0][0];
        for(int i=1; i<intervals.length; i++){
            if(lastR>=intervals[i][0]){
                lastR = Math.max(lastR, intervals[i][1]);
            }else{
                ans.add(new int[]{lastL, lastR});
                lastL = intervals[i][0];
                lastR = intervals[i][1];
            }
        }
        ans.add(new int[]{lastL, lastR});
        return ans.toArray(new int[0][]);
    }
}