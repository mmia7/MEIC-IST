#!env python3
import argparse
import csv
import re

if __name__ == '__main__':
    PARSER = argparse.ArgumentParser(description="Converts a word into an FST")
    PARSER.add_argument('word', help='a word')
    args = PARSER.parse_args()

    toSplit = str(args.word)
    regexSplit = re.findall('/|\d|[A-Z][A-Z][A-Z]', toSplit)
    for i,c in enumerate(regexSplit):
        print("%d %d %s %s" % (i, i+1, c, c) )
    print(i+1)
