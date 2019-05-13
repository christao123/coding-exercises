#ifndef TREENODE_H
#define TREENODE_H

#include <iostream>
using std::cerr;
using std::cout;
using std::endl;
using std::ostream;

#include <memory>
using std::unique_ptr;

#include <utility>
using std::pair;

// TODO your code for the TreeNode class goes here:
template <typename T>
class TreeNode
{

public:
    T data;
    unique_ptr<TreeNode<T>> leftChild;
    unique_ptr<TreeNode<T>> rightChild;
    TreeNode<T> *parent;

    TreeNode(T data) : data(data), parent(nullptr) {}

    TreeNode(TreeNode &tn)
    {
        data = tn.data;
        if (tn.leftChild)
            setLeftChild(new TreeNode<T>(*tn.leftChild));
        if (tn.rightChild)
            setRightChild(new TreeNode<T>(*tn.rightChild));
    }


    int maxDepth(TreeNode<T> *t ) const{

        if(!t){
            return 0;
        }
           int leftDepth = maxDepth(t->leftChild.get());
           int rightDepth = maxDepth(t->rightChild.get());

        if(leftDepth>rightDepth){
            return (leftDepth+1);
        }
        else{
            return rightDepth+1;
        }

    }



    void setLeftChild(TreeNode *child)
    {

        leftChild.release();
        leftChild.reset(child);
        if(child != nullptr)
            leftChild->parent = this;

    }

    void setRightChild(TreeNode *child)
    {

        rightChild.release();
        rightChild.reset(child);
        if(child!= nullptr)
            rightChild->parent = this;

    }

    void write(std::ostream &out) const
    {
        if (leftChild)
            leftChild->write(out);

        out << " ";
        out << data;
        out << " ";

        if (rightChild)
            rightChild->write(out);
    }




};

template <typename T>
class TreeNodeIterator{
private:
    TreeNode<T> * treeNode;
public:

    TreeNodeIterator<T>(TreeNode<T> * t): treeNode(t){
    }



    TreeNodeIterator<T> & operator++(){

        if(treeNode->rightChild){
            treeNode = treeNode->rightChild.get();
            while(treeNode->leftChild){
                treeNode = treeNode->leftChild.get();
            }
            return *this;

        }
        TreeNode<T> * temp = treeNode->parent;
        while(temp){

            if(temp->data > treeNode -> data){
                treeNode =temp;
                return *this;
            }
            temp = temp->parent;
        }
        treeNode = nullptr;
        return *this;

    }

    TreeNode<T>  * getNextNode (TreeNode<T> *t){
        if (t->leftChild.get() != nullptr){
            t = t->leftChild.get();
            while (t->leftChild.get() != nullptr){
                t = t->leftChild.get();
            }
            return t;
        }

        while (t->parent != nullptr){
            if (t->parent->leftChild.get() == t)
                return t->parent;
            t = t->parent;
        }

        return nullptr;
    }


    T & operator*() const{
        return treeNode->data;
    }


    bool operator==(const TreeNodeIterator<T> & other) const{
        return treeNode == other.treeNode;
    }

    bool operator!=(const TreeNodeIterator<T> & other) const{
        return treeNode != other.treeNode;
    }
};


// do not edit below this line

#endif
