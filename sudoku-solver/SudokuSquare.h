#ifndef SUDOKUSQUARE_H
#define SUDOKUSQUARE_H

// Do not add any #include statements to this file

class iterator {
private:
    int currentDigit;
    unsigned int set;

public:
    iterator(unsigned int set, int index) : currentDigit(index), set(set){}

    void operator++() {

        auto number = set;

        int tempIndexCurrent = currentDigit;

        number = number >> (currentDigit + 1);  //rightshift until the number represents the next value
        bool found = false;

        while (!found && currentDigit < 26) {  // todo: remove 33 with actual size?
            (number & 1) == 1 ? found = true :

                number = number >> 1;


            ++currentDigit;
        }

        if (!found) {
            currentDigit = -1;  //the element was the last one;
        }


    }


    bool operator==(iterator other) {
        return currentDigit == other.currentDigit && set == other.set;
    }

    bool operator!=(iterator other) {
        return currentDigit != other.currentDigit || set != other.set;
    }

    int operator*() {
        return currentDigit + 1;
    }

};


class SudokuSquareSet {

    // TODO: write your code here
private:
    unsigned int set;
    int elements;


public:
    SudokuSquareSet() : set(0), elements(0) {};

    SudokuSquareSet(unsigned  int set, int elements) : set(set),elements(elements){};

    int size() const {
        return elements;
    }

    bool empty() const {
        return elements == 0;
    }

    void clear() {
        set = 0;
        elements = 0;
    }

    bool operator==(SudokuSquareSet other) {
        return (set == other.set && elements==other.elements);
    }

    bool operator!=(SudokuSquareSet other) {
        return (set != other.set);
    }


    iterator insert(int value) {
        int n = 0;
        for (int i = value - 1; i > -1; --i) {
            n == 0 ? ++n : n *= 2;
        }
        ++elements;
        set = set | n;


        return {set, value - 1};
    }

    iterator find(const int value) {

       auto n = pow(2,value-1);
       auto g = set & n;
       if(g==n){
           return {set,value-1};
       }

        return iterator(set,-1);
    }

    void erase(const int value) {
        if (find(value) != end()) {
            elements--;
            set -= pow(2, value - 1);
        }
    }

    unsigned int pow(int x, int y) {
        int result = 1;
        for (int i = 0; i < y; ++i) {
            result *= x;
        }

        return static_cast<unsigned int>(result);

    }

    void erase(iterator &itr) {
        erase(*itr);
    }


    iterator begin() const {

        if (set != 0) {

            bool found = false;
            unsigned int number = set;
            int counter = 0;

            while (counter<25) {
                int LSB = number & 1;
                if (LSB == 1) {
                    return {set, counter};
                } else {
                    number = number >> 1;
                }
                ++counter;
            }
        }

        return {set, 0};
    }

    iterator end() const {
        return {set, -1};
    }


};



// Do not write any code below this line
static_assert(sizeof(SudokuSquareSet) == sizeof(unsigned int) + sizeof(int),
              "The SudokuSquareSet class needs to have exactly two 'int' member variables, and no others");


#endif
