package org.ztuit.rsa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimeService {

    private List<Long> primeNumbers = new ArrayList<>();
    private long upperLimit = 40;
    private long firstPrime = 2;

    private int numberOfPrimes = 10;

    public PrimeService() {
        generatePrimes();
    }

    public PrimeService(long lowerLimit, int numberOfPrimes){
        this.numberOfPrimes = numberOfPrimes;
        this.firstPrime = lowerLimit;
        generatePrimes();

    }

    private void generatePrimes(){
       for(long i=firstPrime;i<upperLimit;i++) primeNumbers.add(i);

       int index = 0;

       while(index<upperLimit-firstPrime) {

           for (int i = index; i < upperLimit-firstPrime; i++) {
               long currentPrime = primeNumbers.get(index);
               if(currentPrime==0) continue;
               long primeCandidate = primeNumbers.get(i);
               if(primeCandidate==0) continue;
               if (currentPrime == primeCandidate) continue;
               if (primeCandidate % currentPrime == 0) {
                   primeNumbers.set(i, 0L);
               }
           }
            index++;
       }

       primeNumbers = primeNumbers.stream().filter(e -> e!=0).toList();

    }

    public long randomPrime(){
        Random r = new Random();
        int index = r.nextInt(3,primeNumbers.size());
        return primeNumbers.get(index);
    }


    public List<Long> getPrimes() {
        return this.primeNumbers;
    }


}
