bin_PROGRAMS = jblux_server

jblux_server_CFLAGS=$(CRYPTO_CFLAGS) $(SSL_CFLAGS) $(ZLIB_CFLAGS)
jblux_server_LDADD=$(CRYPTO_LIBS) $(SSL_LIBS) $(ZLIB_LIBS)

jblux_server_SOURCES = \
					   ../src/main.c \
					   ../src/client.c \
					   ../src/map.c \
					   ../src/npc.c \
					   ../src/player.c \
					   ../src/character.c \
					   ../src/quest.c \
					   ../src/world.c \
					   ../src/db/db.c \
					   ../src/db/user_tbl.c \
					   ../src/db/map_tbl.c \
					   ../src/db/npc_tbl.c \
					   ../src/db/quest_tbl.c \
					   ../src/rules/combat.c \
					   ../src/util/base64.c \
					   ../src/util/coords.c \
					   ../src/util/jbm.c \
					   ../src/util/jmath.c \
					   ../src/util/json.c \
					   ../src/util/relation.c

noinst_HEADERS = \
				 ../src/client.h \
				 ../src/map.h \
				 ../src/map_t.h \
				 ../src/npc.h \
				 ../src/player.h \
				 ../src/player_data.h \
				 ../src/character.h \
				 ../src/item.h \
				 ../src/inventory.h \
				 ../src/quest.h \
				 ../src/world.h \
				 ../src/db/db.h \
				 ../src/db/user_tbl.h \
				 ../src/db/map_tbl.h \
				 ../src/db/npc_tbl.h \
				 ../src/db/quest_tbl.h \
				 ../src/rules/combat.h \
				 ../src/util/base64.h \
				 ../src/util/coords.h \
				 ../src/util/jbm.h \
				 ../src/util/jmath.h \
				 ../src/util/json.h \
				 ../src/util/relation.h

