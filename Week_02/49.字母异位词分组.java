/*
 * @lc app=leetcode.cn id=49 lang=java
 *
 * [49] 字母异位词分组
 */

// @lc code=start
import java.util.*;


class Solution {
    /**
     * 做法1：使用Hash来识别异位词
     * 时间复杂度：O(N*L)，其中N为strs长度，L为strs内容的平均长度
     * 空间复杂度：O(N*L)
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        List<List<String>> ans = new ArrayList<>();
        // 遍历每个单词，若该单词与某个HashMap匹配，则加入其对应的列表；否则新建对应的HashMap和列表
        StringBuilder builder = new StringBuilder();
        for(String s: strs){
            int[] pattern = new int[26];
            for(int j=0; j<s.length(); j++){
                pattern[s.charAt(j)-'a']++;
            }
            // 把pattern转换为易于查找的String
            for(int j=0; j<26; j++){
                builder.append(pattern[j]).append("#");
            }
            String patternStr = builder.toString();
            builder.delete(0, builder.length());
            List<String> list = map.get(patternStr);
            if(list==null){
                list = new ArrayList<>();
                list.add(s);
                map.put(patternStr, list);
            }else{
                list.add(s);
            }
        }
        ans.addAll(map.values());
        return ans;
    }

    /**
     * 做法2：使用字符串排序识别异位词
     * 时间复杂度：O(N*L*logL)
     * 空间复杂度：O(N*L)
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams2(String[] strs) {
        String[] sorted = new String[strs.length];
        for(int i=0; i<strs.length; i++){
            String str = strs[i];
            char[] array = str.toCharArray();
            Arrays.sort(array);
            sorted[i] = new String(array);
        }
        List<List<String>> ans = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for(int i=0; i<strs.length; i++){
            List<String> tmp = map.get(sorted[i]);
            if(tmp==null){
                tmp = new ArrayList<>();
                tmp.add(strs[i]);
                map.put(sorted[i], tmp);
            }else 
                tmp.add(strs[i]);
        }
        for(String s: map.keySet()){

        }
        ans.addAll(map.values());
        return ans;
        
    }
}

// @lc code=end