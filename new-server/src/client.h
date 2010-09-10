/*
 * File: client.h
 * Author: Casey Jones
 */

#ifndef _CLIENT_H
#define _CLIENT_H

#define _GNU_SOURCE
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

#define MAX_CLIENTS 200 
#define BUFFSIZE 1024

struct client_t
{
    int socket;
    int connected;
    int authenticated;
    struct player_data *data;
    char* encoded_player_data;
    struct coordinates_t coords;
};

void handle_client(int* sock);
void* client_thread(void* vsock);
void send_player_data_to_self(struct client_t *client, char* char_name);
void move_client(struct client_t *client);
void add_player_to_map(struct client_t *client, char* map,
        struct coordinates_t coords);
void tell_all_players_on_map(int map_id, char* command);
void parse_command(struct client_t *client, char* command);

/* this just eliminates the length param and uses strlen() */
int esend(int socket, char* message);

#endif

