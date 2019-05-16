#ifndef SIMPLEVECTOR_H
#define SIMPLEVECTOR_H

/**
 * @author Andrew Coles
*/
template<class T>
class SimpleVector {
private:
    T * elements;
    int numberOfElements;
public:
    SimpleVector(const int sizeIn)
        : elements(new T[sizeIn]), numberOfElements(sizeIn) {
    }
    
    ~SimpleVector() {
        delete [] elements;
    }

    SimpleVector(const SimpleVector & b) = delete;
    SimpleVector(SimpleVector && b) = delete;        
    SimpleVector & operator=(const SimpleVector & b) = delete;
    SimpleVector & operator=(SimpleVector && b) = delete;
    
    int size() const {
        return numberOfElements;
    }
    
    T & operator[](const int i) {
        return elements[i];
    }
    
    const T & operator[](const int i) const {
        return elements[i];
    }
};

#endif
