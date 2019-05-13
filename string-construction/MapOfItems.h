#ifndef MAPOFITEMS_H
#define MAPOFITEMS_H

#include <vector>
#include <iostream>
#include "Item.h"

using std::cout;
using std::endl;
using std::vector;

// TODO: your code goes here

class MapOfItems
{

private:
    vector<Item> items;

public:
    MapOfItems() {}

    void addItem(const Item &i)
    {
        items.push_back(i);
    }

    int size() const
    {
        return items.size();
    }

    const vector<Item>& getItems() const
    {
        return items;
    }

    double visit(Item &firstItem, Item &secondItem, double walkspeed, double currentTime)
    {
        double d = firstItem.distanceTo(secondItem);
        double t = d / walkspeed;
        return (currentTime + t);
    }

    vector<Item *> getTour(double walkspeed)
    {

        vector<Item *> tour;
        vector<Item *> v;
        double currentTime;
        for (int i = 0; i<items.size(); ++i )
        {
            v.push_back(&(items[i])); //populate v  (O(n))
        }
        if (v[0]->getSeconds() < 3600 && v[0]->getSeconds() >= 0)
        {
            currentTime = v[0]->getSeconds();
            tour.push_back(v[0]);
            v.erase(v.begin());
        }
        else
        {
            return tour;
        }


        while (v.size() > 0)
        {
            double bestTime = 3601;
            int bestItemIndex = -1;
            for (int i = 0; i < v.size(); ++i)
            {
                double timeAppears = v[i]->getSeconds();
                double visitTime = visit(*tour.back(), *v[i], walkspeed, currentTime);
                if(v[i]->getSeconds() > visitTime){
                    visitTime = v[i]->getSeconds();
                }
                if (visitTime < bestTime && visitTime < v[i]->getSeconds()+900)
                {
                    bestTime = visitTime;
                    bestItemIndex = i;
                }
            }

            if (bestTime < 3601 && bestItemIndex > -1)
            {

                if(v[bestItemIndex]->getSeconds() > bestTime){
                    currentTime = v[bestItemIndex]->getSeconds();
                }else{

                currentTime = bestTime;}
                double TIME_OF_SPAWN = v[bestItemIndex]->getSeconds();
                tour.push_back(v[bestItemIndex]);
                v.erase(v.begin() + bestItemIndex);
            }
            else
            {
                return tour;
            }
        }

        return tour;
    }
};

// don't write any code below this line

#endif
