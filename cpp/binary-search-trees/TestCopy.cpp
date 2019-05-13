#include "treenode.h"
#include "tree.h"

#include <iostream>
#include <sstream>

using std::cout;
using std::endl;
using std::ostringstream;

int main()
{

    BinarySearchTree<int> a;
    a.insert(2);
    a.insert(1);
    a.insert(3);
    BinarySearchTree<int> b;
    b = a; // uses the assignment operator
    a.write(std::cout);
    cout << "\n"; // print out a
    b.write(std::cout);
    cout << "\n"; // print out b -- should be the same as a
}