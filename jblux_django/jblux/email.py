from django.core.mail import send_mail

def activation_email(username, password, email):
    subject = "Welcome to The Mushroom Universe!"
    message = "Welcome to The Mushroom Universe Online!\n"
    message += "Please do not delete this email in case you forget\n"
    message += "your username or password.  Your account information\n"
    message += "is as follows:\n\n"
    message += "-------------------------------\n"
    message += "Username: "+username+"\n"
    message += "Password: "+password+"\n"

    from_email = "tmuo-admin@casey-jones.org"
    to_email = [email,]

    send_mail(subject, message, from_email, to_email)

