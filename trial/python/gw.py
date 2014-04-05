from load import load
import numpy as np

a = load("doodle.txt")
max_r, max_c = 716, 1522


def count(r, c, s):
    if r < s + 1 or c < s + 1:
        return -1,-1

    n_sharp = 0
    n_dot = 0
    for i in range(r-s, r+s+1):
        for j in range(c-s, c+s+1):
            if a[i, j] == 1:
                n_sharp += 1
            else:
                n_dot += 1
    return n_sharp, n_dot

def find_best_square_fixed(r, c):
    if a[r, c] == 0:
        return (0, 0)
    bestScore = 0
    bestS = 0
    perimeter = 0
    n_sharp = 0
    n_dot = 0
    for s in range(0, 20):
        for i in range(r-s, r+s+1):
            if a[i,c-s]!=a[i+1,c-s+1]:
                perimeter+=1
            if a[i,c+s-1]!=a[i+1,c+s-1]:
                perimeter+=1 
            if a[i, c-s]==0 :
                n_dot+=1
            else:
                n_sharp+=1 
            if a[i, c+s]==0 :
                n_dot+=1
            else:
                n_sharp+=1 
        for j in range(c-s, c+s+1):
            if a[r-s, j]!=a[r-s, j-1]:
                perimeter+=1
            if a[r+s, j]!=a[r+s, j-1]:
                perimeter+=1
            if a[r-s, j]==0 :
                n_dot+=1
            else:
                n_sharp+=1 
            if a[r+s, j]==0 :
                n_dot+=1
            else:
                n_sharp+=1 
        score = 0-perimeter-n_dot+n_sharp
        if score > bestScore:
            bestS=s
    return(bestS, bestScore)

 
def greedy(r, c, s):
    if r < s + 1 or c < s + 1:
        return 
    m = np.a[r-s:r+s, c-s:c+s]
    painted = np.zeros(2*s+1, 2*s+1)
