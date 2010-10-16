import random
import string

def gen_new_password():
    random.seed(2)
    password = string.join([random.choice(string.letters) for x in xrange(10)])
    return password

