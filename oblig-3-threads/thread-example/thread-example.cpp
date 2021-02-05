#include <iostream>
#include <mutex>
#include <thread>

using namespace std;

int main(){

    int sum = 0;
    mutex sum_mutex;//Used to make sure that only one thread accesses sum at any time

    thread t1([&sum, &sum_mutex] {
        for(int i = 0; i < 1000; i ++){
            sum_mutex.lock(); //If sum_mutex is already locket, wait until it is unlocket, then lock sum_mutex
            sum++;
            sum_mutex.unlock();// sum_mutex can now be locked elsewhere
        }
    });

    thread t2([&sum, &sum_mutex] {
        for(int i = 0; i < 1000; i ++){
            sum_mutex.lock(); 
            sum++;
            sum_mutex.unlock();
        }
    });

    t1.join();
    t2.join();

    cout << sum << endl;
}