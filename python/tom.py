import numpy as np
import sys


def sol1(A):
    from thierry1 import biggestSquare
    bg_tab = []
    n, m = A.shape
    for i in range(n):
        for j in range(m):
            if j % 100 == 0:
                sys.stdout.write('.')
                sys.stdout.flush()
            if A[i, j] == 1:
                bg_tab.append(biggestSquare(A, i, j))
    rank = np.array(bg_tab)
    ind = rank[:, 0].argsort()
    ranked_actions = rank[ind[::-1]]
    Current = A.copy()
    actions = []
    i = 0
    while sum(sum(Current == 1)) != 0:
        actions.append(ranked_actions)
    return rank
