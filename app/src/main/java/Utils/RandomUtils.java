package Utils;

import java.util.Random;

/**
 * Created by admin on 2017/5/26.
 */
public class RandomUtils {

    public static int getrandom(){
        Random r = new Random();
        int i1 = (r.nextInt(99999-10000) + 10000);
        return i1;
    }

    public static int gettowrandom(){
        Random r = new Random();
        int i1 = (r.nextInt(99-10) + 10);
        return i1;
    }
}
