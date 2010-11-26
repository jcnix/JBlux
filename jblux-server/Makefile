CC=gcc
LD=ld
CFLAGS=-Wall -g

BINDIR=bin
ODIR=build
SRCDIR=src

_OBJ= 	main.o \
	  	client.o \
	  	json.o \
	 	map.o \
	  	npc.o \
		player.o \
		character.o \
	  	quest.o \
		world.o \
	  	db/db.o \
	  	db/user_tbl.o \
	  	db/map_tbl.o \
	  	db/npc_tbl.o \
		db/quest_tbl.o \
	  	util/base64.o \
		util/coords.o \
		util/jmath.o \
		util/relation.o

OBJ=$(patsubst %, $(ODIR)/%, $(_OBJ))
LIBS=-lm -lpthread -lpq -lyajl -lcrypto
OUTPUT=bin/jblux-server
.PHONY: default clean build-clean dist-clean

default: $(ODIR) $(BINDIR) $(OUTPUT)

$(OUTPUT): $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS) $(LIBS)

$(ODIR)/%.o: $(SRCDIR)/%.c
	$(CC) -c -o $@ $< $(CFLAGS) $(DEBUG)

$(ODIR):
	mkdir -p $(ODIR)
	mkdir -p $(ODIR)/db
	mkdir -p $(ODIR)/util

$(BINDIR):
	mkdir -p $(BINDIR)

clean: build-clean dist-clean

build-clean:
	rm -rf $(ODIR)

dist-clean:
	rm -f $(OUTPUT)

