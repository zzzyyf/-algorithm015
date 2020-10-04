import java.util.*;
class Solution {
    public int minDistance(String word1, String word2) {
        // 一开始想到的是LCS最长公共子序列，但是对于"intention"和"execution"来说，e的匹配位置不同会导致不同的结果，匹配第2个e才是最优解。
        // 不过这个问题也符合DP，word1和word2每增加1个字母时，当增加的字母相同时编辑距离不变，否则编辑距离=min（原word1和新word2的距离 or 新word1和原word2的距离）+1
        // 字母和空串的距离始终为1
        int i=0, j=0;
        
        // 开始DP，所有情况为word1.length()*word2.length()
        // DP的每一个状态最优解要么与之前的最优解有关，要么其本身是一个单元最优解
        int[][] minDistances = new int[word1.length()+1][word2.length()+1];
        for(i=1;i<=word1.length();i++) minDistances[i][0]=i;
        for(j=1;j<=word2.length();j++) minDistances[0][j]=j;
        for(i=1;i<=word1.length();i++){
            for(j=1;j<=word2.length();j++){
                char c1=word1.charAt(i-1), c2=word2.charAt(j-1);
                if(c1==c2){
                    minDistances[i][j]=minDistances[i-1][j-1];
                }else{
                    minDistances[i][j]=Math.min(Math.min(minDistances[i-1][j], minDistances[i][j-1]), minDistances[i-1][j-1])+1;
                }
            }
        }
        
        return minDistances[word1.length()][word2.length()];
    }
}