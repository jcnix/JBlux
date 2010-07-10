from django.conf.urls.defaults import *

urlpatterns = patterns('jblux_django',
    (r'^$', 'jblux.views.index'),
    (r'^index/$', 'jblux.views.index'),
    (r'^login/$', 'jblux.views.login'),
    (r'^logout/$', 'jblux.views.logout'),
    (r'^register/$', 'jblux.views.register'),
    (r'^register/new/$', 'jblux.views.register_new_user'),
)

