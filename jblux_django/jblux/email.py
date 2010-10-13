from django.core.mail import send_mail, EmailMessage, EmailMultiAlternatives
import hashlib
import random
import string

def activation_email(username, password, email):
    reg_url = "http://tmuo.casey-jones.org/jblux/register/"+reg_num

    subject = "Welcome to The Mushroom Universe!"
    message = "Welcome to The Mushroom Universe Online!\n"
    message += "Click this link to activate your account:\n"
    message += "<a href=\""+reg_url+"\">"+reg_url+"</a>\n"
    message += "Or copy and paste this url into your browser:\n"
    message += reg_url + "\n"
    message += "\n"
    message += "Please do not delete this email in case you forget\n"
    message += "your username or password.  Your account information\n"
    message += "is as follows:\n\n"
    message += "-------------------------------\n"
    message += "Username: "+username+"\n"
    message += "Password: "+password+"\n"

    from_email = "tmuo-admin@casey-jones.org"
    to_email = [email,]

    msg = EmailMessage(subject, message, from_email, to_email)
    msg.content_subtype = "html"
    #msg.send()
    print msg

def get_reg_num(username):
    random.seed(2)
    salt = string.join([random.choice(string.letters) for x in xrange(15)])
    salt = salt.replace(' ', '')
    reg_num = hashlib.sha1(username+salt).hexdigest()
    return reg_num

