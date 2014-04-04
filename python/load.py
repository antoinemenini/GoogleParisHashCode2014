import numpy as np


def load(fname):
    with open(fname) as f:
        str_size = f.readline()
        inputs_ = f.readlines()
    C = np.zeros((inputs_.shape))
    for i, row in enumerate(inputs_):
        for j, c in enumerate(row):
            C[i, j] = (c == '#')
    return C
