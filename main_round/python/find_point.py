
import numpy as np

test = [[48.838566,2.332006], [48.830487,2.356382], [48.850484,2.283855], 
		[48.873071,2.294498], [48.891527,2.334667], [48.887972,2.379814],
		[48.867594,2.40007], [48.861553,2.335525]]
test = np.array(test)


def find_spread_point(G, N):
    sp = np.zeros(8)
    min_d = np.ones(8)*100000000000000
    for i in range(N):
        for j in range(8):
            l = sum((G.node[i]['pos'] - test[j])**2)
            if min_d[j] > l:
                min_d[j] = l
                sp[j] = i
    return sp


if __name__ == '__main__':
    from load import load
    G, params = load()
    s_points = find_spread_point(G, params[0])