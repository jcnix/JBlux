from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.core.urlresolvers import reverse
from django.template import RequestContext
from jblux.models import Poll, Choice

def polls(request):
    latest_poll = Poll.objects.all().order_by('pub_date')[:1]
    poll_list = Poll.objects.all().order_by('pub_date')[:10]
    args = {'latest_poll': latest_poll, 'poll_list': poll_list}
    return render_to_response('jblux/polls/index.html', args,
            context_instance=RequestContext(request))

def detail(request, poll_id):
    poll = get_object_or_404(Poll, pk=poll_id)
    return render_to_response('jblux/polls/detail.html', {'poll': poll},
            context_instance=RequestContext(request))

def results(request, poll_id):
    p = get_object_or_404(Poll, pk=poll_id)
    return render_to_response('jblux/polls/results.html', {'poll': p},
            context_instance=RequestContext(request))

def vote(request, poll_id):
    p = get_object_or_404(Poll, pk=poll_id)
    try:
        selected_choice = p.choice_set.get(pk=request.POST['choice'])
    except (KeyError, Choice.DoesNotExist):
        return render_to_response('jblux/polls/detail.html', {'poll': p, 'error_message': "You didn't select anything"},
                context_instance=RequestContext(request))
    else:
        selected_choice.votes += 1
        selected_choice.save()
        return HttpResponseRedirect(reverse('jblux_django.jblux.polls.results', args=(p.id,)))

