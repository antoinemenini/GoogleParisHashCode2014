#include <iostream>
#include <fstream>
#include <sstream>
#include <list>
#include <string>
using namespace std;

static double lati[] = {0,48+49.0/60+59.25/3600,48+49.0/60+5.23/3600,
                        48+50.0/60+39.79/3600,48+51.0/60+32.17/3600,
                        48+52.0/60+48.27/3600,48+51.0/60+40.92/3600,
                        48+54.0/60+6.88/3600,48+51.0/60+25.02/3600,
                        48+54.0/60+8.20/3600,48+52.0/60+53.44/3600};
static double longi[] = {0,2+19.0/60+40.46/3600,2+19.0/60+28.27/3600,
                         2+21.0/60+59.87/3600,2+17.0/60+27.42/3600,
                         2+16.0/60+38.18/3600,2+19.0/60+34.21/3600,
                         2+19.0/60+44.07/3600,2+20.0/60+44.25/3600,
                         2+21.0/60+59.80/3600,2+24.0/60+36.39/3600};

bool riveGauche(double la, double lo){
    double lat[]={48+49.0/60+58.87/3600,48+50.0/60+53.91/3600,
                  48+51.0/60+46.36/3600,48+51.0/60+49.29/3600,
                  48+51.0/60+5.24/3600,48+49.0/60+28.85/3600};
    double lon[]={2+15.0/60+41.39/3600,2+16.0/60+37.51/3600,
                  2+17.0/60+47.02/3600,2+19.0/60+7.57/3600,
                  2+21.0/60+26.97/3600,2+23.0/60+30.31/3600};
    for(int i=0;i<5;++i){
        if(lon[i]<=lo && lo<=lon[i+1]){
            return la < ((lo-lon[i])*lat[i+1]+(lon[i+1]-lo)*lat[i])/(lon[i+1]-lon[i]);
        }
    }
    return false;
}

int quadrantRiveDroite(double lat, double lon){
    if(lon<longi[5]){
        return 1;
    }else if(lon<longi[4]){
        if(lat<((lon-longi[5])*lati[4]+(longi[4]-lon)*lati[5])/(longi[4]-longi[5])){
            return 1;
        } else{
            return 2;
        }
    } else if(lon<longi[6]){
        return 2;
    } else if(lon<longi[7]){
        if(lat<((lon-longi[6])*lati[7]+(longi[7]-lon)*lati[6])/(longi[7]-longi[6])){
            return 3;
        } else{
            return 2;
        }
    } else if(lon<longi[8]){
        return 3;
    } else if(lon<longi[9]){
        if(lat<((lon-longi[8])*lati[9]+(longi[9]-lon)*lati[8])/(longi[9]-longi[8])){
            return 4;
        } else{
            return 3;
        }
    } else if(lon<longi[3]){
        return 4;
    } else if(lon<longi[10]){
        if(lat<((lon-longi[3])*lati[10]+(longi[10]-lon)*lati[3])/(longi[10]-longi[3])){
            return 5;
        } else{
            return 4;
        }
    } else {
        return 5;
    }
}

int quadrantRiveGauche(double lat, double lon){
    if(lon<longi[4]){
        //cout << "4";
        return 6;
    } else if(lon<longi[2]){
        //cout<<"2";
        if(lat<((lon-longi[4])*lati[1]+(longi[1]-lon)*lati[4])/(longi[1]-longi[4])){
            return 6;
        } else{
            return 7;
        }
    } else if(lon<longi[1]){
        //cout<<"1";
        if(lat<((lon-longi[4])*lati[1]+(longi[1]-lon)*lati[4])/(longi[1]-longi[4])){
            if(lat<((lon-longi[2])*lati[1]+(longi[1]-lon)*lati[2])/(longi[1]-longi[2])){
                return 8;
            } else{
                return 6;
            }
        } else{
            return 7;
        }
    } else if(lon<longi[3]){
        //cout << "3";
        if(lat<((lon-longi[1])*lati[3]+(longi[3]-lon)*lati[1])/(longi[3]-longi[1])){
            return 8;
        } else{
            return 7;
        }
    } else {
        return 8;
    }
}

void writeForMenini(){
    ifstream myfile("../paris_54000.txt");
    int n,m,t,v,s;
    myfile >> n >> m >> t >> v >> s;
    cout << n << endl;
    list<int> secteurs[9];
    double lat[n];
    double lon[n];
    int secteur[n];
    for(int i=0;i<n;++i){
        //cout << "Plop : " << i << endl;
        myfile >> lat[i] >> lon[i];
        int quadrant;
        if(riveGauche(lat[i],lon[i])){
            //cout << "Gauche\n";
            quadrant=quadrantRiveGauche(lat[i],lon[i]);
        } else {
            //cout << "Droite\n";
            quadrant=quadrantRiveDroite(lat[i],lon[i]);
        }
        secteurs[quadrant].push_back(i);
        secteur[i]=quadrant;
    }
    cout << "Plap !" << endl;
    int a[m], b[m], d[m], c[m], l[m];
    int secteurarete[m];
    int nb_aretes[9];
    for(int i=0;i<9;++i){
        nb_aretes[i]=0;
    }

    int total_longueur=0;
    for(int i=0;i<m;++i){
        myfile >> a[i] >> b[i] >> d[i] >> c[i] >> l[i];
        if(secteur[a[i]]!=secteur[b[i]]){
            secteurarete[i]=-1;
        } else {
            secteurarete[i]=secteur[a[i]];
            ++nb_aretes[secteurarete[i]];
        }
        total_longueur+=l[i];
    }
    cout << total_longueur << endl;

    for(int i=1;i<=8;++i){
        cout << "Secteur " << i << " de taille " << secteurs[i].size() << endl;
        stringstream plap;
        plap<<"../subMenini"<<i;
        ofstream out(plap.str().c_str());
        out << secteurs[i].size() <<" "<< nb_aretes[i] <<" "<< t <<" "<< 1 <<" "<< secteurs[i].front() << endl;
        while(!secteurs[i].empty()){
            int zde = secteurs[i].front();
            secteurs[i].pop_front();
            out << lat[zde] << " " << lon[zde] << endl;
        }
        for(int j=0;j<m;++j){
            if(secteurarete[j]==i){
                out << a[j] << " " << b[j] << " " << d[j] << " " << c[j] << " " << l[j] << endl;
            }
        }
    }
}

void writeForMoral(){
    ifstream myfile("../paris_54000.txt");
    int n,m,t,c,s;
    myfile >> n >> m >> t >> c >> s;
    double lat;
    double lon;
    cout << n << endl;
    list<int> secteurs[9];
    int secteur[n];
    for(int i=0;i<n;++i){
        //cout << "Plop : " << i << endl;
        myfile >> lat >> lon;
        int quadrant;
        if(riveGauche(lat,lon)){
            //cout << "Gauche\n";
            quadrant=quadrantRiveGauche(lat,lon);
        } else {
            //cout << "Droite\n";
            quadrant=quadrantRiveDroite(lat,lon);
        }
        secteur[i]=quadrant;
        secteurs[quadrant].push_back(i);
    }
    int a[m], b[m], d[m], ca[m], l[m];
    for(int i=0;i<m;++i){
        myfile >> a[i] >> b[i] >> d[i] >> ca[i] >> l[i];
        if(secteur[a[i]]!=secteur[b[i]]){
            if(d[i]==1)
                secteurs[secteur[a[i]]].push_back(b[i]);
            else{
                secteurs[secteur[a[i]]].push_back(b[i]);
                secteurs[secteur[b[i]]].push_back(a[i]);
            }
        }
    }
    cout << "Plap !" << endl;
    for(int i=1;i<=8;++i){
        cout << "Secteur " << i << " de taille " << secteurs[i].size() << endl;
        stringstream plap;
        plap<<"../sub"<<i;
        ofstream out(plap.str().c_str());
        while(!secteurs[i].empty()){
            int zde = secteurs[i].front();
            secteurs[i].pop_front();
            out << zde << ",";
        }
    }
}



int main(){
    writeForMoral();
}
