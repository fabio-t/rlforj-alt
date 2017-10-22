package rlforj.util;

import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class GaussianKernel
{
    public static ConvolveOp getGaussianBlurFilter1D(int radius, boolean horizontal)
    {
        if (radius < 1)
        {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int     size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma          = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot      = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total          = 0.0f;

        for (int i = -radius; i <= radius; i++)
        {
            float distance = i * i;
            int   index    = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++)
        {
            data[i] /= total;
        }

        Kernel kernel = null;
        if (horizontal)
        {
            kernel = new Kernel(size, 1, data);
        }
        else
        {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    public static ConvolveOp[] getGaussianBlurFilter1DPair(int radius)
    {
        ConvolveOp op = getGaussianBlurFilter1D(radius, true);

        // Copy of data, but faster than recalculating
        float[] data = op.getKernel().getKernelData(null);

        ConvolveOp op1 = new ConvolveOp(new Kernel(1, op.getKernel().getWidth(), data), ConvolveOp.EDGE_NO_OP, null);

        return new ConvolveOp[] { op, op1 };
    }
}
