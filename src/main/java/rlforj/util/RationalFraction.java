package rlforj.util;

public class RationalFraction
{
    int num, denom;

    public RationalFraction(int numerator, int denominator)
    {
        super();
        this.num = numerator;
        this.denom = denominator;
    }

    public void increment(RationalFraction other)
    {
        if (denom == other.denom)
            num += other.num;
        else
        {
            num = num * other.denom + other.num * denom;
            denom *= other.denom;
            // TODO : gcd
        }
    }

    public void multiplyBy(int i)
    {
        num *= i;
    }

    public int getIntPart()
    {
        return num / denom;
    }
}
