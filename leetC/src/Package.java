/**
 * Created by renshuangjun_sx on 2016/8/29.
 */
public class Package {

    public static void main(String[] args) {
        int capacity = 10;
        int[][] goods = {{2,6},{2,3},{6,10},{4,4},{5,6}};
        System.out.println(bigestValue(capacity,goods));
    }

    public static int bigestValue(int capacity,int[][] goods) {
        int n = goods.length;
        int bigestValues[][] = new int[n][capacity+1];
        for(int j = 0; j <= capacity; j ++) {
            if(j >= goods[0][0]) {
                bigestValues[0][j] = goods[0][1];
            }
        }
        for(int i = 1; i < goods.length; i ++ ) {
            for(int j = 1 ; j <= capacity; j ++) {
                if((j - goods[i][0]) > 0) {
                    bigestValues[i][j] = bigestValues[i-1][j] > (bigestValues[i-1][j - goods[i][0]] + goods[i][1])
                        ? bigestValues[i-1][j] : bigestValues[i-1][j - goods[i][0]] + goods[i][1];
                } else {
                    bigestValues[i][j] = bigestValues[i-1][j];
                }
            }
        }
        return bigestValues[n-1][capacity];
    }
}
