# Main features
- Chatting in a one group with your friends on localhost
- Chat messages history
# Usage
It uses PostgreSQL, so you gonna need Docker installed on your system *(I use Arch Linux, so if you do too you can install it by entering the following command in the terminal: `sudo pacman -S docker`)*. And for now there's a guide only for Linux *(UNIX-like also, ig)*, since I didn't test it on any other system
## Server:
1. Enter following command in the terminal:
``` bash
docker run -d \
  --name postgres \
  -e POSTGRES_USER=myuser \
  -e POSTGRES_PASSWORD=mypassword \
  -e POSTGRES_DB=mydb \
  -p 5432:5432 \
  postgres:16
```
You should save somewhere name of your DB (`POSTGRES_DB`), username (`POSTGRES_USER`) and password (`POSTGRES_PASSWORD`) with port (`5432:5432` - default PostgreSQL port), since you'll need this later

2. Create `.env` file and fill it in according to the `.env.clientexample` and your data from previous command
3. Start server by following command in terminal:
``` 
java -jar server-jar-with-dependencies.jar
```
## Client:
1. Create `.env` file and fill it in according to the `.env.clientexample`
2. Enter following command in a terminal with a directory where your jar-file is stored
```
java -jar client-jar-with-dependencies.jar
```

