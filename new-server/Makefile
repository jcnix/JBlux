CC=gcc
LD=ld
CFLAGS=-Wall -g -O2

ODIR=build
SRCDIR=src

_OBJ=main.o
OBJ=$(patsubst %, $(ODIR)/%, $(_OBJ))
LIBS=-lpthread
OUTPUT=bin/jblux-server

$(OUTPUT): $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS) $(LIBS)

$(ODIR)/%.o: $(SRCDIR)/%.c
	$(CC) -c -o $@ $< $(CFLAGS) $(DEBUG)

.PHONY: clean build-clean dist-clean

clean: build-clean dist-clean
	rm -rf bin/*

build-clean:
	rm -f $(ODIR)/*.o *~ core $(INCDIR)/*~

dist-clean:
	rm -f $(OBJ)

