from jblux_django.jblux.models import User, Character, Inventory, Item, Map
from jblux_django.jblux.models import MapItems, MapNpcs, Npc, Quest
from jblux_django.jblux.models import Race, Class, Poll, Choice
from django.contrib import admin

class UserAdmin(admin.ModelAdmin):
    search_fields = ['username']

class InventoryInline(admin.StackedInline):
    model = Inventory
    extra = 0
    max_num = 1
    fields = Inventory._meta.get_all_field_names()
    #Remove non foreign keys
    fields.remove("id")
    fields.remove("character")
    fields.remove("items")
    raw_id_fields = fields

class CharacterAdmin(admin.ModelAdmin):
    search_fields = ['name']
    inlines = [InventoryInline]

class NpcAdmin(admin.ModelAdmin):
    search_fields = ['name']

class QuestAdmin(admin.ModelAdmin):
    search_fields = ['name', 'npc__name']

class ItemAdmin(admin.ModelAdmin):
    search_fields = ['name']

class MapItemInline(admin.TabularInline):
    model = MapItems
    extra = 0

class MapNpcInline(admin.TabularInline):
    model = MapNpcs
    extra = 0

class MapAdmin(admin.ModelAdmin):
    search_fields = ['name']
    inlines = [MapItemInline, MapNpcInline]

class ChoiceInline(admin.TabularInline):
    fields = ['choice']
    model = Choice
    extra = 2

class PollAdmin(admin.ModelAdmin):
    fields = ['pub_date', 'question']
    inlines = [ChoiceInline]

admin.site.register(User, UserAdmin)
admin.site.register(Character, CharacterAdmin)
#admin.site.register(Inventory)
admin.site.register(Item, ItemAdmin)
admin.site.register(Map, MapAdmin)
admin.site.register(Npc, NpcAdmin)
admin.site.register(Quest, QuestAdmin)
admin.site.register(Race)
admin.site.register(Class)
admin.site.register(Poll, PollAdmin)

