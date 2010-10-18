from jblux_django.jblux.models import User, Character, Inventory, Item, Map
from jblux_django.jblux.models import MapItems, MapNpcs, Npc, Quest
from jblux_django.jblux.models import Race, Class, Poll, Choice, NewsPost
from django.contrib import admin

class UserAdmin(admin.ModelAdmin):
    search_fields = ['username']
    list_display = ('__unicode__', 'is_active')
    list_filter = ('is_admin', 'is_active')

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
    list_display = ('__unicode__', 'level', 'race', 'class_t')
    list_filter = ('race', 'class_t')
    inlines = [InventoryInline]
    raw_id_fields = ['user', 'inventory']

class NpcAdmin(admin.ModelAdmin):
    search_fields = ['name']
    list_display = ('__unicode__', 'level', 'race', 'class_t')
    list_filter = ('race', 'class_t')

class QuestAdmin(admin.ModelAdmin):
    search_fields = ['name', 'npc__name']
    list_display = ('__unicode__', 'min_level', 'flag', 'npc')
    list_filter = ('flag',)
    raw_id_fields = ['npc', 'end_npc', 'next_quest', 'quest_item',
            'rewarditem1', 'rewarditem2', 'rewarditem3',
            'reqitem1', 'reqitem2', 'reqitem3', 'reqnpc1', 'reqnpc2',
            'reqnpc3',]

class ItemAdmin(admin.ModelAdmin):
    search_fields = ['name']
    list_display = ('__unicode__', 'itemlevel')

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
admin.site.register(Item, ItemAdmin)
admin.site.register(Map, MapAdmin)
admin.site.register(Npc, NpcAdmin)
admin.site.register(Quest, QuestAdmin)
admin.site.register(Race)
admin.site.register(Class)
admin.site.register(Poll, PollAdmin)
admin.site.register(NewsPost)

