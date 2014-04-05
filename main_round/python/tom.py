import numpy as np
import sys


def main():
    from load import load
    G, params = load()
    print G.number_of_nodes()
    print G.number_of_edges()
    print G.degree(params[-1])
    print params

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
                directP = G.node[S]['pos']-G.node[k]['pos']
                normP = np.sqrt(sum(directP**2))
                directP = directP / (normP + (normP ==0))
                gain = directP.dot(directC)**2
                sc[p['view']].append((1-p['view'])*(p['weight']*1./p['time']*gain)
                                     + p['view']*(nb_node_not_visited(G, k)* gain))
                sk[p['view']].append(k)
            if len(sc[0]) > 0:
                prob = (np.random.rand()< 0.7) & (len(sc[0])> 1)
                current = sk[0][np.argsort(sc[0])[prob]]
                L += p['weight']
            else:
                prob = (np.random.rand()< 0.5) & (len(sc[1])> 1)
                current = sk[1][np.argsort(sc[1])[prob]]
            path.append(current)
            d += 1
            t += possible[current]['time']
            possible[current]['view'] = 1
            try:
                G.edge[current][prev]['view'] = 1
            except KeyError:
                pass
        paths_c.append(path[:-1])
    print L
    return paths_c, L

def nb_node_not_visited(G, n):
    n = 0
    for c, e in G[n].items():
        n += (1-e['view'])
    return n



if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Google Hash Code in Paris!')
    parser.add_argument('-d', action='store_true')
    args = parser.parse_args()
    pcs = []
    scores = []
    for i in range(20):
        pc, s = main()
        pcs.append(pc)
        scores.append(s)
    i0 = np.argmax(scores)
    pc = pcs[i0]
