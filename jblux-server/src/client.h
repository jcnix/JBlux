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
#include "db/user_tbl.h"
#include "json.h"
#include "map.h"
#include "player.h"
#include "types.h"
#include "util/base64.h"
#include "util/relation.h"

#define MAX_CLIENTS 200 
#define BUFFSIZE 1024

struct client_t
{
    int socket;
    int connected;
    int authenticated;
    int user_id;
    struct player_data *data;
    char* encoded_player_data;
    struct coordinates_t coords;
};

struct client_list
{
    struct client_t *client;
    struct client_list *next;
};

void handle_client(int* sock);
void* client_thread(void* vsock);
int send_player_data_to_self(struct client_t *client, char* char_name);
void send_map_info(struct client_t *client, struct map_t *map_st);
void move_client(struct client_t *client, struct coordinates_t coords);
void add_player_to_map(struct client_t *client, char* map,
        struct coordinates_t coords);
void rm_player_from_map(struct client_t *client);
void send_chat_message(struct client_t *from, char* message);
void tell_all_players_on_map(int from_socket, int map_id, char* command);
void parse_command(struct client_t *client, char* command);

void cleanup();
int client_list_size(struct client_list *clients);

#endif

