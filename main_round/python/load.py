import networkx as ntx
import numpy as np


FNAME = '../paris_54000.txt'

def load():
    G = ntx.DiGraph()

    with open(FNAME) as f:
        param_str = f.readline()
        lines = f.readlines()
    params = param_str.split(' ')
    for i in range(len(params)-1):
        params[i] = int(params[i])
    params[-1] = int(params[-1][:-1])

    N = int(params[0])
    for l, line in enumerate(lines[:N]):
        lp = line.split(' ')
        G.add_node(l, pos=np.array([float(lp[0]), float(lp[1])]))
    for l in lines[N:]:
        param_edge = l.split(' ')

        G.add_weighted_edges_from([(int(param_edge[0]),
                             int(param_edge[1]),
                             int(param_edge[4]))], 
                                  view=0, 
                                  time=int(param_edge[3]))
        if param_edge[2] == '2':
            G.add_weighted_edges_from([(int(param_edge[1]),
                                int(param_edge[0]),
                                int(param_edge[4]))], 
                                        view=0, time=int(param_edge[3]))

    return G, params

def load_sub(G, i):
    nodes = np.genfromtxt('../sub{}'.format(i))
    Gi = G.subgraph(nodes)
    return Gi


if __name__ == '__main__':
    G, params = load()
    print G.number_of_nodes()
    print G.number_of_edges()
    print G.degree(params[-1])
    print params

