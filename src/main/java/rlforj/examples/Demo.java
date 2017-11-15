/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.examples;

public class Demo
{
    public static void main(final String[] args)
    {
        System.out.println("Field of Vision Example \n");
        FovExample.main(new String[0]);
        System.out.println("Cone Field of Vision Example \n");
        ConeFovExample.main(new String[0]);
        System.out.println("Line of Sight and Projection Example \n");
        LosExample.main(new String[0]);
    }
}
