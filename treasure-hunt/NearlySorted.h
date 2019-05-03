#ifndef NEARLYSORTED_H
#define NEARLYSORTED_H

#include <iostream>
#include <vector>
#include <algorithm>
using std::cout;
using std::endl;
using std::reverse;
using std::vector;

/** @brief Class that describes how to sort a nearly-sorted vector */
class HowToSort
{
  protected:
    /** @brief The index of the first element to use */
    int firstElement;

    /** @brief The index of the second element to use */
    int secondElement;

    /** @brief If true, firstElement and secondElement should be swapped; if false, the range of elements should be reversed */
    bool swapThem;

  public:
    /** @brief Default constructor.
     * 
     * This assumes that we can't sort the vector by swapping/reversing a range -- it sets both elements
     * to have an index -1.
     */
    HowToSort()
        : firstElement(-1),
          secondElement(-1),
          swapThem(false)
    {
    }

    /** @brief The actual constructor: use this to define how to sort a nearly-sorted vector
     * 
     * @param firstElementIn   The first element to use
     * @param secondElementIn  The second element to use
     * @param swapThemIn       If true, swap firstElementIn and secondElementIn; if false, reverse the range of elements
     */
    HowToSort(const int firstElementIn, const int secondElementIn,
              const bool swapThemIn)
        : firstElement(firstElementIn),
          secondElement(secondElementIn),
          swapThem(swapThemIn)
    {
    }

    /** @brief Returns false if no way to sort the vector could be found (i.e. if firstElement == -1) */
    bool canBeMadeSorted() const
    {
        return (firstElement != -1);
    }

    /** @brief Accessor function -- get the first element to use */
    int getFirstElement() const
    {
        return firstElement;
    }

    /** @brief Accessor function -- get the second element to use */
    int getSecondElement() const
    {
        return secondElement;
    }

    /** @brief If true, the element indices denote a swap.  If false, they denote a range reversal. */
    bool isASwap() const
    {
        return swapThem;
    }
};

// TODO your code goes here:

HowToSort nearlySorted(const vector<int> & originalVector)
{
    vector<int> v = originalVector;
    int firstElement = -1;
    int secondElementSwap = -1;
    int secondElementReverse = -1;

    for (int i = 0; i < v.size() - 1; ++i)
    {

        if (v[i + 1] < v[i])

        {
            //check if a swap or a reverse has already been performed
            if (firstElement > -1)
            {
                return HowToSort();
            }
            //check if there is only a swap needed or we have to start the process of reversing
            if (v[i + 1] > v[i + 2] && v[i + 2] > v[i + 3])
            {
                bool reachedEnd = false;
                secondElementReverse = i;
                while (!reachedEnd)
                {
                    if (v[secondElementReverse + 1] < v[secondElementReverse] && v.size() != secondElementReverse + 1)
                    {
                        ++secondElementReverse;
                    }
                    else
                    {
                        firstElement = i;
                        reachedEnd = true;

                        vector<int>::iterator first = v.begin() + firstElement;
                        vector<int>::iterator last = v.begin() + secondElementReverse + 1;

                        reverse(first, last);
                    }
                }
            }
            else
            { //we assume that there is only one swap needed
                bool reachedEnd = false;
                secondElementSwap = i + 1;
                while (!reachedEnd && v.size() > secondElementSwap)
                {
                    if (v[secondElementSwap] < v[secondElementSwap + 1] && v[i] < v[secondElementSwap + 1] )
                    {
                        firstElement = i;
                        reachedEnd = true;

                        int temp = v[firstElement];
                        v[firstElement] = v[secondElementSwap];
                        v[secondElementSwap] = temp;
                    }
                    else
                    {
                        ++secondElementSwap;
                    }
                }
            }
        }
    }

    if (firstElement > -1)
    {
        if (secondElementSwap > -1 && secondElementReverse == -1)
        {
            return HowToSort(firstElement, secondElementSwap, true);
        }
        else if (secondElementReverse > -1 && secondElementSwap == -1)
        {
            return HowToSort(firstElement, secondElementReverse, false);
        }
    }
    return HowToSort(0, 0, true); //if the vector is sorted
}

// Don't write any code below this line

#endif
