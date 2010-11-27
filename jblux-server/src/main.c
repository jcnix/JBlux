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
#include <signal.h>
#include "client.h"
#include "map.h"
#include "world.h"

#define MAXPENDING 20 

void signal_handler(int sig)
{
    cleanup();
    signal(sig, SIG_DFL);
    raise(sig);
}

int main(int argc, char** argv)
{
    if(argc > 1)
    {
        if(strcmp(argv[1], "-d") == 0)
        {
            if(daemon(1,1) != 0)
            {
                fprintf(stderr, "Could not start daemon\n");
            }
        }
    }
    
    signal(SIGTERM, signal_handler);
    signal(SIGINT, signal_handler);
    signal(SIGQUIT, signal_handler);

    /* Do a test connection to see if the server
     * is even up */
    PGconn* conn = db_connect();
    if(!db_is_connected(conn))
    {
        return 1;
    }
    db_disconnect(conn);

    /* Incase the server crashed with users online
     * set all users to offline so they can reconnect */
    db_set_all_users_offline();

    int serversock = 0;
    int clientsock = 0;

    struct sockaddr_in server;
    struct sockaddr_in client;

    if((serversock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
    {
        fprintf(stderr, "Failed to create socket\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));         /* Clear struct */
    server.sin_family = AF_INET;                /* Internet IP */
    server.sin_addr.s_addr = htonl(INADDR_ANY); /* Incoming addr */
    server.sin_port = htons(4000);              /* Server port */

    if(bind(serversock, (struct sockaddr*) &server, sizeof(server)) < 0)
    {
        fprintf(stderr, "Failed to bind the server socket\n");
        return 1;
    }

    if(listen(serversock, MAXPENDING) < 0)
    {
        fprintf(stderr, "Failed to listen on server socket\n");
        return 1;
    }
    printf("JBlux server 1.0\n");

    /* Start the game world */
    pthread_t world_thread;
    pthread_create(&world_thread, NULL, init_world, NULL);

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

