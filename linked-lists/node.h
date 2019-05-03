#ifndef NODE_H
#define NODE_H

#include <iostream>
using std::cerr;
using std::cout;
using std::endl;

// Do not add any #include statements here.  If you have a convincing need for adding a different `#include` please post in the forum on KEATS.

// TODO your code for the Node class goes here:
// (Note the 'NodeIterator' class will report lots of errors until Node exists
template <typename E>
class Node
{

  public:
    Node<E> *previous;
    Node<E> *next;
    E data;

    Node(E data) : previous(nullptr), next(nullptr), data(data)
    {
    }
};

template <typename T>
class NodeIterator
{

  private:
    Node<T> *current;

  public:
    NodeIterator(Node<T> *currentIn)
        : current(currentIn)
    {
    }

    NodeIterator()
        : current(nullptr)
    {
    }

    Node<T> * getCurrent() const{
        return current;
    }
    

    void operator++()
    {
        current = current->next;
    }

    bool operator!=(const NodeIterator<T> &other) const
    {
        return (current != other.current);
    }

    bool operator==(const NodeIterator<T> &other) const
    {
        return (current == other.current);
    }

    T &operator*()
    {
        return current->data;
    }

    // TODO: complete the code for NodeIterator here
};

// do not edit below this line

#endif
