from django.core.mail import send_mail, EmailMessage, EmailMultiAlternatives
import hashlib
import random
import string

def activation_email(username, password, email, reg_num):
    reg_url = "http://tmuo.casey-jones.org/register/"+reg_num

    subject = "Welcome to The Mushroom Universe!"
    message = "Welcome to The Mushroom Universe Online!<br />"
    message += "Click this link to activate your account:<br />"
    message += "<a href=\""+reg_url+"\">"+reg_url+"</a><br />"
    message += "Or copy and paste this url into your browser:<br />"
    message += reg_url + "<br />"
    message += "<br />"
    message += "Please do not delete this email in case you forget<br />"
    message += "your username or password.  Your account information<br />"
    message += "is as follows:<br /><br />"
    message += "-------------------------------<br />"
    message += "Username: "+username+"<br />"
    message += "Password: "+password+"<br />"

    from_email = "tmuo-admin@casey-jones.org"
    to_email = [email,]

    msg = EmailMessage(subject, message, from_email, to_email)
    msg.content_subtype = "html"
    msg.send()

def get_reg_num(username):
    random.seed(2)
    salt = string.join([random.choice(string.letters) for x in xrange(15)])
    salt = salt.replace(' ', '')
    reg_num = hashlib.sha1(username+salt).hexdigest()
    return reg_num

