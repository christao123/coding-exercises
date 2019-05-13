#ifndef STRINGCONSTRUCTION_H
#define STRINGCONSTRUCTION_H

#include <iostream>
#include <string>
using std::cout;
using std::endl;
using std::size_t;
using std::string;

// TODO: your code goes here:

int stringConstruction(const string &target, const int &appendCost, const int &cloneCost)
{
    int totalCost = 0;
    int spaces = 0;
    bool edgecasedone = false;
    string s = target; //this string is going to contain non clone-worthy chars at the end
    string substrToFind;

LOOP:
    for (int i = s.length() / 2; i > 1; i--)
    { //iterates through length of substrings
        if (appendCost * i >= cloneCost)
        {
            for (int j = 0; j < s.length() / 2 + 1; j++)
            { //this moves substring forward on target string //check for loop condition
                edgecasedone = false;
                substrToFind = s.substr(j, i);
                if (substrToFind.find("?") == std::string::npos)
                {

                    //check for edge case only( if i is odd)
                    size_t foundSuf = s.substr(j + i, s.length() - substrToFind.length() - j).find(substrToFind);

                    if (foundSuf != std::string::npos)
                    {
                        foundSuf += (j + i);

                        //checking for edge case
                        if (i % 2 != 0 && j >= i - 1 && (2 * appendCost + 2 * cloneCost > appendCost + 2 * cloneCost))
                        {

                            int difference = (i - 1) / 2;
                            size_t presubstr = s.substr(0, foundSuf - difference).find(s.substr(foundSuf - difference, i - 1));
                            if (presubstr != std::string::npos)
                            {

                                size_t postsubstr = s.substr(0, foundSuf).find(s.substr(foundSuf + i - difference, i - 1));
                                if (postsubstr != std::string::npos)
                                {

                                    totalCost += (cloneCost * 2);
                                    s.replace(foundSuf - difference, substrToFind.length() - 1, "?");
                                    s.replace(foundSuf, substrToFind.length() - 1, "?");
                                    spaces += 2;

                                    edgecasedone = true;
                                }
                            }
                        }

                        if (!edgecasedone)
                        { //updating cost
                            totalCost += cloneCost;
                            //updating string

                            s.replace(foundSuf, substrToFind.length(), "?"); //replacing second occurance of substri with empty spaces
                            spaces += 1;

                            i = target.length() / 2;
                            j = 0;
                            goto LOOP;
                        }
                    }
                }
            }
        }
    }

    totalCost += ((s.length() - spaces) * appendCost);
    cout << "final s: " << s << endl;
    cout << " s.length: " << s.length() << endl;
    cout << "number erased: " << spaces << endl;
    return totalCost;
}

// do not write or edit anything below this line

#endif
