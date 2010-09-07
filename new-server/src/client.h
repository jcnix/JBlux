/*
 * File: client.h
 * Author: Casey Jones
 */

#ifndef _CLIENT_H
#define _CLIENT_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netinet/in.h>
#include <pthread.h>
#include "base64.h"
#include "db/db_user_tbl.h"
#include "json.h"
#include "types.h"

#define BUFFSIZE 1024

struct client_t
{
    int socket;
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

void handle_client(int* sock);
void* client_thread(void* vsock);
void send_player_data(struct client_t *client, char* char_name);
void parse_command(struct client_t *client, char* command);

#endif

