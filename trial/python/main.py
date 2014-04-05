import numpy as np


def main():
    from load import load
    import matplotlib.pyplot as plt

    im = load('data/doodle.txt')
    plt.imshow(im)
    plt.show()

    from tom import sol1
    test = sol1(im)
    return test


if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Google Hash Code in Paris!')
    parser.add_argument('--hw', action='store_true')
    args = parser.parse_args()

    if args.hw:
        print('Hello world!')
        np.range(2)
    t = main()
