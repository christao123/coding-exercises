#ifndef STACK_H
#define STACK_H

#include <vector>
#include <string>
#include <iostream>

using std::cout;
using std::string;
using std::vector;
class Stack{
    protected:
    vector<double> v;

    public:
    Stack(): v(){}

    void push(const double d){
        v.push_back(d);
    }
    
    double pop(){
        if(!empty()){
            double d = v.back();
            v.pop_back();
            return d;
        }
        return -1;
    }

    bool empty() const{
        return v.size()==0;
    }

};


const double evaluate(const string & expression){
    Stack s;
    int startSplit = 0;

    for(int i = 0; i< expression.size();i++){
        if((isspace(expression[i]) && i > 0) || i==expression.size()-1){  
            char c = expression[i-1];
            if(i == expression.size()-1)
                c = expression[i];
            switch(c){
                case '+' : s.push( s.pop() + s.pop());break;
                case '/' : s.push( 1/s.pop() * s.pop()); break;
                case '-' : s.push(-(s.pop() - s.pop()));break;
                case '*' : s.push( s.pop() * s.pop()); break;
                default: 
                    s.push(std::stod(expression.substr(startSplit, i-1))); break;

            }
            startSplit = i+1;
        }
    }
    return s.pop();
}