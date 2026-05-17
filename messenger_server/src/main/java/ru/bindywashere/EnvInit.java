package ru.bindywashere;

import java.io.*;
import java.io.FileWriter;

public class EnvInit {
    public static void init() {
        try {
            File Env = new File(".env");

            if (Env.createNewFile()) {
                try (FileWriter Writer = new FileWriter(".env")) {
                    Writer.write("""
                            DB_URL=jdbc:postgresql://localhost:5432/dbname
                            DB_USERNAME=yourusername
                            DB_PASSWORD=yourpassword
                            APP_PORT=1234
                            """);
                    Writer.close();
                    System.out.println("[ENV] >> .env file wasn't found, an example was created");
                    System.out.println("[ENV] >> edit .env and restart an app");
                } catch (IOException e) {
                    System.out.println("an error occurred while creating .env file");
                    e.printStackTrace();
                }

                System.out.println(".env file created");
            } else {
                System.out.println(".env file was found!");
            }
        } catch (IOException e) {
            System.out.println("an error has occurred while creating .env file");
            e.printStackTrace();
        }
    }

}
