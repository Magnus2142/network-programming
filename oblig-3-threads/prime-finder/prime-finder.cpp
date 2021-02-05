#include <iostream>
#include <mutex>
#include <thread>
#include <algorithm>
#include <vector>

using namespace std;

/**
 * Method where a thread loops through every number in the interval and checking if it is a prime or not.
 * If it is a prime, it is flagged as a prime and added to the primes array. Use a mutex lock to make sure
 * only one thread is editing the primes vector at the time.
 */
void findPrimes(int low, int high, vector<int> &primes, mutex &primes_mutex){
    for(int i = low; i < high; i ++){

        int isPrime = 1;
        for(int j = 2; j <= i/2; j ++){
            if(i % j == 0){
                isPrime = 0;
            }
        }
        if(isPrime == 1){
            unique_lock<mutex> lock(primes_mutex);
            primes.emplace_back(i);
        }
    }
}

int main(){

    /**
     * Input from user about the upper and lower limit and how many threads we should use
     * Cannot enter more threads than the difference between upper and higher limit
     */
    int lower_limit;
    cout << "Enter the lower limit: ";
    cin >> lower_limit;

    int upper_limit;
    cout << "Enter upper limit: ";
    cin >> upper_limit;

    int n_threads;
    cout << "Enter number of threads to use: ";
    cin >> n_threads;

    if(n_threads > (upper_limit - lower_limit)){
        cout << "ERROR! Can not have more threads than the interval itself!" << endl;
        return -1;
    }

    //mutex lock that guards the primes vector
    mutex primes_mutex;

    vector<int> primes;
    vector<thread> threads;

    //Deciding how to distribute the task between the threads
    int split = (upper_limit - lower_limit) / n_threads;
    int rest = 0;
    if(split*n_threads != (upper_limit - lower_limit)){
        rest += (upper_limit - lower_limit) - (split * n_threads);
    }

    int l;
    int h;
    for(int i = 0; i < n_threads; i ++){
        l = lower_limit + (split*i);
        h = lower_limit + (split * (i + 1));
        
        if(i == (n_threads - 1)){
            h+= rest + 1;
        }

        //Adding threads to the threads vector and executing the findPrimes function
        threads.emplace_back([l, h, &primes, &primes_mutex] {
            findPrimes(l, h, primes, primes_mutex);
        });
        
    }
    
    //Waits for all threads to join
    for(auto &thread : threads){
        thread.join();
    }

    //Sort the primes
    sort(primes.begin(), primes.end());

    //At last print them out
    for(int i = 0; i < primes.size(); i ++){
         cout << primes[i] << ", ";
    }
    cout << endl;


    return 0;
}