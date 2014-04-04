# -*- coding: utf-8 -*-

import numpy as np

def compte(arr):
    n = arr.shape[0]
    p = arr.shape[1]
    count=0
    for i in range(n):
        for j in range(p):
            if(arr[i][j]==1):
                ++count
    print count

def biggestSquare(arr,i,j):
    n = arr.shape[0]
    p = arr.shape[1]
    ok = (arr[i][j]==1)
    s=0
    while ok:
        s+=1
        if (i+s<n) and (j+s<p) and (i>=s) and (j>=s):
            for k in range (s):
                ok = ok and (arr[i+k][j+s]>=1) and (arr[i+s][j+k]>=1)
                ok = ok and (arr[i-k][j+s]>=1) and (arr[i+s][j-k]>=1)
                ok = ok and (arr[i+k][j-s]>=1) and (arr[i-s][j+k]>=1)
                ok = ok and (arr[i-k][j-s]>=1) and (arr[i-s][j-k]>=1)
        else:
            ok=False
    return [s-1,i,j]

if __name__=='__main__':
    a = np.zeros((4,4))
    for i in range(3):
        for j in range(3):
            a[i][j]=1
    print biggestSquare(a,1,1)
    print biggestSquare(a,0,1)
