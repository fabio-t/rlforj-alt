package rlforj.util;

public class StringUtil
{

    public static String times(String x, int times)
    {
        StringBuilder b = new StringBuilder(x.length() * times);
        for (int i = 0; i < times; i++)
            b.append(x);

        return b.toString();
    }
}
