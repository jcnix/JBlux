from django.http import HttpResponse
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext
from jblux_django.jblux.models import NewsPost

def view(request, news_id):
    post = get_object_or_404(NewsPost, pk=news_id)
    return render_to_response('news/view.html', {'post': post},
            context_instance=RequestContext(request))

