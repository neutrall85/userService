package ru.aston.homework.intensive_modul2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);

        // Добавляем кастомный баннер
        application.setBanner((environment, sourceClass, out) -> {
            out.println("╔════════════════════════════════════════════════════════════════╗");
            out.println("║                                                                ║");
            out.println("║    🚀 User Management System API                              ║");
            out.println("║                                                                ║");
            out.println("║    📧 Version: 1.0.0                                          ║");
            out.println("║    🔧 Spring Boot 3.5.7                                       ║");
            out.println("║    🗄️  PostgreSQL + JPA                                      ║");
            out.println("║                                                                ║");
            out.println("╚════════════════════════════════════════════════════════════════╝");
            out.println();
        });

        application.run(args);
    }
}
