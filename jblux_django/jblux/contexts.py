from jblux_django.jblux.models import User

def my_user(request):
    try:
        my_user = request.session['user']
        return {'my_user': my_user}
    except KeyError:
        return {}

