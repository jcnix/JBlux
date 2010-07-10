from django.conf.urls.defaults import *
from django.contrib import admin
from django.conf import settings

admin.autodiscover()

urlpatterns = patterns('',
    (r'^$', 'jblux_django.jblux.views.index'),
    (r'^jblux/', include('jblux_django.jblux.urls')),
    (r'^admin/', include(admin.site.urls)),
)

urlpatterns += patterns('',
    (r'^tmuo_media/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': settings.MEDIA_ROOT})
)

