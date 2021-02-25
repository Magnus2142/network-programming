#include <functional>
#include <list>
#include <mutex>
#include <thread>
#include <vector>
#include <iostream>
#include <condition_variable>
#include <unistd.h>

//TODO: kommenter koden!


using namespace std;
class Workers{
    public:

    list<function<void()>> tasks;
    mutex tasks_mutex;
    condition_variable cv;

    vector<thread> worker_threads;

    int n_threads;

    Workers(int n){
        n_threads = n;
    }


    void post(function<void()> task){
        unique_lock<mutex> lock(tasks_mutex);
        tasks.emplace_back(task);
        cv.notify_all();
    }

    void start(){
        for(int i = 0; i < n_threads; i ++){
            worker_threads.emplace_back([&] {
                while(true){
                    function<void()> task;
                    {
                        unique_lock<mutex> lock(tasks_mutex);
                        if(!tasks.empty()){
                            task = *tasks.begin();
                            tasks.pop_front();
                        }else{
                            lock.unlock();
                            cv.wait(lock);
                        }
                    }
                    if(task){
                        task();
                    }
                }
            });
        }
    }

    void stop(){
        for(auto &thread : worker_threads){
            thread.join();
        }
    }

    //Time in seconds
    void post_timeout(function<void()> task, int s){
        post([task, s]{
            thread tmp([task, s]{
                sleep(s);
                task();
            });
            tmp.join();  
        });
    }
};

int main(){

    Workers worker_threads(4);

    for(int i = 0; i < 10; i ++){
        if(i == 5 || i == 6){
            worker_threads.post_timeout([i]{
                cout << "late task number " << i << endl;
            }, 2);
        }else{
            worker_threads.post([i] {
                cout << "task number " << i << endl;
            });
        }
    }
    worker_threads.start();
    sleep(5);
    worker_threads.stop();
    
    return 0;
}