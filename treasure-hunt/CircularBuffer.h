#ifndef CIRCULAR_BUFFER_H
#define CIRCULAR_BUFFER_H

// NB: Do not add any extra #include statements to this file
#include "SimpleVector.h"
#include <iostream>

/** TODO: complete the definition of the class CircularBuffer here
 *
 * Its constructor should take the size of the buffer as an argument
 *
 * It needs to have the functions:
 * - count() which returns how many things are in the buffer
 * - full() which returns true iff the buffer is full
 * - add() which takes an element and adds it to the buffer (you can assume the buffer is not full)
 * - remove() which removes the next element from the buffer (you can assume the buffer is not empty)
 */
class CircularBuffer {
    private:
        SimpleVector<char> svector;
        int head = 0;
        int tail = 0;
        int elements = 0;
        int size;

    public:
        CircularBuffer(int size) : svector(size), size(size){            
        }

        int count(){
           return elements;
        }

        bool full(){
            return elements == size;
        }

        void add(char c){
            svector[tail] = c;
            elements++;
            (tail == svector.size()-1) ? tail = 0 : tail++;
        }

        char remove(){
            char c = svector[head];
            head++;
            elements--;
            return c;
        }
};


// don't write any code below this line

#endif
