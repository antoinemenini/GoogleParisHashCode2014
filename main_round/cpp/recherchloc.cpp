#include <iostream>
#include <fstream>
#include <utility>
#include <map>
#include <list>
using namespace std;

typedef pair<int,int> PII;

static map<PII,PII> edges;
static list<list<int>*> trajets;
static list<int> durees;
static int max_duree = 54000;
static bool parcourus[];
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
	list<list<int> *>::iterator it=trajets.begin();
	list<int>::iterator duree = durees.begin();
	for(int p=0;p<k;++p){
		++it;
		++duree;
	}
	list<int> *traj = (*it);
	list<int>::iterator preced = traj->begin();
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
	if(gain>0 && (*duree)+cout<max_duree){
		if(execute){
			parcouru[i]=true;
			(*duree) = (*duree)+cout;
			traj->insert(suiv,i);
		}
	}
	return gain;
}

int twoopt(int i,int j,bool executer){
    return 0;
}

int executeInserer(){
	int gain = 0;
	for(int i=0;i<n;++i){
		if(!parcouru[i]){
			list<list<int>*>::iterator traj  = trajets.bnegin();
			for(int k=0;k<8;++j){
				for(int j=0;j<(*traj).size();++j){
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

int main(int argc, char** argv){
	string line;
	ifstream myfile("../paris_54000.txt");
	int m,t,c,s;
	myfile >> n >> m >> t >> c >> s;
	parcouru = new int[n];
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
	ifstream sol("../out.txt");
	int v;
	sol >> v;
	for(int i=0;i<v;++i){
		int k;
		sol >> k;
		list<int> *traj;
		int duree = 0;
		for(int j=0;j<v;++j){
			int p,previous;
			previous = p;
			sol >> p;
			parcouru[p]=true;
			if(j>0){
				PII key; key.first=previous; key.second=p;
				duree+=edges[key].first;
			}
			traj->push_back(p);
			durees.push_back(duree);
		}
		trajets.push_back(traj);
	}
}
