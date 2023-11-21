import socket
import threading
import os
from datetime import datetime, timezone

HEADER = 64
PORT = 7734
FORMAT = 'utf-8'
DISCONNECT_MESSAGE = "quit"
END_ADD_MESSAGE = "End ADD"
#SERVER = socket.gethostbyname(socket.gethostname())
SERVER = 'localhost'
ADDR = (SERVER, PORT)
this_os = os.name
# print(this_os)
# print("\n")
OK = '200 OK'
BAD_REQ = '400 Bad Request'
NOT_FOUND = '404 Not Found'
UNSUPPORTED = '505 P2P-CI Version Not Supported'


rfc_numbers = [693]
rfc_titles = ['Server/Client Computing']
rfc_hostname = 'anotherhost.csc.ncsu.edu'
rfc_last_modified = ['Thu, 21 Jan 2001 9:23:46 GMT']
rfc_data = ['Server/Client Computing']
VER = 'P2P-CI/1.0'
upload_port = 3950

# Peer-to-Peer System
def local_server_thread():
    UPLOAD_ADDR = (SERVER, upload_port)
    local_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    local_server.bind(UPLOAD_ADDR)
    local_server.listen()
    print(f"Local server is listening on {SERVER}")
    while True:
        conn, addr = local_server.accept()
        rfc = ''
        # print(f'New connection with {addr} established\n')
        request = conn.recv(2048).decode(FORMAT)
        RFC_NUM_START = 8
        rfc_num_end = request.find(' ' + VER)
        if (rfc_num_end == -1):
            status = UNSUPPORTED
        else:
            hostname_start = request.find('Host: ') + 6
            hostname_end = request.find('\nOS: ')
            hostname = request[hostname_start : hostname_end]
            os_start = request.find('OS: ') + 4
            os_end = request.find('\n\n')
            os_given = request[os_start : os_end]
            if (hostname != rfc_hostname):
                status = BAD_REQ
            elif (os_given != this_os):
                status = BAD_REQ
            else:
                rfc_num = int(request[RFC_NUM_START : rfc_num_end])
                found = False
                for i in range(len(rfc_numbers)):
                    if (rfc_numbers[i] == rfc_num):
                        found = True
                        index = i
                if (found):
                    status = OK
                    date_time = datetime.now(timezone.utc)
                    format_date_time = datetime.strftime(date_time, '%a, %d %b %Y %H:%M:%S %Z')
                    rfc = '\nDate: ' + format_date_time + '\nOS: ' + this_os + '\nLast-Modified: ' + rfc_last_modified[index] + '\nContent-Length: ' + str(len(rfc_data[index])) + '\nContent-Type: text/plain\n' + rfc_data[index]
                else:
                    status = NOT_FOUND
        response = VER + ' ' + status + rfc
        response_to_send = response.encode(FORMAT)
        conn.send(response_to_send)
        # print(request)
        # conn.send('Message Received'.encode(FORMAT))
        conn.close()
        # print(f"Connection with {addr} terminated\n")

# GET request
def get_rfc(rfc_num, rfc_hostname, rfc_port, rfc_os):
    UPLOAD_ADDR = (SERVER, rfc_port)
    local_client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    local_client.connect(UPLOAD_ADDR)
    request = 'GET RFC ' + str(rfc_num) + ' ' + VER + '\nHost: ' + rfc_hostname + '\nOS: ' + rfc_os + '\n'
    message = request.encode(FORMAT)
    msg_length = len(message)
    send_length = str(msg_length).encode(FORMAT)
    send_length += b' ' * (HEADER - len(send_length))
    # local_client.send(send_length)
    local_client.send(message)
    response = local_client.recv(2048).decode(FORMAT)
    print(response)
    rfc_numbers.append(rfc_num)
    # print('number added')
    rfcs = listall()
    # print('listed')
    rfc = rfcs.find('RFC ' + str(rfc_num))
    title_len = len('RFC ' + str(rfc_num))
    title_start = rfc + title_len
    title_end = rfcs.find(rfc_hostname) + 1
    rfc_titles.append(response[title_start : title_end])
    # print('title added')
    last_mod_start = response.find('Last-Modified: ') + 15
    last_mod_end = response.find('\nContent-Length:')
    rfc_last_modified.append(response[last_mod_start : last_mod_end])
    # print(rfc_last_modified[2])
    # print('modified date added')
    data_start = response.find('Content-Type: text/plain\n') + len('Content-Type: text/plain\n')
    rfc_data.append(response[data_start : len(response)])
    # print(rfc_data[2])
    # print('fully added')
    
# Methods: ADD, LOOKUP, LIST
# Header fields: Host, Port, Title

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)
connected = True

# Starts P2P process
thread = threading.Thread(target=local_server_thread)
thread.start()

def send(msg):
    message = msg.encode(FORMAT)
    msg_length = len(message)
    send_length = str(msg_length).encode(FORMAT)
    send_length += b' ' * (HEADER - len(send_length))
    client.send(send_length)
    client.send(message)

def listall() -> str:
    # print('listall called')
    send('LIST')
    request = 'LIST ALL ' + VER + '\nHost: ' + rfc_hostname + '\nPort: ' + str(upload_port) + '\n'
    send(request)
    # print('request sent')
    response = client.recv(2048).decode(FORMAT)
    # print(response)
    return response

send(rfc_hostname)
send(str(upload_port))

for x in range(len(rfc_numbers)):
    request = 'ADD RFC ' + str(rfc_numbers[x]) + ' ' + VER + '\nHost: ' + rfc_hostname + '\nPort: ' + str(upload_port) + '\nTitle: ' + rfc_titles[x] + '\n'
    send(request)
    print(client.recv(2048).decode(FORMAT))

send(END_ADD_MESSAGE)

while connected:
    method = input('Method (ADD, LOOKUP, LIST, GET, or "' + DISCONNECT_MESSAGE + '" to exit): ')
    # print(method)
    if(method == 'ADD'):
        rfc_num = int(input('RFC Number: '))
        title = input('Title: ')
        send(method)
        request = 'ADD RFC ' + str(rfc_num) + ' ' + VER + '\nHost: ' + rfc_hostname + '\nPort: ' + str(upload_port) + '\nTitle: ' + title + '\n'
        send(request)
        print(client.recv(2048).decode(FORMAT))
    elif(method == 'LOOKUP'):
        rfc_num = int(input('RFC Number: '))
        title = input('Title: ')
        send(method)
        request = 'LOOKUP RFC ' + str(rfc_num) + ' ' + VER + '\nHost: ' + rfc_hostname + '\nPort: ' + str(upload_port) + '\nTitle: ' + title + '\n'
        send(request)
        print(client.recv(2048).decode(FORMAT))

    elif(method == 'LIST'):
        # host = input('Host: ')
        # port = int(input('Port: '))
        response = listall()
        print(response)

    # Starts GET Method
    elif(method == 'GET'):
        rfc_num = int(input('RFC Number: '))
        rfc_host = input('Host: ')
        rfc_port = int(input('Port: '))
        rfc_os = input('OS: ')
        get_rfc(rfc_num, rfc_host, rfc_port, rfc_os)

    elif(method == DISCONNECT_MESSAGE):
        connected = False
        send(method)
        print(client.recv(2048).decode(FORMAT))
    else:
        print(VER + ' ' + BAD_REQ)