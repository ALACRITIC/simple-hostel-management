# Configurer une application

## Configuration automatique basique:

    @SpringBootApplicatcation
    
    >> Equivaut à
    
    @Configuration                  ->  classe de configuration
    @EnableAutoConfiguration        ->  configuration automatique par défaut
    @ComponentScan                  -> recherche de composants (beans) à partir de ce package
    
Le plus simple est de créer une classe application à la racine de l'application pour autoriser l'utilisation 
d'@Autowired (injection) à partir de la racine.

## Bannière

Il est possible de personnaliser la bannière de lancemement de l'application en ajoutant un fichier banner.txt dans le dossier de ressources.

## Changer le port

application.properties:

    server.port=8081
    
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