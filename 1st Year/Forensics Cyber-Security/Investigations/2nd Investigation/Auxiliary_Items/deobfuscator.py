import os
import re
import hashlib
import subprocess

EXTRACT = False

archives = [f for f in os.listdir() if f.endswith(".zip")]
archives.sort()

if len(archives) == 0:
    print("Error, no backups found in current directory")
    exit(1)

timestamps = [re.search(r"\d+", f).group(0) for f in archives]


with open('seeds.txt', 'r') as (f):
    seeds = f.readlines()[-len(archives):]

for i in range(len(archives)):
    pw = hashlib.sha256(str(seeds[i] + timestamps[i]).encode('utf-8')).hexdigest()
    print("{} with seed {} should have password {}".format(archives[i], seeds[i].strip(), pw), end='')
    if EXTRACT:
        print("Unzipping...", end='')
        subprocess.run(['unzip', '-P', pw, archives[i], '-d', 'backup_' + timestamps[i]], stdout=subprocess.DEVNULL)
    print()
