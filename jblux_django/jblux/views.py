from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import Context, loader, RequestContext
from jblux_django.jblux.models import User
from jblux_django.jblux.forms import LoginForm, RegisterForm

def index(request):
    try:
        user = request.session['user']
    except KeyError:
        return HttpResponseRedirect('/jblux/login')

    if user:
        return render_to_response('jblux/index.html', {'user': user})
    else:
        return HttpResponse("Not logged in")

def login(request):
    form = LoginForm()

    try:
        username = request.POST['username']
        password = request.POST['password']
    except Exception:
        return render_to_response('jblux/login.html', {'form': form},
                context_instance=RequestContext(request))

    user = auth(username, password)
    if user is not None:
        request.session['user'] = user
        if user.is_active:
            if request.user.is_authenticated():
                return HttpResponseRedirect('jblux/index')
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
    try:
        username = request.POST['username']
        email = request.POST['email']
        password = request.POST['password']
        password2 = request.POST['password2']
    except Exception:
        form = RegisterForm()
        return render_to_response('jblux/register.html', {'form': form},
                context_instance=RequestContext(request))

    if password != password2:
        #passwords don't match
        return render_to_response('jblux/register.html', {'form': form},
                context_instance=RequestContext(request))
    else:
        #successful
        form = LoginForm()
        user = User.objects.create(
                username=username,
                email=email,
                password=password,
                is_admin=False,
                is_active=True,
                character1=None,
                character2=None,
                character3=None,
                character4=None,
                character5=None,
                )
        return render_to_response('jblux/login.html', {'form': form},
                context_instance=RequestContext(request))

def logout(request):
    request.session.flush()
    return HttpResponseRedirect('jblux/login.html')

def auth(username, password):
    try:
        user = User.objects.get(username=username, password=password)
        return user
    except User.DoesNotExist:
        return None

