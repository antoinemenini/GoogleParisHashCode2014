# -*- coding: utf-8 -*-

import numpy as np
import sys

def compte(arr):
    n = arr.shape[0]
    p = arr.shape[1]
    count=0
    for i in range(n):
        for j in range(p):
            if(arr[i][j]==1):
                count+=1
    return count

def biggestSquare(arr,i,j):
    n = arr.shape[0]
    p = arr.shape[1]
    ok = (arr[i][j]==1)
    s=0
    while ok:
        s+=1
        if (i+s<n) and (j+s<p) and (i>=s) and (j>=s):
            for k in range (s+1):
                ok = ok and (arr[i+k][j+s]>=1) and (arr[i+s][j+k]>=1)
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
    while ok:
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
    return [score,s-1,i,j]

def iterMalin(arr):
    bg_tab = []
    n, m = arr.shape
    for i in range(n):
        for j in range(m):
            #if j % 100 == 0:
            #    sys.stdout.write('.')
            #    sys.stdout.flush()
            if arr[i, j] == 1:
                bg_tab.append(biggestSquareIntel(arr, i, j))
    rank = np.array(bg_tab)
    ind = rank[:, 0].argsort()
    ranked_actions = rank[ind[::-1]]
    current = arr.copy()
    score,s,i,j = ranked_actions[0]
    for p in range(i-s,i+s+1):
        for q in range(j-s,j+s+1):
            if current[p][q]==0:
                current[p][q]=-1
            if current[p][q]==1:
                current[p][q]=2
    print "PAINTSQ",i,j,s
    return [score, current]

def cleanWhite(arr):
    n = arr.shape[0]
    p = arr.shape[1]
    for i in range(n):
        for j in range(p):
            if arr[i][j]==-1:
                print "ERASECELL",i,j

def solMalin(arr):
    score, tab = iterMalin(arr)
    while score>0:
        score, tab = iterMalin(tab)
    cleanWhite(tab)



if __name__=='__main__':
    a = np.zeros((4,4))
    for i in range(3):
        for j in range(3):
            a[i][j]=1
    print biggestSquare(a,1,1)
    print biggestSquare(a,0,1)
