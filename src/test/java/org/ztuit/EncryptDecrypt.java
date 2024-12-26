package org.ztuit;

import org.junit.Test;
import org.ztuit.rsa.RsaDetails;
import org.ztuit.rsa.RsaService;

import java.math.BigInteger;
import java.util.Random;

public class EncryptDecrypt {

    RsaService rsaService = new RsaService();

    @Test
    public void knownPrimes() throws Exception {
        RsaDetails rsaDetails = rsaService.build(7L, 13L);
        long original = 42;
        long encoded = rsaService.encrypt(rsaDetails, original);
        long decoded = rsaService.decrypt(rsaDetails, encoded);
        assert(original==decoded);
    }

    @Test
    public void largerPrimeDifference() throws Exception {
        RsaDetails rsaDetails = rsaService.build(89L, 281L);
        long original = 9727;
        long encoded = rsaService.encrypt(rsaDetails, original);
        long decoded = rsaService.decrypt(rsaDetails, encoded);
        assert(original==decoded);
    }

    @Test
    public void largerSecret() throws Exception {
        RsaDetails rsaDetails = rsaService.build(449L, 	1013L);
        long original = 223517;
        long encoded = rsaService.encrypt(rsaDetails, original);
        long decoded = rsaService.decrypt(rsaDetails, encoded);
        assert(original==decoded);
    }

    //write a test that test two larger prime numbers
    @Test
    public void largerPrimes() throws Exception {
        long prime1 = BigInteger.probablePrime(10, new Random()).longValue();
        long prime2 = BigInteger.probablePrime(16, new Random()).longValue();
        RsaDetails rsaDetails = rsaService.build((long) prime1, (long) prime2);
        long original = 7455;
        long encoded = rsaService.encrypt(rsaDetails, original);
        long decoded = rsaService.decrypt(rsaDetails, encoded);
        assert(original==decoded);
    }

}
