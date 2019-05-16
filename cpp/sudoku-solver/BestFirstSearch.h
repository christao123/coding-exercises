#ifndef BEST_FIRST_SEARCH_H
#define BEST_FIRST_SEARCH_H

#include <queue>
#include "Searchable.h"
#include <memory>
using std::priority_queue;
using std::unique_ptr;


class Compare{
public:
bool operator()(unique_ptr<Searchable> & a, unique_ptr<Searchable> & b) const{
    return a->heuristicValue() > b->heuristicValue();
}
};


class BestFirstSearch {

protected:

    /// Make sure you increment this every time you 'expand' a node, by getting it successors and putting them on the queue        
    int nodes = 0;
    priority_queue<unique_ptr<Searchable>, vector<unique_ptr<Searchable>>, Compare> queue;


public:

    BestFirstSearch(std::unique_ptr<Searchable> && startFrom) {
        queue.push(std::move(startFrom));
        
    }
    
    int getNodesExpanded() const {
        return nodes;
    }
    
    Searchable * solve() {
        while (!queue.empty()) {

            if (queue.top()->isSolution()) {
                return queue.top().get();
            }

            ++nodes;


            // Get the successors...
            vector<unique_ptr<Searchable>> successors = queue.top()->successors();

            queue.pop();


            for (auto & successor : successors) {
                // and push each one onto the back of queue.
                queue.push(std::move(successor));
            }
        }
        return nullptr;
    }


};
