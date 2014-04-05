import networkx as ntx


FNAME = '../paris_54000.txt'

def load():
    G = ntx.DiGraph()

    with open(FNAME) as f:
        param_str = f.readline()
        lines = f.readlines()
    params = param_str.split(' ')
    print params

    N = int(params[0])
    for l in range(N):
        G.add_node(l)
    for l in lines[N:]:
        param_edge = l.split(' ')

        G.add_weighted_edges(int(param_edge[0]),
                             int(param_edge[1]),
                             weight=int(param_edge[3]))
        if param_edge[2] == '2':
            G.add_weighted_edges(int(param_edge[1]),
                             int(param_edge[0]),
                             weight=int(param_edge[3]))

    retrun G

if __name__ == '__main__':
    G = load()
    print G.number_of_nodes()
    print G.number_of_edges()

