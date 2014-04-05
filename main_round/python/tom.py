import numpy as np
import sys

import networkx as ntx


def main(G, params):

    paths_c = []
    S = params[-1]
    C = params[-2]
    T = params[2]
    L = 0
    
    directions = [0,np.pi/3, 2*np.pi/3, np.pi, 6*np.pi/5, 7*np.pi/5, 8 * np.pi/5, 9*np.pi/6]
    for i in range(C):
        directC = np.array([np.cos(directions[i]),
                            np.sin(directions[i])])

        path = [S]
        t = 0
        current = S
        d = 0
        while t < T:
            possible = G.edge[current]
            sc = {0:[], 1:[]}
            prev = current

            sk = {0:[], 1:[]}
            for k, p in possible.items():
                if d % 1000 == 0:
                    sys.stdout.write('.')
                    sys.stdout.flush()
                directP = G.node[k]['pos']-G.node[S]['pos']
                #normP = np.sqrt(sum(directP**2))
                #directP = directP / (normP + (normP ==0))
                gain = directP.dot(directC)**2
                gain += (gain==0)
                sc[p['view']].append((1-p['view'])*(p['time']*1./p['weight']*1./gain)
                                     + p['view']*(nb_node_not_visited(G, k)/ gain))
                sk[p['view']].append(k)
            if len(sc[0]) > 0:
                prob = draw_rv(sc[0])
                prob = (np.random.rand()< 1) & (len(sc[0])> 1)
                current = sk[0][np.argsort(sc[0])[prob]]
                L += p['weight']
            else:
                #prob = draw_rv(sc[1])
                prob = (np.random.rand()< 0.8) & (len(sc[1])> 1)
                current = sk[1][prob]
            path.append(current)
            d += 1
            t += possible[current]['time']
            possible[current]['view'] = 1
            try:
                G.edge[current][prev]['view'] = 1
            except KeyError:
                pass
        paths_c.append(path[:-1])
    print '\n',L
    return paths_c, L

def main2(G, params, s_points):

    S = params[-1]
    C = params[-2]
    T = params[2]
    L = 0

    t = np.zeros(C)
    paths_c = []
    directC = []
    current = []
    for i, sp in enumerate(s_points):
        p1 = ntx.dijkstra_path(G, S, sp)
        for c, c1 in zip(p1[:-1], p1[1:]):
            t[i] += G[c][c1]['time']
            if G.edge[c][c1]['view'] == 0:
                L += G.edge[c][c1]['weight']
            G.edge[c][c1]['view'] = 1
            try:
                G.edge[c1][c]['view'] = 1
            except KeyError:
                pass
        paths_c.append(p1)
        current.append(sp)

    go = True
    
    d = 0
    while go:
        for i in range(C):
            if t[i] < T:
                possible = G.edge[current[i]]
                sc = {0:[], 1:[]}
                prev = current[i]

                sk = {0:[], 1:[]}
                for k, p in possible.items():
                    if d % 1000 == 0:
                        sys.stdout.write('.')
                        sys.stdout.flush()
                    #directP = G.node[k]['pos']-G.node[S]['pos']
                    #normP = np.sqrt(sum(directP**2))
                    #directP = directP / (normP + (normP ==0))
                    #gain = directP.dot(directC[i])**2
                    #gain += (gain==0)
                    sc[p['view']].append((1-p['view'])*(p['time']*1./p['weight'])
                                         + p['view']*(1./nb_node_not_visited(G, k)))
                    sk[p['view']].append(k)
                if len(sc[0]) > 0:
                    prob = draw_rv(sc[0])
                    prob = (np.random.rand()< 0.75) & (len(sc[0])> 1)
                    current[i] = sk[0][np.argsort(sc[0])[prob]]
                    L += p['weight']
                else:
                    #prob = draw_rv(sc[1])
                    prob = (np.random.rand()< 0.5) & (len(sc[1])> 1)
                    current[i] = sk[1][prob]
                paths_c[i].append(current[i])
                d += 1
                t[i] += possible[current[i]]['time']
                possible[current[i]]['view'] = 1
                try:
                    G.edge[current[i]][prev]['view'] = 1
                except KeyError:
                    pass
            go = False
            for st in t:
                go = go | (st < T)
    print '\n',L
    return paths_c, L


def main3(G, params, s_points):

    S = params[-1]
    C = params[-2]
    T = params[2]
    L = 0

    t = np.zeros(C)
    paths_c = []
    current = []
    for i, sp in enumerate(s_points):
        p1 = ntx.dijkstra_path(G, S, sp)
        for c, c1 in zip(p1[:-1], p1[1:]):
            t[i] += G[c][c1]['time']
            if G.edge[c][c1]['view'] == 0:
                L += G.edge[c][c1]['weight']
            G.edge[c][c1]['view'] = 1
            try:
                G.edge[c1][c]['view'] = 1
            except KeyError:
                pass
        paths_c.append(p1)
        current.append(sp)

    go = True
    from load import load_sub
    sub_G = []
    for i in range(C):
        sub_G.append(load_sub(G, i))
    
    d = 0
    while go:
        for i in range(C):
            if t[i] < T:
                possible = sub_G[i].edge[current[i]]
                sc = {0:[], 1:[]}
                prev = current[i]

                sk = {0:[], 1:[]}
                for k, p in possible.items():
                    if d % 1000 == 0:
                        sys.stdout.write('.')
                        sys.stdout.flush()
                    #directP = G.node[k]['pos']-G.node[S]['pos']
                    #normP = np.sqrt(sum(directP**2))
                    #directP = directP / (normP + (normP ==0))
                    #gain = directP.dot(directC[i])**2
                    #gain += (gain==0)
                    sc[p['view']].append((1-p['view'])*(p['time']*1./p['weight'])
                                         + p['view']*(1./nb_node_not_visited(G, k)))
                    sk[p['view']].append(k)
                if len(sc[0]) > 0:
                    prob = draw_rv(sc[0])
                    prob = (np.random.rand()< 0.75) & (len(sc[0])> 1)
                    current[i] = sk[0][np.argsort(sc[0])[prob]]
                    L += p['weight']
                else:
                    #prob = draw_rv(sc[1])
                    prob = (np.random.rand()< 0.5) & (len(sc[1])> 1)
                    current[i] = sk[1][prob]
                paths_c[i].append(current[i])
                d += 1
                t[i] += possible[current[i]]['time']
                possible[current[i]]['view'] = 1
                try:
                    sub_G[i].edge[current[i]][prev]['view'] = 1
                except KeyError:
                    pass
            go = False
            for st in t:
                go = go | (st < T)
    print '\n',L
    return paths_c, L

#def score(G, n):
def draw_rv(p):
    p = np.array(p)
    p = p *1./p.sum()
    rvu = np.random.rand()
    fdr = p.cumsum()
    return np.argmax(fdr  > rvu)

def nb_node_not_visited(G, k):
    n = 0
    for c, e in G[k].items():
        n += (1-e['view'])#*len(G[c].keys())
    return n + (n==0)



if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Google Hash Code in Paris!')
    parser.add_argument('-d', action='store_true')
    args = parser.parse_args()


    from load import load
    from find_point import find_spread_point
    print 'find starting point'
    G, params = load()
    s_points = find_spread_point(G, params[0])
    print 'done'

    s_points = np.argsort(G.degree().values())[:8]

    pcs = []
    scores = []
    for i in range(20):
        G, params = load()
        pc, s = main2(G, params, s_points)
        pcs.append(pc)
        scores.append(s)
        print i
    i0 = np.argmax(scores)
    pc = pcs[i0]
