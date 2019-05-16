#include "Sudoku.h"
#include "BestFirstSearch.h"

#include <vector>
#include <time.h>
#include <string>

using std::vector;
using std::string;

#include <iostream>

using std::cout;
using std::endl;

bool fillBoard(Sudoku * board, const vector<string> & fillWith) {
    
    for (size_t row = 0; row < fillWith.size(); ++row) {
        for (size_t col = 0; col < fillWith.size(); ++col) {
            if (fillWith[row][col] != ' ') {
                const int setTo = std::stoi(fillWith[row].substr(col,1));
                if (!board->setSquare(row, col, setTo)) {
                    cout << "Error: setSquare returned false after setting square[" << row << "][" << col << "] to " << setTo << " - this should happen as the example boards are solvable\n";
                    return false;
                }
            }
        }
    }
        
    return true;
}



bool checkAnswer(Sudoku * board, const vector<string> & answer) {
    
    for (size_t row = 0; row < answer.size(); ++row) {
        for (size_t col = 0; col < answer.size(); ++col) {
            const int setTo = std::stoi(answer[row].substr(col,1));
            if (board->getSquare(row,col) != setTo) {
                cout << "Error: wrong answer in square[" << row << "][" << col << "]: should be " << setTo << " but is " << board->getSquare(row,col) << endl;
                return false;
            }
        }
    }
        
    return true;
}

int main() {
    double totTimes = 0.0;
    int counter = 0;

    for(int i =0; i<100; ++i) {
        {
            vector<string> hardBoard{"8        ",
                                     "  36     ",
                                     " 7  9 2  ",

                                     " 5   7   ",
                                     "    457  ",
                                     "   1   3 ",

                                     "  1    68",
                                     "  85   1 ",
                                     " 9    4  "};

            vector<string> answer{"812753649",
                                  "943682175",
                                  "675491283",
                                  "154237896",
                                  "369845721",
                                  "287169534",
                                  "521974368",
                                  "438526917",
                                  "796318452"};

            unique_ptr<Sudoku> board(new Sudoku(9));

            clock_t timeParsing;
            timeParsing = clock();


            if (!fillBoard(board.get(), hardBoard)) {
                cout << "Failed: When filling the board, setSquare() returned false\n";
                return 1;
            }
   
            BestFirstSearch search(std::move(board));

            Searchable *solution = search.solve();

            timeParsing = clock() - timeParsing;

            totTimes += (float) timeParsing / CLOCKS_PER_SEC;
            counter++;
    

            if (solution == nullptr) {
                cout << "\nFailed: Couldn't be solved\n";
                return 1;
            }

            Sudoku *solvedBoard = static_cast<Sudoku *>(solution);
        }
    }

    cout<<"avg time: "<<totTimes/counter<<"s"<<endl;
    cout<<"tot time:" <<totTimes;

    return 0;
}
