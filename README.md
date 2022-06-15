# client-server
### Client-server application using shared memory and multithreading in Java

  The work of the client-server architecture begins with the launch of the server, because the server can work without clients, but clients without a server cannot. When turned on, the client (using a generator) generates a command represented by a string of characters. The interaction between the client and the server is carried out using sockets that play the role of a connection point. The socket is created on the client side, and the server recreates it when it receives a connection signal. The socket specifies the hostname (a specific network node) or the IP address of the machine and the port. A socket server is created on the server side. You need to specify the port for it, you do not need to specify the connection address, since communication takes place on the server machine. The command received by the server from the client enters the queue located in the shared memory area. The client receives a message about the acceptance of the command by the server.

  To process commands, the handler pipeline is enabled. The essence of the pipeline is that a certain stage of it (in this case, a certain handler) performs an operation not on the entire command (a string of characters), but on its part and transfers control to the next stage (the handler). At the same time, all handlers work in parallel. Thus, an increase in system performance is achieved by performing not one operation, but several operations at once per unit of time. After registering in the global register, the handlers get access to the shared memory area and do their part on the commands, passing them to each other.

  After processing all received commands, the server immediately sends the result to the client. The connection of the server and the client is also carried out via sockets.

---

When writing the program , the following classes were created:
* Admin.class – launches methods for creating, running and stopping the server.
* Server.class – the server part of the architecture. It creates a socket server listening on port 4004, a global register, and handlers. The server accepts the client's requests (commands), then transfers them to shared memory, where handlers are already waiting, and sends a response to the client.
* Client.class – the client part of the architecture. It creates a socket with the localhost address and port 4004 (like the server) and a command generator. The client communicates with the server.
* Task.class – a command sent by the client to the server. As fields, it has an array of letters in a string, a lock sign in memory, and a counter for the number of processed characters.
* Handler.class – a handler that implements the Runnable class. It has an ID, a reference to the next handler, and a variable telling it to wait for a command from the previous handler. Has access to shared memory for raw and processed.
* GlobalRegister.class - the global register of the server. Starts the passed number of handlers to work. As fields, it has an array of registered handlers, a number, an array of tasks (from which commands are taken) and results (in which executed commands are placed) for them.
