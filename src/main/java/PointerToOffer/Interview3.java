package PointerToOffer;

/**
 * FileName: Interview3
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-1 下午4:03
 * Description:
 */
public class Interview3 {
    public static void main(String[] args){
    int[][] arr = {{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}};
    boolean flag = Solution.Find(7,arr);
    System.out.println(flag);
    }
}

 class Solution{
    public static boolean Find(int target, int[][] array){
        int rows = array.length;
        int colums = array[0].length;
        int row = 0;
        int colum = colums-1;
        while (row < rows && colum >=0){
            if (array[row][colum] == target){
                return true;
            }else if (array[row][colum] > target){
                colum--;
            }else {
                row++;
            }
        }
        return false;
    }

}
