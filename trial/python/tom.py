import numpy as np
import sys


def sol1(A):
    '''
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
    np.save('ranked_actions.npy', ranked_actions)'''
    ranked_actions = np.load('ranked_actions.npy')
    Current = A.copy()
    actions = []
    oups = []
    bib = []
    taken = []
    i = 0
    k = 24
    N = ranked_actions.shape[0]
    paint_action = sum(sum(Current == 1))
    while paint_action != 0:
        S, r, s = ranked_actions[i]
        nb_ac = sum(sum(Current[r - S:r + S + 1, s - S:s + S + 1] == 1))
        if nb_ac > k and i not in taken:
            Current[r - S:r + S + 1, s - S:s + S + 1] = 2
            act = ranked_actions[i]
            bib.append(act[1] + act[2])
            oups.append(act)
            paint_action -= nb_ac
            print(paint_action)
            taken.append(i)
        i += 1
        if i == N and k != 0:
            i = 0
            k -= 1
        if s < k:
            i = 0
            k -= 1
    ind = np.argsort(bib)
    actions = []
    oups = np.array(oups)
    for act in oups[ind]:
        actions.append('PAINTSQ {} {} {}'.format(act[1], act[2], act[0]))
   
    return actions


def sol2(A):
    '''
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
    np.save('ranked_actions.npy', ranked_actions)'''
    ranked_actions = np.load('ranked_actions.npy')
    Current = A.copy()
    actions = []
    i = 0
    paint_action = sum(sum(Current == 1))
    while paint_action != 0:
        S, r, s = ranked_actions[i]
        nb_ac = sum(sum(Current[r - S:r + S + 1, s - S:s + S + 1] == 1))
        if nb_ac > 0:
            Current[r - S:r + S + 1, s - S:s + S + 1] = 2
            act = ranked_actions[i]
            actions.append('PAINTSQ {} {} {}'.format(act[1], act[2], act[0]))
            paint_action -= nb_ac
            print(paint_action)
        i += 1
    return actions


if __name__ == '__main__':
    test = np.arange(100).reshape((10, 10))
    t = sol1(test)
