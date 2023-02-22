#!/usr/bin/python
import urllib.request
import urllib.parse
import sys
import base64
from Crypto.Cipher import AES
import hashlib

password = "Fj39@vF4@54&8dE@!)(*^+-pL;'dK3J2"


def decrypt(enc, password):
    private_key = hashlib.sha256(password.encode("utf-8")).digest()
    enc = base64.b64decode(enc)
    iv = enc[:16]
    cipher = AES.new(private_key, AES.MODE_CFB, iv)
    return cipher.decrypt(enc[16:])


if (len(sys.argv) != 2 and len(sys.argv)):
    print("Use: ./{} <file_in>".format(sys.argv[0]))
    exit(1)

with open(sys.argv[1], 'r') as fp:
    last_line = fp.readlines()[-1]

i = last_line[:10].rfind('=')

if i != -1:
    url_coded = last_line[i+1:]
else:
    url_coded = last_line

base64_coded = urllib.parse.unquote(url_coded)
plaintext = decrypt(base64_coded, password)

with open("{}.dec".format(sys.argv[1]), 'wb') as fp:
    fp.write(plaintext)
