/*
 * File: client.c
 * Author: Casey Jones
 */

#include "client.h"

static struct client_list *clients; 
static int num_clients = 0;

void handle_client(int* sock)
{
    pthread_t thread;
    pthread_create(&thread, NULL, client_thread, (void*) sock);
    pthread_detach(thread);
}

void* client_thread(void* vsock)
{
    int* sock = (int*) vsock;
    char buffer[BUFFSIZE];
    int received = -1;

    struct client_t *client = malloc(sizeof(struct client_t));
    if(!client)
    {
        /* We're out of memory, you can't play */
        close(*sock);
        pthread_exit(NULL);
        return 0;
    }

    client->connected = 1;
    client->socket = *sock;

    add_client(&clients, client);
    num_clients++;

    while(client->connected)
    {
        if((received = recv(*sock, buffer, BUFFSIZE, 0)) <= 0)
        {
            client->connected = 0;
            break;
        }

        parse_command(client, buffer);
        memset(buffer, 0, BUFFSIZE);
    }

    close(*sock);
    remove_client_from_list(&clients, client);
    pthread_exit(NULL);
    return 0;
}

void send_player_data_to_self(struct client_t *client, char* char_name)
{
    struct player_data *data = db_get_player(char_name);
    client->data = data;
    char* data_json = player_data_to_json(data);
    char* data_enc = base64_encode(data_json);
    client->encoded_player_data = data_enc;

    char* c1 = "player self";
    char* command = malloc(strlen(c1) + strlen(data_enc) + 2);
    sprintf(command, "%s %s", c1, data_enc);
    esend(client->socket, command);

    free(command);
    free(data_json);
}

void move_client(struct client_t *client, struct coordinates_t coords)
{
    client->data->coords.x = coords.x;
    client->data->coords.y = coords.y;
    char* command;
    if(!asprintf(&command, "move %s %d %d", client->data->character_name,
                client->data->coords.x, client->data->coords.y))
    {
        return;
    }

    tell_all_players_on_map(client, client->data->map_id, command);

    free(command);
}

void add_player_to_map(struct client_t *client, char* map,
        struct coordinates_t coords)
{
    if(map == NULL)
    {
        char* command = "map stay";
        esend(client->socket, command);
        return;
    }

    struct map_t *map_st = get_map_for_name(map);
    db_set_map_for_player(client->data->character_id, map_st->id, coords);
    client->data->map_id = map_st->id;
    client->data->map = map_st->name;
    client->data->coords = coords;

    /* TODO: get NPCs and Items and send to player */
    struct npc_list *npcs = map_st->npcs;
    char* npc_enc = base64_encode(npc_list_to_json(npcs));
    char* command = NULL;
    if(!asprintf(&command, "map goto %s %d %d npcs %s", map, coords.x, coords.y,
                npc_enc))
    {
        return;
    }
    esend(client->socket, command);
    free(command);

    command = NULL;
    if(asprintf(&command, "map add %s %d %d %s", client->data->character_name,
                coords.x, coords.y, client->encoded_player_data) < 0)
    {
        return;
    }

    struct client_list *cl = clients;
    while(cl)
    {
        struct client_t *to = cl->client;
        if(to->data->map_id == client->data->map_id &&
                to->socket != client->socket)
        {
            /* Tell other clients about the new player */
            esend(to->socket, command);

            /* Tell new player about other clients */
            char* other_player = NULL;
            if(!asprintf(&other_player, "map add %s %d %d %s",
                        to->data->character_name, to->data->coords.x,
                        to->data->coords.y, to->encoded_player_data))
            {
                continue;
            }
            esend(client->socket, other_player);
            free(other_player);
        }
        cl = cl->next;
    }

    free(command);
}

void rm_player_from_map(struct client_t *client)
{
    char* command = NULL;
    if(!asprintf(&command, "map rm %s", client->data->character_name))
    {
        return;
    }

    tell_all_players_on_map(client, client->data->map_id, command);
    free(command);
}

void send_chat_message(struct client_t *from, char* message)
{
    char* command  = NULL;
    if(!asprintf(&command, "chat %s %s", from->data->character_name, message))
    {
        return;
    }

    tell_all_players_on_map(from, from->data->map_id, command);
    free(command);
}

void tell_all_players_on_map(struct client_t *from, int map_id, char* command)
{
    struct client_list *cl = clients;
    while(cl)
    {
        struct client_t *to = cl->client;
        if(to->data->map_id == map_id &&
                to->socket != from->socket)
        {
            esend(to->socket, command);
        }
        cl = cl->next;
    }
}

void parse_command(struct client_t *client, char* command)
{
    strtok(command, " ");

    if(strncmp(command, "auth", 4) == 0)
    {
        char* name = strtok(NULL, " ");
        char* pass = strtok(NULL, " ");
        char* char_name = strtok(NULL, " ");
        if(db_authenticate(name, pass, char_name))
        {
            client->authenticated = 1;
            send_player_data_to_self(client, char_name);
            add_player_to_map(client, client->data->map, client->data->coords);
        }
    }

    /* Ignore all commands from client until they authenticate */
    if(!client->authenticated)
    {
        return;
    }

    if(strncmp(command, "move", 4) == 0)
    {
        struct coordinates_t coords;
        coords.x = atoi(strtok(NULL, " "));
        coords.y = atoi(strtok(NULL, " "));
        move_client(client, coords);
    }
    else if(strncmp(command, "chat", 4) == 0)
    {
        int bytes = 0;
        char* message = malloc(150);
        if(!message)
        {
            /* chat messages aren't important enough
             * to bring the server down */
            return;
        }

        char* m;
        while((m = strtok(NULL, "")) != NULL)
        {
            bytes += strlen(m) + 1;
            strcat(message, m);
            strcat(message, " ");
        }

        send_chat_message(client, message);
        free(message);
    }
    else if(strncmp(command, "map", 3) == 0)
    {
        char* c = strtok(NULL, " ");
        if(strcmp(c, "goto") == 0)
        {
            enum Relation rel = str_to_rel(strtok(NULL, " "));
            char* map_name = strtok(NULL, " ");
            struct map_t *map = get_map_for_name(map_name);
            map = get_adjacent_map(map, rel);
            struct coordinates_t coords = get_map_entrance(map, rel);
            add_player_to_map(client, map_name, coords);
        }
        else if(strcmp(c, "pickup") == 0)
        {
            /* TODO: properly implement this */
            char* command = "item null";
            esend(client->socket, command);
        }
        else if(strcmp(c, "info") == 0)
        {
            /* Tell the player about the map they're on. */
            int map_id = client->data->map_id;
            struct map_t *map = get_map_for_id(map_id);
            add_player_to_map(client, map->name, client->data->coords);
        }
    }
    else if(strncmp(command, "disconnect", 10) == 0)
    {
        client->connected = 0;
    }
}

int esend(int socket, char* message)
{
    int status = 0;
    int sent_bytes = 0;
    int message_len = strlen(message);
    int to_send = message_len;

    char size_msg[16];
    sprintf(size_msg, "size %d ", message_len);
    int size_msg_len = strlen(size_msg);
    send(socket, size_msg, size_msg_len, 0);

    while(sent_bytes < message_len && status >= 0)
    {
        char *m = message + sent_bytes;
        int m_len = strlen(m);
        if(m_len >= 1024)
        {
            to_send = 1024;
        }
        else
        {
            to_send = m_len;
        }

        status = send(socket, m, to_send, 0);
        if(status > 0)
            sent_bytes += status;
    }

    return status;
}

void add_client(struct client_list **clients, struct client_t *client)
{
    struct client_list* new = malloc(sizeof(struct client_list));
    new->client = client;
    new->next = *clients;
    *clients = new;
}

void delete_client_list(struct client_list **clients)
{
    struct client_list *curr = *clients;
    struct client_list *next = NULL;
    while(curr)
    {
        next = curr->next;
        free(curr);
        curr = next;
    }
}

void remove_client_from_list(struct client_list **clients, struct client_t *client)
{
    struct client_list *curr = *clients;
    struct client_list *prev = NULL;
    while(curr)
    {
        /* We're only going to determine equality by socket */
        if(curr->client->socket == client->socket)
        {
            if(prev == NULL)
            {
                /* We're at the head of the list */
                *clients = curr->next;
            }
            else
            {
                prev->next = curr->next;
            }

            free(curr);
            return;
        }
        prev = curr;
        curr = curr->next;
    }
}

int client_list_size(struct client_list *clients)
{
    struct client_list *curr = clients;
    int size = 0;

    while(curr)
    {
        size++;
        curr = curr->next;
    }

    return size;
}

