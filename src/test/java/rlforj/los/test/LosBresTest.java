/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.los.test;

import rlforj.los.BresLos;

public class LosBresTest extends LosTest
{
    public LosBresTest()
    {
        a = new BresLos(false);
    }

    @Override
    protected boolean seesAroundPillar()
    {
        return false;
    }
}
