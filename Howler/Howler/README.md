# Homework 5: Authentication with JWT

## Placeholder Image Source: https://depositphotos.com/vectors/avatar-placeholder.html

## Interesting Challenge

One issue that I had when creating my JWT was figuring out how to implement the token middleware. The way I solved this was by splitting the token into header, payload, and signature and then, using the header and payload, confirming the signature.  Once the signature was confirmed, I only had to make sure the token was not expired before letting the user continue.

## Security Risks

One security risk that I found while creating my JWT authentication was when the session would continue if I restarted the server while logged in.  This can be exploited by a user accessing the session and getting information from the session.  A possible fix could be to remove all sessions every time the server is taken down, but the tradeoff to this is that many people would be signed out abruptly if the server is either taken down by a malicious user, if a problem with the server causes it to shut itself down, or if a problem requires a server reboot to fix it.
