
# Développement

## Serveur simplifié

Spring boot propose un serveur Tomcat simplifié pour développement:

```
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
```

Il est possible d'écouter des événements:

```
    SpringApplication app = new SpringApplication(Application.class);
    ...
    app.addListeners(updater);
    app.run(args);
```

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

## Logging

Le niveau de log général peut être modifié dans le fichier application.properties:

    # Main log level
    logging.level.root=INFO
