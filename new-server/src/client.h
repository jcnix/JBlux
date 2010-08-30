/*
 * File: command_parser.h
 * Author: Casey Jones
 */

#ifndef _COMMAND_PARSER_H
#define _COMMAND_PARSER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netinet/in.h>
#include "types.h"

#define BUFFSIZE 2048

struct client_t
{
    int sock;
    int connected;
    int authenticated;
    struct player_data data;
    char* encoded_player_data;
    struct coordinates_t coords;
};

struct client_list
{
    struct client_list *next;
    struct client_t *player;
};
struct client_list *clients;

void* client_thread(void* vsock);
void parse_command(int sock, struct client_t *client, char* command);

#endif

