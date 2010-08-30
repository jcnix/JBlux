CC=gcc
LD=ld
CFLAGS=-Wall -g -O2

BINDIR=bin
ODIR=build
SRCDIR=src

_OBJ= main.o \
	  client.o \
	  json.o \
	  base64.o \
	  db/db.o \
	  db/db_user_tbl.o \
	  db/db_map_tbl.o \
	  db/db_npc_tbl.o

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
	mkdir $(ODIR)
	mkdir $(ODIR)/db

$(BINDIR):
	mkdir $(BINDIR)

clean: build-clean dist-clean

build-clean:
	rm -rf $(ODIR)

dist-clean:
	rm -f $(OUTPUT)

