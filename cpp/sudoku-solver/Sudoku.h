#ifndef SUDOKU_H
#define SUDOKU_H

#include "Searchable.h"
#include "SudokuSquare.h"
#include <iostream>
#include <set>
#include <cmath>
#include <utility>

using std::cout;
using std::pair;
using std::endl;
using std::set;
using std::sqrt;

class Sudoku : public Searchable {

private:
    vector<vector<SudokuSquareSet>> solution;
    int size;
    int completedCells;

public:

     bool isSolution() const override {
        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                if (solution[row][col].size() != 1) {
                    return false;
                }
            }
        }
        return true;
    }


    int heuristicValue() const override {
        return (size * size) - completedCells;
    }

     vector<unique_ptr<Searchable>> successors() const override {
        vector<unique_ptr<Searchable>> succ;


        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {


                if (solution[row][col].size() != 1) {
                    auto s = solution[row][col];
                    for (auto i : s) {

                        auto *su = new Sudoku(*this);

                        if (su->setSquare(row, col, i)) {
                            succ.emplace_back(su);
                        } else {
                            delete su;
                        }
                    }
                    if (succ.size() == 1 && !succ[0]->isSolution()) {
                        return succ[0]->successors();
                    } else {
                        return succ;
                    }


                }


            }
        }
        return succ;


    }

    unsigned int pow(int x, int y) {
        int result = 1;
        for (int i = 0; i < y; ++i) {
            result *= x;
        }

        return static_cast<unsigned int>(result);

    }


    explicit Sudoku(unsigned int size) : completedCells(0), size(size),
                       solution(size, vector<SudokuSquareSet>(size, SudokuSquareSet(pow(2, size) - 1, size))) {

    }

    int getSquare(int row, int col) const {
        if (solution[row][col].size() == 1)
            return *solution[row][col].begin();

        return -1;
    }


    bool setSquare(int row, int col, int value) {

        solution[row][col].clear();
        solution[row][col].insert(value);
        ++completedCells;


        vector<pair<int, int>> updatedValues;

        //REMOVE VALUE FROM ROW
        for (int i = 0; i < size; ++i) {
            if (i != col) {


                auto cellrow = &(solution[row][i]);

                auto found = cellrow->find(value);
                if (found != cellrow->end()) {
                    if (cellrow->size() > 1) {
                        cellrow->erase(found);
                        if (cellrow->size() == 1) {
                            updatedValues.emplace_back(pair<int, int>(row, i));
                        }
                    } else {
                        return false;
                    }
                }
            }





            //REMOVE VALUE FROM COL
            if (row != i) {

                auto cellcol = &(solution[i][col]);

                auto found = cellcol->find(value);
                if (found != cellcol->end()) {
                    if (cellcol->size() > 1) {

                        cellcol->erase(found);

                        if (cellcol->size() == 1) {
                            updatedValues.emplace_back(pair<int, int>(i, col));
                        }
                    } else { return false; }

                }
            }
          }



            //REMOVE VALUE FROM BOX
            int boxSize = static_cast<int>(sqrt(size));

            int originX = row - (row % boxSize);
            int originY = col - (col % boxSize);

            /*int yInd = 0;
            (i + 1) % 3 != 0 ? yInd = (i + 1) / boxSize + originY : yInd = (i + 1) / boxSize - 1 + originY;

            int xInd = (i % boxSize) + originX;*/

            for (size_t xInd = originX; xInd < originX+boxSize+1; xInd++) {
              for (size_t yInd = originY; yInd < originY+boxSize+1; yInd++) {



            if (yInd != col && xInd != row) {


                int tempX = xInd - (xInd % boxSize);
                int tempY = yInd - (yInd % boxSize);

                if (originX == tempX && originY == tempY) {
                    auto cell = &(solution[xInd][yInd]);
                    auto found = cell->find(value);

                    if (found != cell->end()) {
                        if (cell->size() > 1) {
                            cell->erase(found);
                            if (cell->empty()) {
                                return false;
                            }
                            if (cell->size() == 1) {
                                updatedValues.emplace_back(pair<int, int>(xInd, yInd));
                            }
                        } else { return false; }
                    }

                    }
                }
              }
            }


            for (auto i : updatedValues) {
                auto cell = solution[i.first][i.second];
                int v = *cell.begin();

                if (!setSquare(i.first, i.second, v)) {
                    return false;
                }

            }


            return true;

        }


        void write(ostream &o) const override{
            o << "-----------------------------------------STATE OF SUDOKU CELLS-----------------------------------"
              << endl;

            for (int row = 0; row < size; ++row) {
                for (int col = 0; col < size; ++col) {
                    SudokuSquareSet cell = solution[row][col];
                    o << "{";

                    for (auto i : cell) {
                        o << i;
                    }

                    for (int s = cell.size(); s < size; ++s) {
                        o << " ";
                    }
                    o << "}";
                }
                o << endl;
            }

            o << "-------------------------------------------------------------------------------------------------"
              << endl;
        }


    };