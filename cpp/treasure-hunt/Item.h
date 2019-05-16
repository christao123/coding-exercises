#ifndef ITEM_H
#define ITEM_H

#include <ostream>
#include <iostream>
#include <string>
#include <cmath>
using std::string;
using std::atan;
using std::ostream;



class Item{

private:
    double latitude;
    double longitude;
    string id;
    int seconds;

public:
    Item(double latitude, double longitude, const string & id, int seconds):
        latitude(latitude), longitude(longitude), id(id), seconds(seconds)
    {
        
    }
    void write(ostream & o) const {
       o << "{" << latitude << ", " << 
        longitude << ", \"" << id << "\", "<<
        seconds << "}";
    }


    double distanceTo(const Item & other){
        const int R = 6373000;
        const double PI =  atan(1)*4 / 180;

        double lat1 = latitude *PI;
        double lat2 = other.getLatitude() *PI;
        double lon1 = longitude *PI;
        double lon2 = other.getLongitude() *PI;
        
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        
        double a = pow((sin(dlat/2)), 2) + cos(lat1) * cos(lat2) * pow((sin(dlon/2)), 2);
        double c = 2 * atan2( sqrt(a), sqrt(1-a) );
        return R * c;

    }

    
    double getLatitude() const{
        return latitude;
    }

    double getLongitude() const{
        return longitude;
    }

    string getID() const{
        return id;
    }

    int getSeconds(){
        return seconds;
    }

};

ostream & operator<<(ostream & o, const Item & item){
    item.write(o);
    return o;
}




