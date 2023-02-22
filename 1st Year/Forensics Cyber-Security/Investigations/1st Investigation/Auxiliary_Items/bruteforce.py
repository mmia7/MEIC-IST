import hashlib
from base64io import Base64IO as b64


TOOL_URL_HEADER_SIZE = 64
PASSWORD_HASH_SIZE = 64
PASSWORD_HASH_ITERATIONS = 25000

WORDLIST = "wordlist.txt"
FILENAME = "Corrupted.pdf"
VERBOSE = True


def get_password_hash(password: str) -> bytes:
    hash = password.encode('utf-8')
    for _ in range(PASSWORD_HASH_ITERATIONS):
        hash = hashlib.sha512(hash).digest()

    return hash


def check_password(filename: str, password: str):
    with open((f"{filename}"), mode='rb') as encoded_source:
        with b64(encoded_source) as source:
            source.read(TOOL_URL_HEADER_SIZE)
            hash = source.read(PASSWORD_HASH_SIZE)
            # print(hash.hex())
            if get_password_hash(password) == hash:
                print('Password "{}" is correct!'.format(password))
                return True
            else:
                if VERBOSE:
                    print('Password "{}" is incorrect! Aborting...'.format(password))
                return False


fp = open(WORDLIST)
wordlist = [w.strip() for w in fp.readlines()]
fp.close()

for word in wordlist:
    if check_password(FILENAME, word):
        break
