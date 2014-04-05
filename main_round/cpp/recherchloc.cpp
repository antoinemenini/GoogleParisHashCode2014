#include <iostream>
#include <fstream>
#include <utility>
#include <map>
#include <list>
using namespace std;

typedef pair<int,int> PII;

static map<PII,PII> edges;
static list<list<int> > trajets;
static list<int> durees;
static int max_duree = 54000;
static bool parcouru[11348];
static int n;

int l(int i,int j){
    PII zde;
    zde.first=i;
    zde.second=j;
    if(edges.find(zde)==edges.end()){
        return -1;
    } else {
        return edges[zde].second;
    }
}

int c(int i,int j){
    PII zde;
    zde.first=i;
    zde.second=j;
    if(edges.find(zde)==edges.end()){
        return -1;
    } else {
        return edges[zde].first;
    }
}

//Pt i dans traj k apres j-ieme
int inserer(int i,int k,int j,bool execute){
    list<list<int> >::iterator it=trajets.begin();
    list<int>::iterator duree = durees.begin();
    for(int p=0;p<k;++p){
        ++it;
        ++duree;
    }
    list<int> traj = (*it);
    list<int>::iterator preced = traj.begin();
    for(int p=0;p<j;++p){
        ++preced;
    }
    list<int>::iterator suiv = preced;
    ++suiv;
    if(c(*preced,i)==-1 || c(i,*suiv)==-1){
        return -1;
    }
    int gain = l(*preced,i)+l(i,*suiv)-l(*preced,*suiv);
    int cout = c(*preced,i)+c(i,*suiv)-c(*preced,*suiv);
    if(gain>0 && (*duree)+cout<=max_duree){
        if(execute){
            parcouru[i]=true;
            (*duree) = (*duree)+cout;
            traj.insert(suiv,i);
        }
    }
    return gain;
}

//swap points at position i in k1 and j in k2
int twoopt(int i,int k1,int j,int k2,bool executer){
    list<list<int> >::iterator it1=trajets.begin();
    list<int>::iterator duree1 = durees.begin();
    for(int p=0;p<k1;++p){
        ++duree1;
        ++it1;
    }
    list<int> traj1 = (*it1);
    list<int>::iterator avant1=traj1.begin();
    for(int p=0;p<i-1;++p){
        ++avant1;
    }
    list<int>::iterator apres1=avant1;
    ++apres1; ++apres1;

    list<list<int> >::iterator it2=trajets.begin();
    list<int>::iterator duree2 = durees.begin();
    for(int p=0;p<k2;++p){
        ++duree2;
        ++it2;
    }
    list<int> traj2 = (*it2);
    list<int>::iterator avant2=traj2.begin();
    for(int p=0;p<j-1;++p){
        ++avant2;
    }
    list<int>::iterator apres2=avant2;
    ++apres2; ++apres2;

    int gain = l(*avant1,j)+l(j,*apres1)+l(*avant2,i)+l(i,*apres2);
    gain -=  l(*avant1,i)+l(i,*apres1)+l(*avant2,j)+l(j,*apres2);

    int cout1 = c(*avant1,j)+c(j,*apres1)-c(*avant1,i)-c(i,*apres1);
    int cout2 =  c(*avant2,i)+c(i,*apres2)-c(*avant2,j)-c(j,*apres2);

    if(gain>0 && (*duree1)+cout1<=max_duree && (*duree2)+cout2<=max_duree){
        if(executer){
            (*duree1)+=cout1;
            (*duree2)+=cout2;
            ++avant1;
            ++avant2;
            int old1 = *avant1;
            *avant1=*avant2;
            *avant2=old1;
        }
    }

    return 0;
}

int executeInserer(){
    int gain = 0;
    for(int i=0;i<n;++i){
        if(!parcouru[i]){
            list<list<int> >::iterator traj  = trajets.begin();
            for(int k=0;k<8;++k){
                for(int j=0;j<traj->size();++j){
                    int zou=inserer(i,k,j,true);
                    if(zou>0){
                        gain+=zou;
                    }
                }
                ++traj;
            }
        }
    }
    return gain;
}

int executeTwoOpt(){
    int gain = 0;
    for(int k1=0;k1<8;++k1){
        list<list<int> >::iterator traj1 = trajets.begin();
        for(int p=0;p<k1;++p)
            ++traj1;
        for(int k2=k1+1;k2<8;++k2){
            list<list<int> >::iterator traj2 = trajets.begin();
            for(int p=0;p<k2;++p)
                ++traj2;
            for(int i=0;i<traj1->size();++i){
                for(int j=0;j<traj2->size();++j){
                    int zou=twoopt(i,k1,j,k2,true);
                    if(zou>0){
                        gain+=zou;
                    }
                }
            }
        }
    }
    return gain;
}

void recherche(){
    while(executeTwoOpt()){
        cout << "TwoOpt\n";
        while(executeInserer()){
            cout << "Insertion\n";
        }
    }
}

void writeSol(string name){
    ofstream myfile;
    myfile.open(name.c_str());
    cout << "8\n";
    for(list<list<int> >::iterator it=trajets.begin();it!=trajets.end();++it){
        cout << it->size() << endl;
        for(list<int>::iterator it2 = it->begin();it2!=it->end();++it2){
            cout << (*it2)<<endl;
        }
    }
}

int main(int argc, char** argv){
    string line;
    ifstream myfile("../paris_54000.txt");
    int m,t,c,s;
    myfile >> n >> m >> t >> c >> s;
    for(int i=0;i<n;++i){
        //if(i%1==0)
        //	cout << '.';
        //getline(cin,line);
        double lon,lat;
        parcouru[i]=false;
        myfile >> lat >> lon;
    }
    for(int i=0;i<m;++i){
        int p,q,d,c,l;
        myfile >> p >> q >> d >> c >> l;
        PII key, value;
        key.first=p;
        key.second=q;
        value.first=c;
        value.second=l;
        edges[key]=value;
        if(d==2){
            PII key2;
            key2.first=q;
            key2.second=p;
            edges[key2]=value;
        }
    }
    cout << n << " " << m << " " << edges.size() << endl;
    ifstream sol("../outMenini_best.txt");
    int v;
    sol >> v;
    for(int i=0;i<v;++i){
        int k;
        sol >> k;
        list<int> traj;
        int duree = 0;
        for(int j=0;j<k;++j){
            int p=0,previous=0;
            previous = p;
            sol >> p;
            parcouru[p]=true;
            if(j>0){
                PII key; key.first=previous; key.second=p;
                duree+=edges[key].first;
            }
            traj.push_front(p);
        }
        durees.push_back(duree);
        trajets.push_back(traj);
        cout << traj.size() << endl;
    }
    cout << "Plap" << endl;
    recherche();
    writeSol("../plapzde.txt");
}
