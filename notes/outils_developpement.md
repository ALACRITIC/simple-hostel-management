# Outils de développement

Ces événements sont très pratiques pour remplacer des templates par exemple, à chaque redémarrage.

## Mise à jour

Spring Boot surveille le classpath.

*   Pour mettre à jour des classes Java, éxecuter un "make" pour rafraichir le projet
*   Pour mettre à jour les templates thymeleaf, ajouter cette option dans application.properties

```
    spring.devtools.restart.additional-paths=src/main/resources/templates
    
    Ou alors en Java:
    
    System.setProperty("spring.template.cache", "false");
```

Ecouter les évenements de l'application peut être utile pour copier les templates ou les fichiers statiques:

```
    SpringApplication app = new SpringApplication(Application.class);
    ...
    app.addListeners(updater);
    app.run(args);
```

## Logging

Le niveau de log général peut être modifié dans le fichier application.properties:

    # Main log level
    logging.level.root=INFO

# Mettre en place des mesures

Ajouter au pom.xml:

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    
Ensuite au fichier application.xml:

    management.port=8081                    # Port d'accès aux mesures
    management.address=127.0.0.1            # Adresse d'éccès
    management.context-path=/management     # Racine commune optionnelle 
    endpoints.health.enabled=false          # Permet de désactiver certaines fonctionnalités, ici /health

Adresses disponibles ensuite:

    /management/metrics
    /management/health
    /management/info
    /management/trace
    /management/configprops
    /management/beans
    /management/env
    /management/heapdump
    /management/autoconfig

Voir comme interface: http://codecentric.github.io/spring-boot-admin/1.4.4/#getting-started
Plus sur: http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html

## Utilitaires

    StringUtils
    