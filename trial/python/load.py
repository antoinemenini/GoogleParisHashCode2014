import numpy as np


def load(fname):
    with open(fname) as f:
        str_size = f.readline()
        str_size_ = str_size.split(' ')
        n = int(str_size_[0])
        m = int(str_size_[1])
        inputs_ = f.readlines()

    n = len(inputs_)
    m = len(inputs_[0])
    C = np.zeros((n, m))
    for i, row in enumerate(inputs_):
        for j, c in enumerate(row):
            C[i, j] = (c == '#')
    return C
