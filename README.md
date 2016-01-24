# ip_availability
Project for first term Internet Programming class.

##### A multi-threaded server-client Java application.
Users can:
- log in
- log out
- see who's online
- see who's offline
- get information about other users (whether they're available, how many times they've logged in, when they were offline, etc.).
- stop the server

### Available commands:
- login:{username}
- listavailable
- listabsent
- info:{username}
- logout
- shutdown

### Command statuses:
- ok
- error:unknowncommand
- error:notlogged

### How to run:

1. Clone repo

2. Import project in Eclipse

3. Run Main.java from main package from Eclipse - this starts a server running on port 31112

4. Run as many clients as you want by running ```telnet localhost 31112``` in however many terminals you want.
