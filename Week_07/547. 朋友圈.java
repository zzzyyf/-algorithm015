import java.util.*;

class Solution {
    int[] ancestors;
    public int findCircleNum(int[][] M) {
        int n = M.length;
        int cnt = 0;
        // 两人是朋友时，这两人的朋友圈合并
        // 一开始所有人的朋友圈只有自己
        // 每次新增朋友，都把两人的朋友圈合并，可以让一个朋友圈的祖先把另一个朋友圈的祖先变为儿子
        // 最后统计不同的祖先数目
        ancestors = new int[n];
        for(int i=0; i<n; i++){
            ancestors[i] = i;
        }
        for(int y=0; y<n; y++){
            for(int x=y; x<n; x++){
                if(M[y][x]==1){
                    ancestors[find(y)] = find(x);
                }
            }
        }
        for(int i=0; i<n; i++){
            if(ancestors[i]==i) cnt++;
        }
        return cnt;
    }

    private int find(int index){
        if(ancestors[index]!=index){
            ancestors[index] = find(ancestors[index]);
        }
        return ancestors[index];        
    }
}