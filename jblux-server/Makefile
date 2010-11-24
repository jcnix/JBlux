CC=gcc
LD=ld
CFLAGS=-Wall -g

BINDIR=bin
ODIR=build
SRCDIR=src

_OBJ= 	main.o \
	  	client.o \
	  	json.o \
	  	base64.o \
	 	map.o \
	  	npc.o \
		player.o \
		character.o \
	  	quest.o \
	  	relation.o \
		world.o \
	  	db/db.o \
	  	db/user_tbl.o \
	  	db/map_tbl.o \
	  	db/npc_tbl.o \
		db/quest_tbl.o

OBJ=$(patsubst %, $(ODIR)/%, $(_OBJ))
LIBS=-lpthread -lpq -lyajl -lcrypto
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

$(BINDIR):
	mkdir -p $(BINDIR)

clean: build-clean dist-clean

build-clean:
	rm -rf $(ODIR)

dist-clean:
	rm -f $(OUTPUT)

