package org.ztuit;

import org.junit.Test;

public class EuclideanTest {

    @Test
    public void testExtendedGCD() {
        long[] result = org.ztuit.rsa.Euclidean.extendedGCD(5, 48);
        assert(result[0] == 1);
        assert(result[1] == -3);
        assert(result[2] == 2);
    }
}
