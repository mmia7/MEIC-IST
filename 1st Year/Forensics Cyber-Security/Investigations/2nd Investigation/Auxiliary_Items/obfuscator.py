# uncompyle6 version 3.2.3
# Python bytecode 2.7 (62211)
# Decompiled from: Python 2.7.13 (default, Nov 24 2017, 17:33:09) 
# [GCC 6.3.0 20170516]
# Embedded file name: obfuscator.py
# Compiled at: 2022-10-07 15:07:20
import hashlib, sys
with open('seeds.txt', 'r') as (f):
    lines = f.readlines()
with open('seeds.txt', 'w') as (f):
    for line in lines[1:]:
        f.write(line)
    else:
        f.write(lines[0])

pw = hashlib.sha256(str(lines[0] + str(sys.argv[1])).encode('utf-8'))
print pw.hexdigest()
# okay decompiling obfuscator.pyc
