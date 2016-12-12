# Erreurs possibles

Lors du setup:

    java.lang.annotation.AnnotationFormatError: Invalid default:
        public abstract org.springframework.beans.factory.annotation.Autowire
        org.springframework.config.java.annotation.Configuration.defaultAutowire()

    >> La version de Spring java config n'est pas compatible

En exportant du XML:

     Resolved exception caused by Handler execution: org.springframework.web.HttpMediaTypeNotAcceptableException: Could not find acceptable representation
     
     >> L'objet à exporter est mal configuré, ajouter @XmlRootElement(name = "note")
     Voir: https://docs.oracle.com/javase/tutorial/jaxb/intro/
     
Lors de l'utilisation d'une Servlet:

    Error 405 HTTP method GET is not supported by this URL
    
    >> Implémenter la méthode doGet() et enlever l'appel de super.doGet(req, resp);