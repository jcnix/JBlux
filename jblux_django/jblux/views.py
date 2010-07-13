from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import Context, loader, RequestContext
from jblux_django.jblux.models import User
from jblux_django.jblux.forms import LoginForm, RegisterForm
import hashlib

def index(request):
    try:
        user = request.session['user']
        return render_to_response('jblux/index.html', {'user': user})
    except KeyError:
        return HttpResponseRedirect('/jblux/login')

def login(request):
    form = LoginForm()

    try:
        username = request.POST['username']
        password = hashlib.sha1(request.POST['password']).hexdigest()
    except Exception:
        return render_to_response('jblux/login.html', {'form': form},
                context_instance=RequestContext(request))

    user = auth(username, password)
    if user is not None:
        request.session['user'] = user
        if user.is_active:
            if request.user.is_authenticated():
                return HttpResponseRedirect('/jblux/index')
            else:
                return render_to_response('jblux/login.html', {'form': form},
                        context_instance=RequestContext(request))
        else:
            return HttpResponse("Disabled")
    else:
        return HttpResponse("Not logged in")

def register(request):
    form = RegisterForm()
    return render_to_response('jblux/register.html', {'form': form},
            context_instance=RequestContext(request))

def register_new_user(request):
    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            username = form.cleaned_data['username']
            email = form.cleaned_data['email']
            #Hash passwords immediately
            password = hashlib.sha1(form.cleaned_data['password']).hexdigest()
            password2 = hashlib.sha1(form.cleaned_data['password2']).hexdigest()

            if password != password2:
                #passwords don't match
                form = RegisterForm()
                return render_to_response('jblux/register.html', {'form': form},
                        context_instance=RequestContext(request))
            else:
                #successful
                user = User.objects.create(
                    username=username,
                    email=email,
                    password=password,
                    is_admin=False,
                    is_active=True,
                    )

                form = LoginForm()
                return HttpResponseRedirect('/jblux/login')
        else:
            return render_to_response('jblux/register.html', {'form': form},
                    context_instance=RequestContext(request))
    else:
        return render_to_response('jblux/register.html', {'form': form},
                context_instance=RequestContext(request))

def logout(request):
    request.session.flush()
    return HttpResponseRedirect('jblux/login.html')

#Recieves hashed password
def auth(username, password):
    try:
        user = User.objects.get(username=username, password=password)
        return user
    except User.DoesNotExist:
        return None

