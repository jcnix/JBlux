from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext, loader, Context
from jblux_django.jblux.models import User, Character, Map, Inventory, NewsPost
from jblux_django.jblux.forms import LoginForm, RegisterForm, CharacterForm
from jblux_django.jblux.forms import SelectCharacterForm, ResetPasswordForm
from jblux_django.jblux.forms import AccountSettingsForm
from jblux_django.jblux.email import activation_email, get_reg_num, email_new_pass
from jblux_django.jblux import util
import hashlib

def index(request):
    latest_posts = NewsPost.objects.all().order_by('-pub_date')
    return render_to_response('jblux/index.html', {'latest_posts': latest_posts},
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
            return HttpResponseRedirect('/profile/'+str(user.id))
        else:
            return HttpResponse("Disabled")
    else:
        return render_to_response('jblux/login.html', {'form': form, 'error': True},
                context_instance=RequestContext(request))

def register(request):
    form = RegisterForm()
    return render_to_response('jblux/register.html', {'form': form},
            context_instance=RequestContext(request))

def activate_account(request, reg_num):
    try:
        user = User.objects.get(reg_num=reg_num)
    except User.DoesNotExist:
        return HttpResponse("Invalid registration")

    user.is_active = True
    user.save()
    return HttpResponse("Thank you for registering")

def register_new_user(request):
    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            username = form.cleaned_data['username']
            email = form.cleaned_data['email']
            pass_for_email = form.cleaned_data['password']

            #Hash password
            password = hashlib.sha1(form.cleaned_data['password']).hexdigest()
            reg_num = get_reg_num(username)
            user = User.objects.create(
                username=username,
                email=email,
                password=password,
                is_admin=False,
                is_active=False,
                reg_num=reg_num
                )

            #send an email
            activation_email(username, pass_for_email, email, reg_num)

            return HttpResponse('Confirmation email sent to '+email)
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
            #verify the user has an open slot
            user = request.session['user']
            chars = Character.objects.filter(user=user).count()
            if (chars) >= 1:
                return HttpResponse("No open character slots.")

            name = form.cleaned_data['name']
            race = form.cleaned_data['race']
            class_t = form.cleaned_data['class_t']
            user = request.session['user']

            starter_map = Map.objects.get(name='residential')
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
                    current_map=starter_map,
                    x_coord = 352,
                    y_coord = 384,
                    )
            inventory = Inventory.objects.create(character=character)
            character.inventory = inventory
            character.save()

            return HttpResponseRedirect("/index")
        else:
            return render_to_response('jblux/character_edit.html', {'form': form},
                    context_instance=RequestContext(request))

    try:
        user = request.session['user']
    except KeyError:
        return HttpResponseRedirect('/login')

    form = CharacterForm()
    return render_to_response('jblux/character_edit.html', {'form': form},
            context_instance=RequestContext(request))

def logout(request):
    request.session.flush()
    return HttpResponseRedirect('/login')

def game(request):
    try:
        user = request.session['user']
    except KeyError:
        return HttpResponseRedirect('/login')

    try:
        character = request.session['character']
    except KeyError:
        return HttpResponseRedirect('/character/select')

    return render_to_response('jblux/game.html', {'character': character},
            context_instance=RequestContext(request))

def jnlp(request):
    try:
        user = request.session['user']
        character = request.session['character']
    except KeyError:
        return HttpResponseRedirect('/')

    template = loader.get_template('jblux/tmuo.jnlp')
    context = Context({'username': user.username, 'password': user.password,
        'charname': character})
    jnlp = template.render(context)
    response = HttpResponse(jnlp, mimetype='application/x-java-jnlp-file')
    response['Content-Disposition'] = 'attachement; filename=tmuo.jnlp'
    return response

def info(request):
    return render_to_response('jblux/info.html',
            context_instance=RequestContext(request))

def screens(request):
    return render_to_response('jblux/screens.html',
            context_instance=RequestContext(request))

def help(request):
    return render_to_response('jblux/help.html',
            context_instance=RequestContext(request))

def view_profile(request, account_id=0):
    if account_id == 0:
        account_id = request.session['user'].id

    user = get_object_or_404(User, pk=account_id)
    characters = Character.objects.filter(user=account_id)

    return render_to_response('jblux/account_profile.html', {'user': user,
            'chars': characters}, context_instance=RequestContext(request))

def view_char(request, char_id):
    character = get_object_or_404(Character, pk=char_id)
    return render_to_response('jblux/character.html', {'char': character},
            context_instance=RequestContext(request))

def select_character(request):
    if request.method == 'POST':
        character = Character.objects.get(id=request.POST['character'])
        request.session['character'] = character
        return HttpResponseRedirect('/game')
    else:
        try:
            user = request.session['user']
            form = SelectCharacterForm(user=user)
            return render_to_response('jblux/character_select.html', {'form': form},
                    context_instance=RequestContext(request))
        except KeyError:
            return HttpResponseRedirect('/login')

def profile_settings(request, account_id):
    # First make sure the logged inuser is allowed to 
    # change this user's settings
    user = get_object_or_404(User, pk=account_id)
    try:
        logged_in_user = request.session['user']
    except KeyError:
        return HttpResponseRedirect('/login')

    if logged_in_user != user:
        return HttpResponse('Not Authenticated')

    form = AccountSettingsForm()
    if request.method == 'POST':
        form = AccountSettingsForm(request.POST)
        if form.is_valid():
            password = form.cleaned_data['password']
            user.password = hashlib.sha1(password).hexdigest()
            user.save()
            return HttpResponseRedirect('/profile')

    return render_to_response('jblux/settings.html', {'user': user, 'form': form},
        context_instance=RequestContext(request))


def reset_pass(request):
    form = ResetPasswordForm()
    if request.method == 'POST':
        form = ResetPasswordForm(request.POST)
        if form.is_valid():
            email = form.cleaned_data['email']
            user = User.objects.get(email=email)
            password = util.gen_new_password()
            user.password = hashlib.sha1(password).hexdigest()
            user.save()
            email_new_pass(email, password)
            return HttpResponseRedirect('New password sent to '+email)

    return render_to_response('jblux/reset_pass.html', {'form': form},
        context_instance=RequestContext(request))


#Recieves hashed password
def auth(username, password):
    try:
        user = User.objects.get(username=username, password=password)
        return user
    except User.DoesNotExist:
        return None

