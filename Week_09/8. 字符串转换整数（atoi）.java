class Solution {
    public static int myAtoi(String str) {
        final long INT_MAX = (1l<<31) -1;
        final long INT_MIN = -(1l<<31);
        boolean isConverting = false;
        boolean isPositive = true;
        long num = 0;
        for(int i=0; i<str.length();i++){
            char c = str.charAt(i);
            if(!isConverting){
                if(c==' ') continue;
                else if(c=='+' || c=='-'){
                    isConverting = true;
                    if(c=='-') isPositive = false;
                    continue;
                }else if(c>='0' && c<='9'){
                    isConverting = true;
                    num+=c-'0';
                    continue;
                }else return 0;
            }else{
                // 持续识别数字字符并组合，并检查是否超出范围
                if(c>='0' && c<='9'){
                    num*=10;
                    num+=c-'0';
                    long temp = num*(isPositive?1:-1);
                    if(temp>INT_MAX) return (int)INT_MAX;
                    if(temp<INT_MIN) return (int)INT_MIN;
                    continue;
                }else{
                    return (int)(num*(isPositive?1:-1));
                }
            }
        }
        return (int)(num*(isPositive?1:-1));
    }
}