import numpy as np
import re

from load import load

arr = load('data/doodle.txt')
n = arr.shape[0]
m = arr.shape[1]
arr2 = np.zeros((n, m)) 

with open('solution.txt') as f:
    instr_nbr = int(f.readline())
    instr_it = f.readlines()
    for i, instr in enumerate(instr_it):
        line = instr.split(' ')
        if(line[0] == "PAINTSQ"):
            r = int(line[1])
            c = int(line[2])
            s = int(line[3])
            for k in range (-s,s+1):
                for p in range (-s, s+1):
                    arr2[r+k][c+p] = 1
        elif(line[0] == "ERASECELL"):
            r = int(line[1])
            c = int(line[2])
            arr2[r][c] = 0

result = True
for k in range (0,n):
    for p in range (0,m):
        result = result && arr[k,p]==arr2[k,p]

