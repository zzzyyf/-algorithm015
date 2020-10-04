import java.util.*;
class Solution {
    public int countSubstrings(String s) {
        // 根据起始位置和长度来确定所有回文子串
        // 回文子串的左右两端添加相同字母后仍为回文子串
        // 因此可以采取长度递增的自底向上DP？
        // 分长为奇数和偶数两种，而且这两种情况互相独立，可以分开计算
        boolean[][] dp = new boolean[s.length()][s.length()+1];
        int cnt = 0;
        
        for(int i=0; i<s.length(); i++){
            dp[i][1]=true;
            cnt++;
        }
        for(int i=0; i+1<s.length(); i++){
            if(s.charAt(i)==s.charAt(i+1)) {
                dp[i][2] = true;
                cnt++;
            }
        }
        for(int len=3; len<=s.length(); len++){
            for(int i=0; i+len-1<s.length(); i++){
                // 先得到内部子串，然后看最外边的两字符是否相等
                if(dp[i+1][len-2] && s.charAt(i)==s.charAt(i+len-1)){
                    dp[i][len] = true;
                    cnt++;
                }
            }
        }
        return cnt;

    }
}
