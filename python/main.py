import numpy as np


def main():
    np.arange(10)


if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Google Hash Code in Paris!')
    parser.add_argument('--hw', action='store_true')
    args = parser.parse_args()

    if args.hw:
        print('Hello world!')
    main()
