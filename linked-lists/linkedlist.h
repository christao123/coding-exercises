#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include "node.h"

#include <utility>
#include <initializer_list>

using std::initializer_list;

// Do not add any #include statements here.  If you have a convincing need for adding a different `#include` please post in the forum on KEATS.

// TODO your code goes here:

template <typename T>
class LinkedList
{

private:
    Node<T> *head;
    Node<T> *tail;
    int elements;

public:
    LinkedList() : head(nullptr), tail(nullptr), elements(0) {}


    LinkedList(initializer_list<T> list): head(nullptr), tail(nullptr), elements(0){
        for(auto item : list){
            push_back(item);
        }
    }

    NodeIterator<T> insert(NodeIterator<T> position, T element){
        if(elements == 0){
            push_back(element);
            return NodeIterator<T>(head);
        }

        Node<T> *newNode = new Node<T>(element);

        auto current = position.getCurrent();



        if(current->previous == nullptr) {  //inserting before head
            push_front(element);
            delete newNode;
            return begin();
        }

        if( current->next == nullptr){
            push_back(element);
            delete newNode;

            return NodeIterator<T>(tail);
        }

            newNode->next = current;
            newNode->previous = current->previous;
        current->previous->next = newNode;

        current->previous = newNode;


        return NodeIterator<T>(newNode);

    }

    NodeIterator<T> erase(NodeIterator<T> position) {

        if(elements == 0){   //if it's empty we cant delete
            return  NodeIterator<T>();

        }

        if(position.getCurrent()!=nullptr){

            auto current = position.getCurrent();
            
            if(current->next != nullptr && current->previous != nullptr){  //if it is not tail
                current->previous->next = current->next;
                current->next->previous = current->previous;
            }
            else{
                if(current->next == nullptr){
                    current->previous->next = nullptr;
                    tail = current->previous;
                }
                if(current->previous == nullptr){
                    current->next->previous = nullptr;
                    head = current->next;

                }
            }
            NodeIterator<T> iterator(current->next);




            delete position.getCurrent();

            return iterator;
        }

        return NodeIterator<T>();


    }




    void push_front(const T &item)
    {
        Node<T> *newNode = new Node<T>(item);

        if (head != nullptr)
        {
            newNode->next = head;
            head->previous = newNode;
            newNode->previous = nullptr;
            head = newNode;
        }
        else
        {
            head = newNode;
            tail = head;
        }

        ++elements;
    }

    void push_back(const T &item)
    {
        Node<T> *newNode = new Node<T>(item);

        if (tail != nullptr)
        {
            newNode->previous = tail;
            tail->next = newNode;
            newNode->next = nullptr;
            tail = newNode;
        }
        else
        {
            head = newNode;
            tail = head;
        }

        ++elements;
    }

    T &front()
    {
        return head->data;
    }

    const T &front() const
    {
        return head->data;
    }

    T &back()
    {
        return tail->data;
    }

    const T &back() const
    {
        return tail->data;
    }

    int size() const
    {
        return elements;
    }

    NodeIterator<T> begin() const
    {
        return NodeIterator<T>(head);
    }

    NodeIterator<T> end() const
    {
        return NodeIterator<T>(); //default constructor has nullptr node
    }

    void reverse()
    {

        Node<T> *temp = tail;
        tail = head;
        head = temp;

        Node<T> *temphead = head;

        while (temphead != nullptr)
        {
            Node<T> *t = temphead->next;
            temphead->next = temphead->previous;
            temphead->previous = t;
            temphead = temphead->next;
        }
    }

    ~LinkedList()
    {
        Node<T> *temp = tail;
        while (temp != nullptr)
        {
            tail = temp->previous;
            delete temp;
            temp = tail;
        }
    }
};

// do not edit below this line

#endif
