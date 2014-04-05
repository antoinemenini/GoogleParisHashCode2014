from load import load
import numpy as np

a = load("doodle.txt")
max_r, max_c = 716, 1522
#max_r, max_c = 9, 9


def count(r, c, s):
    if r < s + 1 or c < s + 1:
        return -1, -1

    n_sharp = 0
    n_dot = 0
    for i in range(r - s, r+s+1):
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
    maxS= min(20, max_r-r, r, max_c-c, c)
    for s in range(0, maxS):
        for i in range(r-s, r+s+1):
            if a[i, c-s]!=a[i+1, c-s+1]:
                perimeter +=1
            if a[i, c+s-1]!=a[i+1, c+s-1]:
                perimeter +=1
            if a[i, c-s]==0:
                n_dot +=1
            else:
                n_sharp +=1
            if a[i, c+s] == 0:
                n_dot +=1
            else:
                n_sharp +=1
        for j in range(c-s, c+s+1):
            if a[r-s, j]!=a[r-s, j-1]:
                perimeter+=1
            if a[r+s, j]!=a[r+s, j-1]:
                perimeter +=1
            if a[r-s, j] == 0:
                n_dot +=1
            else:
                n_sharp += 1
            if a[r+s, j] == 0:
                n_dot += 1
            else:
                n_sharp +=1
        score = 0-perimeter-n_dot+n_sharp
        if score > bestScore:
            bestS = s
    return(bestS, bestScore)

a_out = np.zeros((max_r, max_c))
b = np.zeros((max_r, max_c))


def find_all():
    out = open("output.txt", 'w')
    for r in range(max_r):
        for c in range(max_c):
            if a_out[r,c]==0:
                s, y = find_best_square_fixed(r, c)
                if s != 0:
                    out.write("PAINTSQ %d %d %d\n" % (r, c, s))
                    b[r, c] = s
                    a_out[r-s:r+s+1, c-s:c+s+1] = 1
                    print r, c

    for r in range(max_r):
        for c in range(max_c):
            if b[r,c] == 0 and a_out[r, c] == 1:
                out.write("ERASECELL %d %d\n" %(r, c))
            if b[r,c] == 1 and a_out[r, c] == 0:
                out.write("PAINTSQ %d %d 0\n" % (r, c))      



find_all()
