/*
 * File: main.c
 * Author: Casey Jones
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netinet/in.h>
#include <pthread.h>
#include "types.h"

#define MAXPENDING 20 
#define BUFFSIZE 32

struct player_t
{
    int sock;
    int authenticated;
    struct player_data data;
    char* encoded_player_data;
    struct coordinates_t coords;
};

struct player_list
{
    struct player_list *next;
    struct player_t *player;
};
struct player_list *players;

void* client_thread(void* vsock)
{
    int* sock = (int*) vsock;
    char buffer[BUFFSIZE];
    int received = -1;

    while(received > 0)
    {
        if((received = recv(*sock, buffer, BUFFSIZE, 0)) < 0)
        {
            printf("Failed to received data from client\n");
        }
        if(send(*sock, buffer, received, 0) != received)
        {
            printf("Failed to send data to client\n");
        }
    }

    close(*sock);

    return 0;
}

void handle_client(int* sock)
{
    pthread_t thread;
    int t;
    t = pthread_create(&thread, NULL, client_thread, (void*) sock);
    pthread_join(thread, NULL);
}

int main(int argc, char** argv)
{
    int serversock;
    int clientsock;

    struct sockaddr_in server;
    struct sockaddr_in client;

    if((serversock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
    {
        printf("Failed to create socket\n");
    }

    memset(&server, 0, sizeof(server));         /* Clear struct */
    server.sin_family = AF_INET;                /* Internet IP */
    server.sin_addr.s_addr = htonl(INADDR_ANY); /* Incoming addr */
    server.sin_port = htons(4000);              /* Server port */

    if(bind(serversock, (struct sockaddr*) &server, sizeof(server)) < 0)
    {
        printf("Failed to bind the server socket\n");
    }

    if(listen(serversock, MAXPENDING) < 0)
    {
        printf("Failed to listen on server socket\n");
    }

    printf("JBlux server 1.0\n");

    while(1)
    {
        unsigned int clientlen = sizeof(client);
        /* Wait for client connection */
        if((clientsock = accept(serversock, (struct sockaddr*) &client,
                    &clientlen)) < 0)
        {
            printf("Failed to accept client connection\n");
        }

        printf("Client connect: %s\n", inet_ntoa(client.sin_addr));
        handle_client(&clientsock);
    }

    return 0;
}

