from django.conf.urls.defaults import *
from django.contrib import admin
from django.conf import settings

admin.autodiscover()

urlpatterns = patterns('',
    (r'^$', 'jblux_django.jblux.views.index'),
    (r'^index/$', 'jblux_django.jblux.views.index'),
    (r'^game/$', 'jblux_django.jblux.views.game'),
    (r'^jnlp/$', 'jblux_django.jblux.views.jnlp'),
    (r'^login/$', 'jblux_django.jblux.views.login'),
    (r'^logout/$', 'jblux_django.jblux.views.logout'),
    (r'^profile/$', 'jblux_django.jblux.views.view_profile'),
    (r'^profile/(?P<account_id>\d+)/$', 'jblux_django.jblux.views.view_profile'),
    (r'^profile/(?P<account_id>\d+)/settings/$', 'jblux_django.jblux.views.profile_settings'),
    (r'^character/(?P<char_id>\d+)/$', 'jblux_django.jblux.views.view_char'),
    (r'^register/$', 'jblux_django.jblux.views.register'),
    (r'^register/(?P<reg_num>\w{40})/$', 'jblux_django.jblux.views.activate_account'),
    (r'^register/new/$', 'jblux_django.jblux.views.register_new_user'),
    (r'^register/reset_password/$', 'jblux_django.jblux.views.reset_pass'),
    (r'^character/new/$', 'jblux_django.jblux.views.new_character'),
    (r'^character/select/$', 'jblux_django.jblux.views.select_character'),
    (r'^info/$', 'jblux_django.jblux.views.info'),
    (r'^screens/$', 'jblux_django.jblux.views.screens'),
    (r'^help/$', 'jblux_django.jblux.views.help'),

    (r'^polls/$', 'jblux_django.jblux.polls.polls'),
    (r'^polls/(?P<poll_id>\d+)/$', 'jblux_django.jblux.polls.detail'),
    (r'^polls/(?P<poll_id>\d+)/results/$', 'jblux_django.jblux.polls.results'),
    (r'^polls/(?P<poll_id>\d+)/vote/$', 'jblux_django.jblux.polls.vote'),
    (r'^news/(?P<news_id>\d+)/$', 'jblux_django.jblux.news.view'),
    (r'^admin/', include(admin.site.urls)),
    (r'^favicon\.ico$', 'django.views.generic.simple.redirect_to', {'url': '/tmuo_media/favicon.ico'}),
)

if settings.DEBUG:
    urlpatterns += patterns('',
        (r'^tmuo_media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': settings.MEDIA_ROOT}),
    )

