package rlforj.math;

public class SimpleMatrix2D
{
    public double[][] data;

    public SimpleMatrix2D(int x, int y)
    {
        data = new double[x][y];
    }

    public SimpleMatrix2D(double[][] d)
    {
        data = d;
    }

    public void addToThis(SimpleMatrix2D m)
    {
        if (data == null || m.data == null)
            return;
        if (data.length != m.data.length)
            throw new IllegalArgumentException("Matrix size mismatch in matrix add");
        if (data[0].length != m.data[0].length)
            throw new IllegalArgumentException("Matrix size mismatch in matrix add");

        final int x = data.length, y = data[0].length;
        for (int i = 0; i < x; i++)
            for (int j = 0; j < y; j++)
                data[i][j] += m.data[i][j];
    }

    public void normalize()
    {
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;

        final int width = data.length, height = data[0].length;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                if (data[i][j] < min)
                    min = data[i][j];
                if (data[i][j] > max)
                    max = data[i][j];
            }

        //normalize
        double range = max - min;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                data[i][j] = (data[i][j] - min) / range;
                if (data[i][j] > 1 || data[i][j] < 0)
                    System.out.println("Odd normalized value " + data);
            }
    }
}
