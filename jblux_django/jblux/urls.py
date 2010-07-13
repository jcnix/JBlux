from django.conf.urls.defaults import *
from django.conf import settings

urlpatterns = patterns('jblux_django',
    (r'^$', 'jblux.views.index'),
    (r'^index/$', 'jblux.views.index'),
    (r'^game/$', 'jblux.views.game'),
    (r'^login/$', 'jblux.views.login'),
    (r'^logout/$', 'jblux.views.logout'),
    (r'^register/$', 'jblux.views.register'),
    (r'^register/new/$', 'jblux.views.register_new_user'),
    (r'^info/$', 'jblux.views.info'),
    (r'^screens/$', 'jblux.views.screens'),
    (r'^help/$', 'jblux.views.help'),
    (r'^polls/$', 'jblux.views.polls'),
)

