/**
 * Created by renshuangjun_sx on 2016/8/29.
 */
public class LongestPalindromeDP {

    public static void main(String[] args) {
        System.out.println(longestPalindromeDP("iqiyiyiqi"));
    }

    public static String longestPalindromeDP(String s) {
        char[] chars = s.toCharArray();
        int n = s.length();
        int longestStart = 0;
        int longestEnd = 0;
        int longestLen = 0;
        boolean[][] table = new boolean[n][n];
        for(int i = 0; i < n; i ++ ) {
            table[i][i] = true;
        }
        for(int i = 0; i < n -1; i ++) {
            if(chars[i] == chars[i+1]) {
                table[i][i+1] = true;
                longestStart = i;
                longestEnd = i + 1;
                longestLen = 2;
            }
        }
        for(int len= 3; len <= n; len ++) {
            for(int i = 0; i < n - len + 1; i ++) {
                int j = i + len - 1;
                if((chars[i] == chars[j]) && table[i+1][j-1]) {
                    table[i][j] = true;
                    longestStart = i;
                    longestEnd = j;
                    longestLen = len;
                }
            }
        }

       return s.substring(longestStart,longestEnd +1);
    }
}
