# Configurer une application Spring

## Configuration automatique basique:

    @SpringBootApplicatcation
    
    >> Equivaut à
    
    @Configuration                  ->  classe de configuration
    @EnableAutoConfiguration        ->  configuration automatique par défaut
    @ComponentScan                  -> recherche de composants (beans) à partir de ce package
    
Le plus simple est de créer une classe application à la racine de l'application pour autoriser l'utilisation 
d'@Autowired (injection) à partir de la racine.

