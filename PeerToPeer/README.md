# Peer To Peer System

![image](https://github.com/FirebombDragon/PortfolioProjects/assets/91640916/7a0a0d3b-35cf-42c8-a426-6a2298e6fa48)
![image](https://github.com/FirebombDragon/PortfolioProjects/assets/91640916/d97f6c2d-9110-48b1-bef5-92c8a9bc38a5)


### Features
This system consists of two types of files.  The client files hold bits of information known as "RFCs", while the server file contains references to the RFCs and what clients hold them.  A client can make a request to either GET an RFC from another client, LOOKUP a specific RFC through the server, LIST all RFCs known to the server, or ADD an RFC reference to the server.

### Files
<b>Server:</b> The server contains references to all RFCs and can send that information to clients<br>
<b>Client1:</b> The first client which contains RFCs. This file can add RFC references to the server or send them to other clients. This file can also send requests to the server or other clients<br>
<b>Client2:</b> The second client which contains a different set of RFCs. This file can add RFC references to the server or send them to other clients. This file can also send requests to the server or other clients<br>
<b>Unsupported Client:</b> The client which doesn't have proper information needed to make requests.  The server and other clients should ignore requests made by this client.<br>

### Requests
<b>GET:</b> When called, the client will connect with another client to retrieve RFC information.<br>
<b>ADD:</b> When called, the client will connect with the server to add a reference to an RFC to the server.<br>
<b>LOOKUP:</b> When called, the client will connect with the server to search for an RFC recognized by the server.<br>
<b>LIST:</b> When called, the client will connect with the server to get all RFC references recognized by the server.<br>
