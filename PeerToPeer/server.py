import socket
import threading
from collections import deque
import os

HEADER = 64
PORT = 7734
#SERVER = socket.gethostbyname(socket.gethostname())
SERVER = 'localhost'
ADDR = (SERVER, PORT)
FORMAT = 'utf-8'
DISCONNECT_MESSAGE = "quit"
END_ADD_MESSAGE = "End ADD"
VER = 'P2P-CI/1.0'

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)

active_peers = deque()
# [hostname, port number]
PEER_HOST_IDX = 0
PEER_PORTNUM_IDX = 1

rfc_indexes = deque()
# [RFC number, RFC title, hostname]
RFC_NUM_IDX = 0
RFC_TITLE_IDX = 1
RFC_HOST_IDX = 2

OK = '200 OK'
BAD_REQ = '400 Bad Request'
NOT_FOUND = '404 Not Found'
UNSUPPORTED = '505 P2P-CI Version Not Supported'

def find_port(hostname):
    for x in range(len(active_peers)):
        if(hostname == active_peers[x][PEER_HOST_IDX]):
            return active_peers[x][PEER_PORTNUM_IDX]
    return -1

def rfc_exists(num, title, host):
    for x in range(len(rfc_indexes)):
        if(num == rfc_indexes[x][RFC_NUM_IDX] and title == rfc_indexes[x][RFC_TITLE_IDX] and host == rfc_indexes[x][RFC_HOST_IDX]):
            return True
    return False

def handle_client(conn, addr):
    print(f"[NEW CONNECTION] {addr} connected.")
    print('\n')

    msg_length = conn.recv(HEADER).decode(FORMAT)
    msg_length = int(msg_length)
    msg = conn.recv(msg_length).decode(FORMAT)
    # conn.send(" ".encode(FORMAT))
    rfc_hostname = msg
    # print(rfc_hostname + '\n')

    msg_length = conn.recv(HEADER).decode(FORMAT)
    msg_length = int(msg_length)
    msg = conn.recv(msg_length).decode(FORMAT)
    # conn.send(" ".encode(FORMAT))
    upload_port = int(msg)
    # print(str(upload_port) + '\n')

    active_peers.appendleft([rfc_hostname, upload_port])
    
    print('[ACTIVE PEERS] ' + str(len(active_peers)))
    for x in range(len(active_peers)):
        print(active_peers[x])
    
    connected = True
    adding = True

    while adding:
        msg_length = conn.recv(HEADER).decode(FORMAT)
        msg_length = int(msg_length)
        req = conn.recv(msg_length).decode(FORMAT)
        if(req == END_ADD_MESSAGE):
            adding = False
            # conn.send(" ".encode(FORMAT))
        else:
            # print(msg)
            # RFC Number
            RFC_NUM_START = 8
            rfc_num_end = req.index(' P2P-CI/1.0')
            rfc_num = int(req[RFC_NUM_START : rfc_num_end])
            #print(str(rfc_num))
            # RFC Title
            RFC_TITLE_START = req.index('Title: ') + 7
            RFC_TITLE_END = len(req) - 1
            rfc_title = req[RFC_TITLE_START : RFC_TITLE_END]
            #print(rfc_title)
            # RFC Hostname

            status = OK
            if(rfc_exists(rfc_num, rfc_title, rfc_hostname)):
                status = BAD_REQ
            else:
                rfc_indexes.appendleft([rfc_num, rfc_title, rfc_hostname])
            response = VER + ' ' + status + '\nRFC ' + str(rfc_num) + ' ' + rfc_title + ' ' + rfc_hostname + ' ' + str(upload_port)
            conn.send(response.encode(FORMAT))

    print('[RFC INDEXES] ' + str(len(rfc_indexes)))
    for x in range(len(rfc_indexes)):
        print(rfc_indexes[x])


    while connected:
        msg_length = conn.recv(HEADER).decode(FORMAT)
        if msg_length:
            msg_length = int(msg_length)
            method = conn.recv(msg_length).decode(FORMAT)
            # print(f"[{addr}] {method}")
            
            if(method == 'ADD'):
                # RFC Number
                msg_length = conn.recv(HEADER).decode(FORMAT)
                msg_length = int(msg_length)
                req = conn.recv(msg_length).decode(FORMAT)
                RFC_NUM_START = 8
                rfc_num_end = req.find(' ' + VER)
                if (rfc_num_end == -1):
                    status = UNSUPPORTED
                    response = VER + ' ' + status
                else:
                    rfc_num = int(req[RFC_NUM_START : rfc_num_end])
                    #print(str(rfc_num))
                    # RFC Title
                    RFC_TITLE_START = req.index('Title: ') + 7
                    RFC_TITLE_END = len(req) - 1
                    rfc_title = req[RFC_TITLE_START : RFC_TITLE_END]
                    #print(rfc_title)
                    # RFC Hostname

                    status = OK
                    if(rfc_exists(rfc_num, rfc_title, rfc_hostname)):
                        status = BAD_REQ
                    else:
                        rfc_indexes.appendleft([rfc_num, rfc_title, rfc_hostname])
                    response = VER + ' ' + status + '\nRFC ' + str(rfc_num) + ' ' + rfc_title + ' ' + rfc_hostname + ' ' + str(upload_port)
                conn.send(response.encode(FORMAT))
            elif(method == 'LOOKUP'):
                msg_length = conn.recv(HEADER).decode(FORMAT)
                msg_length = int(msg_length)
                req = conn.recv(msg_length).decode(FORMAT)
                RFC_NUM_START = 11
                rfc_num_end = req.find(' ' + VER)
                if rfc_num_end == -1:
                    status = UNSUPPORTED
                    response = VER + ' ' + status
                else:
                    rfc_num = int(req[RFC_NUM_START : rfc_num_end])
                    RFC_TITLE_START = req.index('Title: ') + 7
                    RFC_TITLE_END = len(req) - 1
                    rfc_title = req[RFC_TITLE_START : RFC_TITLE_END]
                    list = ''
                    found = False
                    for x in range(len(rfc_indexes)):
                        if(rfc_title == rfc_indexes[x][RFC_TITLE_IDX] and rfc_num == rfc_indexes[x][RFC_NUM_IDX]):
                            found = True
                            list += 'RFC ' + str(rfc_indexes[x][RFC_NUM_IDX]) + ' ' + rfc_indexes[x][RFC_TITLE_IDX] + ' ' + rfc_indexes[x][RFC_HOST_IDX] + ' ' + str(find_port(rfc_indexes[x][RFC_HOST_IDX])) + '\n'
                    status = ''
                    if found:
                        status = OK
                    else:
                        status = NOT_FOUND
                    response = VER + ' ' + status + '\n' + list
                conn.send(response.encode(FORMAT))

            elif(method == 'LIST'):
                msg_length = conn.recv(HEADER).decode(FORMAT)
                msg_length = int(msg_length)
                req = conn.recv(msg_length).decode(FORMAT)
                if (req.find(VER) == -1):
                    status = UNSUPPORTED
                    response = VER + ' ' + status
                else:
                    list = ''
                    for x in range(len(rfc_indexes)):
                        list += 'RFC ' + str(rfc_indexes[x][RFC_NUM_IDX]) + ' ' + rfc_indexes[x][RFC_TITLE_IDX] + ' ' + rfc_indexes[x][RFC_HOST_IDX] + ' ' + str(find_port(rfc_indexes[x][RFC_HOST_IDX])) + '\n'
                        
                    status = OK
                    response = VER + ' ' + OK + '\n' + list
                conn.send(response.encode(FORMAT))

            elif(method == DISCONNECT_MESSAGE):
                connected = False
                conn.send("Disconnect message received".encode(FORMAT))

    print(f"{addr} disconnected.")

    # Remove peer from list
    peer_index = -1
    for x in range(len(active_peers)):
        if(rfc_hostname == active_peers[x][PEER_HOST_IDX]):
            peer_index = x

    del active_peers[peer_index]

    # Delete RFC Indexes related to hostname
    has_rfc = True
    while has_rfc:
        rfc_idx = -1
        for x in range(len(rfc_indexes)):
            if(rfc_hostname == rfc_indexes[x][RFC_HOST_IDX]):
                rfc_idx = x
        if(rfc_idx == -1):
            has_rfc = False
        else:
            del rfc_indexes[rfc_idx]

    conn.close()

def start():
    server.listen()
    print(f"[LISTENING] Server is listening on {SERVER}")
    while True:
        conn, addr = server.accept()
        thread = threading.Thread(target=handle_client, args=(conn, addr))
        thread.start()
        print(f"[ACTIVE CONNECTIONS] {threading.active_count() - 1}")

print("[STARTING] server is starting...")
start()