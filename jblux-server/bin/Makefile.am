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
    ../src/util/base64.c \
    ../src/util/coords.c \
    ../src/util/jmath.c \
    ../src/util/json.c \
    ../src/util/relation.c

