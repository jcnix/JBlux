from django.conf.urls.defaults import *
from django.conf import settings

urlpatterns = patterns('jblux_django',
    (r'^$', 'jblux.views.index'),
    (r'^index/$', 'jblux.views.index'),
    (r'^game/$', 'jblux.views.game'),
    (r'^jnlp/$', 'jblux.views.jnlp'),
    (r'^login/$', 'jblux.views.login'),
    (r'^logout/$', 'jblux.views.logout'),
    (r'^profile/$', 'jblux.views.view_profile'),
    (r'^profile/(?P<account_id>\d+)/$', 'jblux.views.view_profile'),
    (r'^character/(?P<char_id>\d+)/$', 'jblux.views.view_char'),
    (r'^register/$', 'jblux.views.register'),
    (r'^register/new/$', 'jblux.views.register_new_user'),
    (r'^character/new/$', 'jblux.views.new_character'),
    (r'^character/select/$', 'jblux.views.select_character'),
    (r'^info/$', 'jblux.views.info'),
    (r'^screens/$', 'jblux.views.screens'),
    (r'^help/$', 'jblux.views.help'),

    (r'^polls/$', 'jblux.polls.polls'),
    (r'^polls/(?P<poll_id>\d+)/$', 'jblux.polls.detail'),
    (r'^polls/(?P<poll_id>\d+)/results/$', 'jblux.polls.results'),
    (r'^polls/(?P<poll_id>\d+)/vote/$', 'jblux.polls.vote'),
)

