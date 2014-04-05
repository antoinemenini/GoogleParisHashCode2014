# -*- coding: utf-8 -*-

import numpy as np
import sys


def compte(arr):
    n = arr.shape[0]
    p = arr.shape[1]
    count = 0
    for i in range(n):
        for j in range(p):
            if(arr[i][j] == 1):
                count += 1
    return count


def biggestSquare(arr, i, j):
    n = arr.shape[0]
    p = arr.shape[1]
    ok = (arr[i][j] == 1)
    s = 0
    while ok:
        s += 1
        if (i + s < n) and (j + s < p) and (i >= s) and (j >= s):
            for k in range(s + 1):
                ok = ok and (arr[i + k][j + s] >= 1) and (arr[i + s][j + k]>=1)
                ok = ok and (arr[i-k][j+s]>=1) and (arr[i+s][j-k]>=1)
                ok = ok and (arr[i+k][j-s]>=1) and (arr[i-s][j+k]>=1)
                ok = ok and (arr[i-k][j-s]>=1) and (arr[i-s][j-k]>=1)
        else:
            ok=False
    return [s-1,i,j]


def biggestSquareIntel(arr,i,j):
    n = arr.shape[0]
    p = arr.shape[1]
    ok = (arr[i][j]==1)
    s=0
    score=0
    res = np.zeros(70)
    while ok and s < 50:
        s+=1
        if (i+s<n) and (j+s<p) and (i>=s) and (j>=s):
            for k in range (-s,s+1):
                count = 0
                if arr[i+k][j+s]==0:
                    count+=1
                elif arr[i+k][j+s]==1:
                    score+=1
                if arr[i+k][j-s]==0:
                    count+=1
                elif arr[i+k][j-s]==1:
                    score+=1
                if arr[i+s][j+k]==0:
                    count+=1
                elif arr[i+s][j+k]==1:
                    score+=1
                if arr[i-s][j+k]==0:
                    count+=1
                elif arr[i-s][j+k]==1:
                    score+=1
            if(count > s/2):
                ok=False
        else:
            ok=False
        res[s-1] = score
    return res

def iterMalin(arr, score):
    #print "plop"
    
        #sys.stdout.write('\n')

    rank = score.max(axis=-1)
    ind = rank[:, 0].argsort()
    ranked_actions = rank[ind[::-1]]
    current = arr.copy()
    score,s,i,j = ranked_actions[0]
    actions = []
    while score > 0:
        for p in range(i-s,i+s+1):
            for q in range(j-s,j+s+1):
                if current[p][q]==0:
                    current[p][q]=-1
                if current[p][q]==1:
                    current[p][q]=2
                    ind = []
                    for s in range(50):
                        for x in range(p-s, p+s+1):
                            for y in range(q-s, q+s+1) :
                                score[x,y,s] -= 1
        rank = score.max(axis=-1)
        ind = score[:, 0].argsort()
        ranked_actions = rank[ind[::-1]]
        current = arr.copy()
        score,s,i,j = ranked_actions[0]
        actions.append("PAINTSQ {} {} {}".format(i,j,s))
    return actions

def cleanWhite(arr):
    n = arr.shape[0]
    p = arr.shape[1]
    action = []
    for i in range(n):
        for j in range(p):
            if arr[i][j]==-1:
                action.append("ERASECELL {} {}".format(i, j))
    return action

def compute_score_tab(A):

    n, m = A.shape
    bg_tab = np.zeros((n ,m, 70))
    for i in range(n):
        for j in range(m):
            if(j%1000==0):
                sys.stdout.write('.')
                sys.stdout.flush()
            if A[i, j] == 1:
                bg_tab[i, j] = biggestSquareIntel(A, i, j)
    return bg_tab
 
def solMalin(arr):
    score_tab = compute_score_tab(arr)
    np.save('score_tab.npy', score_tab)
    score, tab = iterMalin(arr, score_tab)
    act_pr = iterMalin(tab)
    act_pr += cleanWhite(tab)
    with open('output.txt', 'w') as f:
        f.write(len(a) + '\n')
        for a in act_pr:
            f.write(a + '\n')




if __name__=='__main__':
#    a = np.zeros((4,4))
#    for i in range(3):
#        for j in range(3):
#            a[i][j]=1
#    solMalin(a)
    from load import load
    #import matplotlib.pyplot as plt

    im = load('data/doodle.txt')
    #plt.imshow(im)
    #plt.show()
    solMalin(im)
