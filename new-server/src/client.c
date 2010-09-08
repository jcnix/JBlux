/*
 * File: client.c
 * Author: Casey Jones
 */

#include "client.h"

static struct client_t *clients[MAX_CLIENTS]; 
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
    client->connected = 1;
    client->socket = *sock;

    clients[num_clients] = client;
    num_clients++;

    while(client->connected)
    {
        if((received = recv(*sock, buffer, BUFFSIZE, 0)) < 0)
        {
            client->connected = 0;
            break;
        }

        parse_command(client, buffer);
    }

    close(*sock);
    pthread_exit(NULL);
    return 0;
}

void send_player_data_to_self(struct client_t *client, char* char_name)
{
    struct player_data *data = db_get_player(char_name);
    char* data_json = player_data_to_json(data);
    char* data_enc = base64_encode(data_json, strlen(data_json));
    client->encoded_player_data = data_enc;

    char* c1 = "player self";
    char* command = malloc(strlen(c1) + strlen(data_enc) + 2);
    sprintf(command, "%s %s", c1, data_enc);
    char* c = base64_encode(command, strlen(command));
    send(client->socket, c, strlen(c), 0);

    free(command);
    free(c);
}

void add_player_to_map(struct client_t *client, char* map,
        struct coordinates_t coords)
{
    int map_id = get_map_id_for_name(map);
    db_set_map_for_player(client->data->character_id, map_id, coords);
    client->data->map = map;
    client->data->coords = coords;

    /* TODO: get NPCs and Items and send to player */
    
    char* command;
    if(asprintf(&command, "map add %s %s %d %d", map,
                client->data->character_name, coords.x, coords.y) < 0)
    {
        return;
    }
    char* command_enc = base64_encode(command, strlen(command));

    int i;
    for(i = 0; i < num_clients; i++)
    {
        struct client_t *to_client = clients[i];
        if(to_client->data->map_id == client->data->map_id)
        {
            /* Tell other clients about the new player */
            send(to_client->socket, command_enc, strlen(command), 0);
            /* TODO: Tell new player about other clients */
        }
    }

    free(command);
}

void parse_command(struct client_t *client, char* command)
{
    char* c = base64_decode(command, strlen(command));

    char* commands = strtok(c, " ");
    if(strncmp(commands, "auth", 4) == 0)
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

    if(strncmp(commands, "move", 4) == 0)
    {
    }
    else if(strncmp(commands, "chat", 4) == 0)
    {
    }
    else if(strncmp(commands, "map", 3) == 0)
    {
    }
    else if(strncmp(commands, "disconnect", 10) == 0)
    {
        client->connected = 0;
    }

    //free(command);
    free(c);
}

