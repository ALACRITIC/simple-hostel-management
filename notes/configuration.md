# Configurer une application

## Configuration automatique basique:

    @SpringBootApplicatcation
    
    Equivaut à
    
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