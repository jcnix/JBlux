from django.db import models

class User(models.Model):
    username = models.CharField(max_length=50)
    password = models.CharField(max_length=40) #SHA-1
    email = models.CharField(max_length=75)
    is_admin = models.BooleanField()
    is_active = models.BooleanField()
    character1 = models.ForeignKey('Character', null=True, related_name='char1')
    character2 = models.ForeignKey('Character', null=True, related_name='char2')
    character3 = models.ForeignKey('Character', null=True, related_name='char3')
    character4 = models.ForeignKey('Character', null=True, related_name='char4')
    character5 = models.ForeignKey('Character', null=True, related_name='char5')

    def __unicode__(self):
        return self.username

class Character(models.Model):
    name = models.CharField(max_length=50)
    level = models.IntegerField()
    inventory = models.ForeignKey('Inventory', related_name='items')

    def __unicode__(self):
        return self.name

class Inventory(models.Model):
    character = models.ForeignKey('Character', related_name='player')
    head = models.ForeignKey('Item', related_name='helmet')
    neck = models.ForeignKey('Item', related_name='necklace')
    chest = models.ForeignKey('Item', related_name='chest_armor')
    back = models.ForeignKey('Item', related_name='cape')
    waist = models.ForeignKey('Item', related_name='belt')
    hands = models.ForeignKey('Item', related_name='hands_armor')
    finger1 = models.ForeignKey('Item', related_name='finger1_item')
    finger2 = models.ForeignKey('Item', related_name='finger2_item')
    feet = models.ForeignKey('Item', related_name='feet_armor')
    main_hand = models.ForeignKey('Item', related_name='main_hand_weapon')
    off_hand = models.ForeignKey('Item', related_name='off_hand_item')
    ranged = models.ForeignKey('Item', related_name='ranged_weapon')
    slot1 = models.ForeignKey('Item', related_name='s1')
    slot2 = models.ForeignKey('Item', related_name='s2')
    slot3 = models.ForeignKey('Item', related_name='s3')
    slot4 = models.ForeignKey('Item', related_name='s4')
    slot5 = models.ForeignKey('Item', related_name='s5')
    slot6 = models.ForeignKey('Item', related_name='s6')
    slot7 = models.ForeignKey('Item', related_name='s7')
    slot8 = models.ForeignKey('Item', related_name='s8')
    slot9 = models.ForeignKey('Item', related_name='s9')
    slot10 = models.ForeignKey('Item', related_name='s10')
    slot11 = models.ForeignKey('Item', related_name='s11')
    slot12 = models.ForeignKey('Item', related_name='s12')
    slot13 = models.ForeignKey('Item', related_name='s13')
    slot14 = models.ForeignKey('Item', related_name='s14')
    slot15 = models.ForeignKey('Item', related_name='s15')
    slot16 = models.ForeignKey('Item', related_name='s16')
    slot17 = models.ForeignKey('Item', related_name='s17')
    slot18 = models.ForeignKey('Item', related_name='s18')
    slot19 = models.ForeignKey('Item', related_name='s19')
    slot20 = models.ForeignKey('Item', related_name='s20')
    slot21 = models.ForeignKey('Item', related_name='s21')
    slot22 = models.ForeignKey('Item', related_name='s22')
    slot23 = models.ForeignKey('Item', related_name='s23')
    slot24 = models.ForeignKey('Item', related_name='s24')
    slot25 = models.ForeignKey('Item', related_name='s25')

    def __unicode__(self):
        return self.character

class Item(models.Model):
    types = models.IntegerField()
    subtype = models.IntegerField()
    name = models.CharField(max_length=75)
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
    stat_type1 = models.IntegerField()
    stat_value1 = models.IntegerField()
    stat_type2 = models.IntegerField()
    stat_value2 = models.IntegerField()
    stat_type3 = models.IntegerField()
    stat_value3 = models.IntegerField()
    stat_type4 = models.IntegerField()
    stat_value4 = models.IntegerField()
    stat_type5 = models.IntegerField()
    stat_value5 = models.IntegerField()
    stat_type6 = models.IntegerField()
    stat_value6 = models.IntegerField()
    stat_type7 = models.IntegerField()
    stat_value7 = models.IntegerField()
    stat_type8 = models.IntegerField()
    stat_value8 = models.IntegerField()
    stat_type9 = models.IntegerField()
    stat_value9 = models.IntegerField()
    stat_type10 = models.IntegerField()
    stat_value10 = models.IntegerField()
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
