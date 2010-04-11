CREATE TABLE items (
      id serial UNIQUE NOT NULL,
      class smallint NOT NULL default '0',
      subclass smallint NOT NULL default '0',
      name varchar(255) NOT NULL,
      displayid varchar(255) NOT NULL default '0',
      quality smallint NOT NULL default '0',
      buyprice integer NOT NULL default '0',
      sellprice integer NOT NULL default '0',
      allowableclass smallint NOT NULL default '0',
      allowablerace smallint NOT NULL default '0',
      itemlevel smallint NOT NULL default '0',
      requiredlevel smallint NOT NULL default '0',
      RequiredFaction smallint NOT NULL default '0',
      RequiredFactionStanding smallint NOT NULL default '0',
      isUnique smallint NOT NULL default '0',
      maxstack smallint NOT NULL default '20',
      itemstatscount smallint NOT NULL default '10',
      stat_type1 smallint NOT NULL default '0',
      stat_value1 smallint NOT NULL default '0',
      stat_type2 smallint NOT NULL default '0',
      stat_value2 smallint NOT NULL default '0',
      stat_type3 smallint NOT NULL default '0',
      stat_value3 smallint NOT NULL default '0',
      stat_type4 smallint NOT NULL default '0',
      stat_value4 smallint NOT NULL default '0',
      stat_type5 smallint NOT NULL default '0',
      stat_value5 smallint NOT NULL default '0',
      stat_type6 smallint NOT NULL default '0',
      stat_value6 smallint NOT NULL default '0',
      stat_type7 smallint NOT NULL default '0',
      stat_value7 smallint NOT NULL default '0',
      stat_type8 smallint NOT NULL default '0',
      stat_value8 smallint NOT NULL default '0',
      stat_type9 smallint NOT NULL default '0',
      stat_value9 smallint NOT NULL default '0',
      stat_type10 smallint NOT NULL default '0',
      stat_value10 smallint NOT NULL default '0',
      dmg_min real NOT NULL default '0',
      dmg_max real NOT NULL default '0',
      dmg_type smallint NOT NULL default '0',
      armor smallint NOT NULL default '0',
      water_res smallint NOT NULL default '0',
      fire_res smallint NOT NULL default '0',
      nature_res smallint NOT NULL default '0',
      frost_res smallint NOT NULL default '0',
      shadow_res smallint NOT NULL default '0',
      arcane_res smallint NOT NULL default '0',
      delay smallint NOT NULL default '0',
      ammo_type smallint NOT NULL default '0',
      range real NOT NULL default '0',
      spellid_1 smallint NOT NULL default '0',
      spelltrigger_1 smallint NOT NULL default '0',
      spellcharges_1 smallint NOT NULL default '0',
      spellcooldown_1 smallint NOT NULL default '0',
      description varchar(255) NOT NULL default '',
      quest_item smallint NOT NULL default '0',
      quest_id smallint NOT NULL default '0',
      block_amount smallint NOT NULL default '0',
      itemset smallint NOT NULL default '0',
      MaxDurability smallint NOT NULL default '0',
      ReqDisenchantSkill smallint NOT NULL default '-1',
      ArmorDamageModifier smallint NOT NULL default '0'
);

