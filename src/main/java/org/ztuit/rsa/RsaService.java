package org.ztuit.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RsaService {

    private PrimeService primeService;

    public RsaService(){
        primeService =  new PrimeService();
    }

    public RsaDetails build() throws Exception {
        Long prime1 = this.primeService.randomPrime();

        Long prime2 = this.primeService.randomPrime();


        return this.build(prime1,prime2);
    }

    public RsaDetails build(Long prime1, Long prime2) throws Exception {


        long pubModulo = prime1*prime2;

        long pModulo = 0;
        long encryptKey = 0;
        long decryptKey = -1;

        pModulo = (prime1-1)*(prime2-1);
        encryptKey = nonEqualDivisorPrime(pModulo);
        decryptKey = this.extended_euclidean(encryptKey,pModulo);
        return new RsaDetails(pubModulo,pModulo,encryptKey,decryptKey);
    }

    /**
     * Encryption here is only using the public parts of the key.
     * @param rsaDetails
     * @param toEncrypt
     * @return
     */
    public long encrypt(RsaDetails rsaDetails, long toEncrypt){
        BigInteger original = new BigInteger(String.valueOf(toEncrypt));
        BigInteger opow = original.pow((int) rsaDetails.encryptionKey());
        long encoded = opow.mod(new BigInteger(String.valueOf(rsaDetails.publicModulus()))).longValue();
        return encoded;
    }

    public long decrypt(RsaDetails rsaDetails, long toDecrypt){
        BigInteger b = new BigInteger(String.valueOf(toDecrypt));

        BigInteger dPpw = b.pow((int) rsaDetails.decryptionKey());
        long decoded = dPpw.mod(new BigInteger(String.valueOf(rsaDetails.publicModulus()))).longValue();
        return decoded;
    }

    private long nonEqualDivisorPrime(long pModulo) {
        Optional<Long> result = this.primeService.getPrimes().stream().filter(e -> ( pModulo % e) != 0).findFirst();
        return  result.get();
    }

    //bernouli's identifity: for a pair of coprime numbers then Av + Bw = 1
    private long extended_euclidean(long encyptionKey, long modulus) throws Exception {
        long gcdV = gcd(encyptionKey, modulus);

        if(gcdV==1)
            System.out.println("---COPRIME PAIR---" +  encyptionKey + "," + modulus);


        List<Long> quotidienList = new ArrayList<>();
        List<Long> remainderList = new ArrayList<>();
        List<Long> SList = new ArrayList<>();
        List<Long> TList = new ArrayList<>();

        //Set 0 elements
        remainderList.add(modulus);
        quotidienList.add(0L);
        SList.add(1L);
        TList.add(0L);
        //1 elements
        remainderList.add(encyptionKey);
        quotidienList.add(0L);
        SList.add(0L);
        TList.add(1L);

        //Now do lock step calculations
        int i = 1;
        boolean nonZeroRemainder = true;
        while(nonZeroRemainder){
            i++;
            long qi = (remainderList.get(i-2) / remainderList.get(i-1));
            var ri = remainderList.get(i-2) - qi * remainderList.get(i-1);
            var si = SList.get(i-2) - qi * SList.get(i-1);
            var ti = TList.get(i-2) - qi * TList.get(i-1);
            quotidienList.add(qi);
            remainderList.add(ri);
            SList.add(si);
            TList.add(ti);
            if(ri==0) nonZeroRemainder = false;
        }
        var ti = TList.get(i-1);
        var si  = SList.get(i-1);

        if(ti<0) {
            ti = reverseCoefficients(encyptionKey, modulus, ti, si);
            si = si-encyptionKey;
        }

        if((encyptionKey*ti+si*modulus) != 1)
            throw new Exception("identity does not hold");

        return ti;
    }

    private Long reverseCoefficients(long encyptionKey, long modulus, Long ti, Long si) {

        return ti+modulus;
    }

    private long gcd(long divisor, long divend) {

             long remainder =  divend % divisor;
             if(remainder==0){
                 return divisor;
             }
             return gcd(remainder, divisor);
    }



    public static void main(String args[]) throws Exception {
        RsaService rsaService = new RsaService();


        rsaService.extended_euclidean(5,288);

        for(int i  = 0; i < 10; i++) {
            System.out.println("\n\n\n====================================\n\n");
            RsaDetails rsaDetails = rsaService.build();
            long original = 18;
            long encoded = rsaService.encrypt(rsaDetails, 18);
            long decoded = rsaService.decrypt(rsaDetails, encoded);

            System.out.println(" public mod key :" + rsaDetails.publicModulus());
            System.out.println(" decryption key :" + rsaDetails.decryptionKey());
            System.out.println(" Original " + original);
            System.out.println(" Encoded " + encoded);
            System.out.println(" Decoded " + decoded);
            if (decoded != original) {
                System.out.println("Decoding failed");
            } else {
                System.out.println("Decoding succeeded");
            }
        }


    }


}
