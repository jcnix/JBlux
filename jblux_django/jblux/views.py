from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import Context, loader, RequestContext
from jblux_django.jblux.models import User, Character
from jblux_django.jblux.forms import LoginForm, RegisterForm, CharacterForm
import hashlib

def index(request):
    return render_to_response('jblux/index.html',
            context_instance=RequestContext(request))

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
            return HttpResponseRedirect('/jblux/index')
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

def new_character(request):
    if request.method == 'POST':
        form = CharacterForm(request.POST)
        if form.is_valid():
            name = form.cleaned_data['name']
            race = form.cleaned_data['race']
            class_t = form.cleaned_data['class_t']
            user = request.session['user']

            character = Character.objects.create(
                    name=name,
                    user=user,
                    race=race,
                    class_t=class_t,
                    strength=1,
                    agility=1,
                    stamina=1,
                    intelligence=1,
                    spirit=1,
                    current_map="residential",
                    )
            return HttpResponseRedirect("/jblux/index")

    try:
        user = request.session['user']
    except KeyError:
        return HttpResponseRedirect('/jblux/login')

    form = CharacterForm()
    return render_to_response('jblux/character_edit.html', {'form': form},
            context_instance=RequestContext(request))

def logout(request):
    request.session.flush()
    return HttpResponseRedirect('/jblux/login')

def game(request):
    try:
        user = request.session['user']
        return render_to_response('jblux/game.html',
                context_instance=RequestContext(request))
    except KeyError:
        return HttpResponseRedirect('/jblux/login')

def info(request):
    return HttpResponse("Not Implemented Yet!")

def screens(request):
    return HttpResponse("Not Implemented Yet!")

def help(request):
    return HttpResponse("Not Implemented Yet!")

def polls(request):
    return HttpResponse("Not Implemented Yet!")

#Recieves hashed password
def auth(username, password):
    try:
        user = User.objects.get(username=username, password=password)
        return user
    except User.DoesNotExist:
        return None

