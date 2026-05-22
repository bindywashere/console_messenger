# Main features
- Chatting in a one group with your friends on localhost
- Chat messages history
# Usage
It uses PostgreSQL, so you gonna need Docker installed on your system *(I use Arch Linux, so if you do too you can install it by entering the following command in the terminal: `sudo pacman -S docker`)*. And for now there's a guide only for Linux *(UNIX-like also, ig)*, since I didn't test it on any other system

## Server:
1. Download `docker-compose.yml` from this repository and put it the directory with server jar
2. Create `.env` file and fill it in according to the `.env.serverexample` 


3. Go to server jar directory in terminal and enter following command:
``` bash
docker compose up -d
```
4. Start server by following command in terminal:
``` 
java -jar server-jar-with-dependencies.jar
```
## Client:
1. Create `.env` file and fill it in according to the `.env.clientexample`
2. Enter following command in a terminal with a directory where your jar-file is stored
```
java -jar client-jar-with-dependencies.jar
```

