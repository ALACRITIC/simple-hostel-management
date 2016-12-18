# Configurer le serveur embarqué Tomcat

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


## Bannière

Il est possible de personnaliser la bannière de lancemement de l'application en ajoutant un fichier banner.txt dans le dossier de ressources.


## Changer le port et l'adresse d'écoute

Dans le fichier application.properties:

    server.port=8081
    server.address=0.0.0.0
    
    # Utiliser un port libre aléatoire
    server.port=0
    
## Activer SSL

Dans 'application.properties' ajouter:

    # SSL
    server.port=8443
    server.ssl.key-store=classpath:keystore.jks         # chemin du fichier où sont stockées les clefs
    server.ssl.key-store-password=another-secret        # mot de passe du keystore
    server.ssl.key-password=secret                      # mot de passe de la clef
    
Pour générer un fichier de clefs, mode non intéractif:
    
    $ keytool -genkey -alias serverkey -keyalg RSA -keysize 4096 \
        -keystore keystore.jks -validity 3650 \
        -dname "CN=Jhon Doe, OU=Heyhey, O=Hoho, L=Brussels une fois, ST=Unknown, C=BE" \
        -keypass secret \
        -storepass another-secret
    
Remarques:
    1. Le mot de passe du keystore est obligatoire
    1. Il peut être plus simple d'utiliser un proxy
    
Plus à l'adresse: https://docs.oracle.com/cd/E19509-01/820-3503/ggfen/index.html

## Journaux d'accès 

Dans le fichier application.properties:

    server.tomcat.basedir=tomcat-logs
    server.tomcat.accesslog.enabled=true
    server.tomcat.accesslog.pattern=%t %a "%r" %s (%D ms)