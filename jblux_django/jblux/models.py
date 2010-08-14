from django.db import models
from django.core.validators import MinLengthValidator

class User(models.Model):
    min_name = MinLengthValidator(3)
    username = models.CharField(max_length=50, validators=[min_name], unique=True)
    password = models.CharField(max_length=40) #SHA-1
    email = models.CharField(max_length=75, unique=True)
    is_admin = models.BooleanField()
    is_active = models.BooleanField()

    def __unicode__(self):
        return self.username

class Character(models.Model):
    name = models.CharField(max_length=50, unique=True)
    user = models.ForeignKey('User')
    race = models.ForeignKey('Race')
    class_t = models.ForeignKey('Class')
    level = models.IntegerField(default=1)
    inventory = models.ForeignKey('Inventory', blank=True, null=True, related_name='items')
    strength = models.IntegerField()
    agility = models.IntegerField()
    stamina = models.IntegerField()
    intelligence = models.IntegerField()
    spirit = models.IntegerField()
    current_map = models.ForeignKey('Map')
    x_coord = models.IntegerField()
    y_coord = models.IntegerField()

    def __unicode__(self):
        return self.name

class Race(models.Model):
    name = models.CharField(max_length=25)
    sprite_sheet = models.CharField(max_length=100)
    sprite_height = models.IntegerField()

    def __unicode__(self):
        return self.name

class Class(models.Model):
    name = models.CharField(max_length=25)

    def __unicode__(self):
        return self.name

class Inventory(models.Model):
    character = models.ForeignKey('Character', blank=True, null=True, related_name='player')
    head = models.ForeignKey('Item', blank=True, null=True, related_name='helmet')
    neck = models.ForeignKey('Item', blank=True, null=True, related_name='necklace')
    chest = models.ForeignKey('Item', blank=True, null=True, related_name='chest_armor')
    back = models.ForeignKey('Item', blank=True, null=True, related_name='cape')
    waist = models.ForeignKey('Item', blank=True, null=True, related_name='belt')
    hands = models.ForeignKey('Item', blank=True, null=True, related_name='hands_armor')
    finger1 = models.ForeignKey('Item', blank=True, null=True, related_name='finger1_item')
    finger2 = models.ForeignKey('Item', blank=True, null=True, related_name='finger2_item')
    feet = models.ForeignKey('Item', blank=True, null=True, related_name='feet_armor')
    main_hand = models.ForeignKey('Item', blank=True, null=True, related_name='main_hand_weapon')
    off_hand = models.ForeignKey('Item', blank=True, null=True, related_name='off_hand_item')
    ranged = models.ForeignKey('Item', blank=True, null=True, related_name='ranged_weapon')
    slot1 = models.ForeignKey('Item', blank=True, null=True, related_name='s1')
    slot2 = models.ForeignKey('Item', blank=True, null=True, related_name='s2')
    slot3 = models.ForeignKey('Item', blank=True, null=True, related_name='s3')
    slot4 = models.ForeignKey('Item', blank=True, null=True, related_name='s4')
    slot5 = models.ForeignKey('Item', blank=True, null=True, related_name='s5')
    slot6 = models.ForeignKey('Item', blank=True, null=True, related_name='s6')
    slot7 = models.ForeignKey('Item', blank=True, null=True, related_name='s7')
    slot8 = models.ForeignKey('Item', blank=True, null=True, related_name='s8')
    slot9 = models.ForeignKey('Item', blank=True, null=True, related_name='s9')
    slot10 = models.ForeignKey('Item', blank=True, null=True, related_name='s10')
    slot11 = models.ForeignKey('Item', blank=True, null=True, related_name='s11')
    slot12 = models.ForeignKey('Item', blank=True, null=True, related_name='s12')
    slot13 = models.ForeignKey('Item', blank=True, null=True, related_name='s13')
    slot14 = models.ForeignKey('Item', blank=True, null=True, related_name='s14')
    slot15 = models.ForeignKey('Item', blank=True, null=True, related_name='s15')
    slot16 = models.ForeignKey('Item', blank=True, null=True, related_name='s16')
    slot17 = models.ForeignKey('Item', blank=True, null=True, related_name='s17')
    slot18 = models.ForeignKey('Item', blank=True, null=True, related_name='s18')
    slot19 = models.ForeignKey('Item', blank=True, null=True, related_name='s19')
    slot20 = models.ForeignKey('Item', blank=True, null=True, related_name='s20')
    slot21 = models.ForeignKey('Item', blank=True, null=True, related_name='s21')
    slot22 = models.ForeignKey('Item', blank=True, null=True, related_name='s22')
    slot23 = models.ForeignKey('Item', blank=True, null=True, related_name='s23')
    slot24 = models.ForeignKey('Item', blank=True, null=True, related_name='s24')
    slot25 = models.ForeignKey('Item', blank=True, null=True, related_name='s25')

    def __unicode__(self):
        return self.character

class QuestLog(models.Model):
    character = models.ForeignKey('Character')
    quest = models.ForeignKey('Quest')
    status = models.IntegerField()

class Item(models.Model):
    types = models.IntegerField()
    subtype = models.IntegerField()
    name = models.CharField(max_length=75, unique=True)
    description = models.CharField(max_length=255)
    diplayimg = models.CharField(max_length=255)
    quality = models.IntegerField()
    buyprice = models.IntegerField()
    sellprice = models.IntegerField()
    allowableclass = models.IntegerField()
    allowablerace = models.IntegerField()
    itemlevel = models.IntegerField()
    requiredlevel = models.IntegerField()
    RequiredFaction = models.IntegerField()
    RequiredFactionStanding = models.IntegerField()
    isUnique = models.BooleanField()
    maxstack = models.IntegerField()
    itemstatscount = models.IntegerField()
    stat_type1 = models.IntegerField(blank=True, null=True, )
    stat_value1 = models.IntegerField(blank=True, null=True, )
    stat_type2 = models.IntegerField(blank=True, null=True, )
    stat_value2 = models.IntegerField(blank=True, null=True, )
    stat_type3 = models.IntegerField(blank=True, null=True, )
    stat_value3 = models.IntegerField(blank=True, null=True, )
    stat_type4 = models.IntegerField(blank=True, null=True, )
    stat_value4 = models.IntegerField(blank=True, null=True, )
    stat_type5 = models.IntegerField(blank=True, null=True, )
    stat_value5 = models.IntegerField(blank=True, null=True, )
    stat_type6 = models.IntegerField(blank=True, null=True, )
    stat_value6 = models.IntegerField(blank=True, null=True, )
    stat_type7 = models.IntegerField(blank=True, null=True, )
    stat_value7 = models.IntegerField(blank=True, null=True, )
    stat_type8 = models.IntegerField(blank=True, null=True, )
    stat_value8 = models.IntegerField(blank=True, null=True, )
    stat_type9 = models.IntegerField(blank=True, null=True, )
    stat_value9 = models.IntegerField(blank=True, null=True, )
    stat_type10 = models.IntegerField(blank=True, null=True, )
    stat_value10 = models.IntegerField(blank=True, null=True, )
    dmg_min = models.FloatField()
    dmg_max = models.FloatField()
    dmg_type = models.IntegerField()
    armor = models.IntegerField()
    water_res = models.IntegerField()
    fire_res = models.IntegerField()
    nature_res = models.IntegerField()
    frost_res = models.IntegerField()
    shadow_res = models.IntegerField()
    arcane_res = models.IntegerField()
    delay = models.FloatField()
    ammo_type = models.IntegerField()
    attack_range = models.FloatField()
    spellid = models.IntegerField()
    spelltrigger = models.IntegerField()
    spellcharges = models.IntegerField()
    spellcooldown = models.IntegerField()
    quest_item = models.BooleanField()
    quest_id = models.IntegerField()
    block_amount = models.IntegerField()
    itemset = models.IntegerField()
    MaxDurability = models.IntegerField()
    ReqDisenchantSkill = models.IntegerField()
    ArmorDamageModifier = models.IntegerField()

    def __unicode__(self):
        return self.name

class Npc(models.Model):
    name = models.CharField(max_length=50)
    race = models.ForeignKey('Race')
    class_t = models.ForeignKey('Class')
    job = models.IntegerField()
    sprite_sheet = models.CharField(max_length=50,null=True, blank=True)

    def __unicode__(self):
        return self.name

class Quest(models.Model):
    name = models.CharField(max_length=50)
    details = models.CharField(max_length=500)
    objectives = models.CharField(max_length=100)
    completion_text = models.CharField(max_length=500)
    npc = models.ForeignKey('Npc', related_name='quest_giver')
    min_level = models.IntegerField()
    flag = models.IntegerField()
    next_quest = models.ForeignKey('Quest', null=True, blank=True)
    quest_item = models.ForeignKey('Item', null=True, blank=True, related_name='src_item')
    reward_xp = models.IntegerField()
    reward_money = models.IntegerField()
    rewardItem1 = models.ForeignKey('Item', null=True, blank=True, related_name='reward_item1')
    rewardItem1_count = models.IntegerField(null=True, blank=True)
    rewardItem2 = models.ForeignKey('Item', null=True, blank=True, related_name='reward_itme1')
    rewardItem2_count = models.IntegerField(null=True, blank=True)
    reqItem1 = models.ForeignKey('Item', null=True, blank=True, related_name='req_item1')
    reqItem1_count = models.IntegerField(null=True, blank=True)
    reqItem2 = models.ForeignKey('Item', null=True, blank=True, related_name='req_item2')
    reqItem2_count = models.IntegerField(null=True, blank=True)
    reqItem3 = models.ForeignKey('Item', null=True, blank=True, related_name='req_item3')
    reqItem3_count = models.IntegerField(null=True, blank=True)
    reqNpc1 = models.ForeignKey('Npc', null=True, blank=True, related_name='req_npc1')
    reqNpc1_count = models.IntegerField(null=True, blank=True)
    reqNpc2 = models.ForeignKey('Npc', null=True, blank=True, related_name='req_npc2')
    reqNpc2_count = models.IntegerField(null=True, blank=True)
    reqNpc3 = models.ForeignKey('Npc', null=True, blank=True, related_name='req_npc3')
    reqNpc3_count = models.IntegerField(null=True, blank=True)

    def __unicode__(self):
        return self.name

class Map(models.Model):
    name = models.CharField(max_length=50, unique=True)
    map_left = models.ForeignKey('Map', blank=True, null=True, related_name='m_left')
    map_right = models.ForeignKey('Map', blank=True, null=True, related_name='m_right')
    map_top = models.ForeignKey('Map', blank=True, null=True, related_name='m_above')
    map_bottom = models.ForeignKey('Map', blank=True, null=True, related_name='m_below')
    entrance_left_x = models.IntegerField(blank=True, null=True)
    entrance_left_y = models.IntegerField(blank=True, null=True)
    entrance_right_x = models.IntegerField(blank=True, null=True)
    entrance_right_y = models.IntegerField(blank=True, null=True)
    entrance_top_x = models.IntegerField(blank=True, null=True)
    entrance_top_y = models.IntegerField(blank=True, null=True)
    entrance_bottom_x = models.IntegerField(blank=True, null=True)
    entrance_bottom_y = models.IntegerField(blank=True, null=True)

    def __unicode__(self):
        return self.name

class MapItems(models.Model):
    #Working around the word map being a python keyword
    map_t = models.ForeignKey('Map')
    item = models.ForeignKey('Item')
    x_coord = models.IntegerField()
    y_coord = models.IntegerField()

    def __unicode__(self):
        return str(self.item.__unicode__() + '@' + str(self.x_coord) + ',' + str(self.y_coord))

class MapNpcs(models.Model):
    map_t = models.ForeignKey('Map')
    npc = models.ForeignKey('Npc')
    direction = models.CharField(max_length=6)
    x_coord = models.IntegerField()
    y_coord = models.IntegerField()

    def __unicode__(self):
        return str(self.npc.__unicode__() + '@' + str(self.x_coord) + ',' + str(self.y_coord))

#Polls
class Poll(models.Model):
    question = models.CharField(max_length=200)
    pub_date = models.DateTimeField('date published')

    def __unicode__(self):
        return self.question

class Choice(models.Model):
    poll = models.ForeignKey(Poll)
    choice = models.CharField(max_length=200)
    votes = models.IntegerField(default=0)

    def __unicode__(self):
        return self.choice

