import numpy as np


def main():
    np.arange(10)


if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Google Hash Code in Paris!')
    args = parser.parse_args()
    main()
