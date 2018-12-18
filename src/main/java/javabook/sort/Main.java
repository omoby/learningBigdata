package javabook.sort;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String str = input.next();
            Map<Character, Integer> set = new LinkedHashMap<>();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (set.containsKey(c)) {
                    set.put(c, set.get(c) + 1);
                } else {
                    set.put(c, 1);
                }
            }
            for (Character c : set.keySet()){
                System.out.println(c + " "+ set.get(c));
            }

        }

    }
}
