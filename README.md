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
You should write somewhere name of your DB (`POSTGRES_DB`), username (`POSTGRES_USER`) and password (`POSTGRES_PASSWORD`) with port (`5432:5432` - default PostgreSQL port), since you'll need this later

2. Start server by following command in terminal:
``` 
java -jar server-jar-with-dependencies.jar
```
If you don't have `.env` file it'll be created in the folder with jar-file.
3. Change `.env` file according the parameters, you entered in the command in the 1st point. Don't forget about the `APP_PORT` field there you should choose on wich port will gonna work server *(NOT DATABASE PORT)*
4. Start the server again
## Client:
1. Enter following command in a terminal with a directory where your jar-file is stored
```
java -jar client-jar-with-dependencies.jar
```
It will create an `.env` file in the directory with your jar
2. Edit `.env` file according the `APP_PORT` field from the server's `.env`
3. Start the client again
