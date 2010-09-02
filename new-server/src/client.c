/*
 * File: client.c
 * Author: Casey Jones
 */

#include "client.h"

void handle_client(int* sock)
{
    pthread_t thread;
    int t;
    t = pthread_create(&thread, NULL, client_thread, (void*) sock);
    pthread_join(thread, NULL);
}

void* client_thread(void* vsock)
{
    int* sock = (int*) vsock;
    char buffer[BUFFSIZE];
    int received = -1;

    struct client_t client;
    client.connected = 1;

    while(client.connected)
    {
        if((received = recv(*sock, buffer, BUFFSIZE, 0)) < 0)
        {
            client.connected = 0;
            break;
        }

        parse_command(*sock, &client, buffer);
    }

    close(*sock);
    return 0;
}

void send_player_data(int sock, char* char_name)
{
    struct player_data *data = db_get_player(char_name);
    char* data_json = player_data_to_json(data);
    char* data_enc = base64_encode(data_json, strlen(data_json));

    /* TODO: the string sent needs to be part of a larger command,
     * and base64 encoded */
    send(sock, data_enc, 0, 0);

    free(data_json);
    free(data_enc);
}

void parse_command(int sock, struct client_t *client, char* command)
{
    printf("%s\n", command);
    command = base64_decode(command, strlen(command));

    char* commands = strtok(command, " ");
    if(strncmp(commands, "auth", 4))
    {
        char* name = strtok(NULL, " ");
        char* pass = strtok(NULL, " ");
        char* char_name = strtok(NULL, " ");
        printf("%s %s %s\n", name, pass, char_name);
        if(db_authenticate(name, pass, char_name))
        {
            client->authenticated = 1;
            send_player_data(sock, char_name);
        }
    }

    /* Ignore all commands from client until they authenticate */
    if(!client->authenticated)
    {
        return;
    }

    if(strncmp(commands, "move", 4))
    {
    }
    else if(strncmp(commands, "chat", 4))
    {
    }
    else if(strncmp(commands, "map", 3))
    {
    }
    else if(strncmp(commands, "disconnect", 10))
    {
        client->connected = 0;
    }

    //if(send(sock, buffer, received, 0) != received)
    //{
    //    printf("Failed to send data to client\n");
    //}
}

