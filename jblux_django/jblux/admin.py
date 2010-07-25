from jblux_django.jblux.models import User, Character, Inventory, Item, Map
from jblux_django.jblux.models import Race, Class, Poll, Choice
from django.contrib import admin

class ChoiceInline(admin.TabularInline):
    fields = ['choice']
    model = Choice
    extra = 2

class PollAdmin(admin.ModelAdmin):
    fields = ['pub_date', 'question']
    inlines = [ChoiceInline]

admin.site.register(User)
admin.site.register(Character)
admin.site.register(Inventory)
admin.site.register(Item)
admin.site.register(Map)
admin.site.register(Race)
admin.site.register(Class)
admin.site.register(Poll, PollAdmin)

