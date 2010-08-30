CC=gcc
LD=ld
CFLAGS=-Wall -g -O2

BINDIR=bin
ODIR=build
SRCDIR=src

_OBJ= main.o \
	  client.o \
	  db.o \
	  db_user_tbl.o \
	  db_map_tbl.o \
	  json.o \
	  base64.o
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

$(BINDIR):
	mkdir $(BINDIR)

clean: build-clean dist-clean
	rm -rf bin/*

build-clean:
	rm -f $(ODIR)/*.o *~ core $(INCDIR)/*~

dist-clean:
	rm -f $(OBJ)

