#ifndef TREE_H
#define TREE_H

#include "treenode.h"
#include <iostream>

using std::cout;
using std::endl;

// TODO your code goes here:

template <typename T>
class BinarySearchTree
{
private:
    unique_ptr<TreeNode<T>> root;

public:
    BinarySearchTree() = default;

    BinarySearchTree(const BinarySearchTree &bst)
    {

        bst.root != nullptr ? root = unique_ptr<TreeNode<T>>(new TreeNode<T>(*bst.root)) : root = nullptr;
    }

    BinarySearchTree &operator==(BinarySearchTree & bst)
    {
        bst.root != nullptr ? root = unique_ptr<TreeNode<T>>(new TreeNode<T>(*bst.root)) : root = nullptr;
        return *this;
    }



    TreeNodeIterator<T> begin() const{
        TreeNodeIterator<T> a(getSmallestNode());
        return TreeNodeIterator<T>(getSmallestNode());
    }

    TreeNodeIterator<T> end() const{
        return TreeNodeIterator<T>(nullptr);
    }




    TreeNode<T>* getSmallestNode() const{
        TreeNode<T> * node;
        if(root== nullptr){
            return  nullptr;
        }
        if(root->leftChild != nullptr){
            node = root->leftChild.get();
        }
        else{
            if(root!= nullptr) {
                node = root.get();
                return node;
            }
            else{
                return nullptr;
            }
        }
        while(node->leftChild!=nullptr){
            node= node->leftChild.get();
        }

        return node;
    }

    TreeNode<T> * getBiggestNode() const {
        TreeNode<T> * node;
        if(root== nullptr){
            return  nullptr;
        }
        if(root->rightChild != nullptr){
            node = root->rightChild.get();
        }
        else{
            if(root!= nullptr) {
                node = root.get();
                return node;
            }
            else{
                return nullptr;
            }
        }
        while(node->rightChild!=nullptr){
            node = node->rightChild.get();
        }

        return node;
    }



    void write(std::ostream &out) const
    {
        if (root)
            root->write(out);
    }

    int maxDepth(){
        return root->maxDepth(root.get());
    }

    int balanceFactor(TreeNode<T>& t){
        int l;
        int r;

            if(t.leftChild ){
                l = t.maxDepth(t.leftChild.get());

            }
            else{
                l=0;
            }

            if(t.rightChild != nullptr){
                r = t.maxDepth(t.rightChild.get());
            }
            else{
                r=0;
            }

            return l-r;

    }

    void balanceAVL(TreeNode<T> & t){

        if(t.parent != nullptr && t.parent->parent != nullptr){
            int balance = balanceFactor(*t.parent->parent);

        if(balance == 2|| balance == -2) {

            //checking for leftRotation
            if (t.parent->leftChild.get() == nullptr && t.parent->parent->rightChild.get() == t.parent) {
                leftRotation(t);
            }

            //checking for rightRotation
            if (t.parent->rightChild.get() == nullptr && t.parent->parent->leftChild.get() == t.parent) {
                rightRotation(t);
            }

            //checking for left-right rotation
            if (t.parent->leftChild.get() == nullptr
                && t.parent->parent->leftChild.get() == t.parent &&
                t.parent->parent->rightChild.get() == nullptr) {
                leftRightRotation(t);
            }

            //checking for right-left

            else if (t.parent->rightChild.get() == nullptr &&
                t.parent->parent->rightChild.get() == t.parent &&
                t.parent->parent->leftChild.get() == nullptr) {
                rightLeftRotation(t);
            }


        }
        }
    }


    void rightLeftRotation(TreeNode<T> & t){
        auto mainRoot = t.parent->parent->parent;

        TreeNode<T> * grandpa = t.parent->parent;
        TreeNode<T> * pa = t.parent;

        t.parent = mainRoot;
        t.setLeftChild(grandpa);
        t.setRightChild(pa);

        grandpa->parent = &(t);
        pa->parent = &(t);

        grandpa->setRightChild(nullptr);
        pa->setLeftChild(nullptr);

        if(root.get() == grandpa){
            root.release();
            root.reset(&(t));
        }
    }



    void leftRightRotation(TreeNode<T> & t){

        auto mainRoot = t.parent->parent->parent;

        TreeNode<T> * c = t.parent->parent;
        TreeNode<T> * a = t.parent;


        t.parent = mainRoot;
        t.setLeftChild(a);
        t.setRightChild(c);

        a->parent = &(t);
        c->parent = &(t);

        a->setRightChild(nullptr);
        c->setLeftChild(nullptr);

        if(root.get() == c){
            root.release();
            root.reset(&(t));
        }



    }


    void leftRotation(TreeNode<T> & t){
        auto mainRoot = t.parent->parent->parent;

        auto grandpa = t.parent->parent;

        auto pa = t.parent;

        pa->setLeftChild(t.parent->parent);
        grandpa->setRightChild(nullptr);
        t.parent->parent = mainRoot;

        if(mainRoot != nullptr)
        mainRoot->setRightChild(pa);

        if(root.get() == grandpa){
            root.release();
            root.reset(pa);
        }
    }

    void rightRotation(TreeNode<T> & t){
        auto mainRoot = t.parent->parent->parent;

        auto grandpa = t.parent->parent;
        auto pa = t.parent;

        pa->setRightChild(t.parent->parent);
        grandpa->setLeftChild(nullptr);

        t.parent->parent = mainRoot;

        if(mainRoot != nullptr)
            mainRoot->setLeftChild(pa);


        if(root.get() == grandpa){
            root.release();
            root.reset(pa);
        }
    }


    TreeNode<T> *insert(T item)
    {
        auto *t = new TreeNode<T>(item);

        if (root == nullptr)
        {
            root.reset(t);
            return root.get();
        }

        TreeNode<T> *current = root.get();

        while (current != nullptr)
        {
            if (item < current->data)
            {
                if (current->leftChild)
                    current = current->leftChild.get();
                else
                {
                    current->setLeftChild(t);
                    balanceAVL(*t);
                    return t;
                }
            }
            else if (current->data < item)
            {
                if (current->rightChild)
                    current = current->rightChild.get();
                else
                {
                    current->setRightChild(t);
                    balanceAVL(*t);
                    return t;
                }
            }
            else
            {
                return current;
            }
        }

        return nullptr;
    }

    TreeNode<T> *find(T item) const
    {
        TreeNode<T> *current = root.get();
        while (current)
        {
            if (item < current->data)
            {
                current = current->leftChild.get();
            }
            else if (current->data < item)
            {
                current = current->rightChild.get();
            }
            else
            {
                return current;
            }
        }

        return nullptr;
    }
};

// do not edit below this line

#endif
