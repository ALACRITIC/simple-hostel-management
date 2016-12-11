# Web

## Mapping

Pour mapper une requête utiliser @RequestMapping:

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public void methodName(){
        ...
    }
    
Pour définir la racine d'une classe:
    
    @Controller
    @RequestMapping("root/")
    class Controller{
        ...
    }
    
## Retour

Pour retourner directement un résultat sous forme de chaine de caractère par exemple utiliser @ResponseBody:

    @ResponseBody
    public String employee() {
        Employee employee = employsvc.affectTask("Pierre", "Go make french fries !");
        return "Task affected: <br/>" + employee.toString();
    }
    
## Récupérer des paramètres

Utiliser @RequestParam

    @RequestMapping("notes/display")
    public String method(@RequestParam(value = "name", required = false, defaultValue = "default") String name, Model model) {
        ...
    }

Utilisation du chemin d'accès:

    @RequestMapping("notes/display/{noteId}")
    public String displayNoteHtml(@PathVariable String noteId, Model model) {
        ...
    }
    
## Arborescence 

Arborescence pour le web:

    /src/resources/static       ->  sert les ressources statiques
    /src/resources/templates    ->  sert les ressources templates (Thymeleaf ici)
    
## Production de JSON

Les objets peuvent être convertis directement:
    - Dans le format voulu si il est spécifié,
    - En fonction de l'en-tête 'Accept' envoyé par le client (Json, XML, ...)

    @RequestMapping(value = "get/json/{noteId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Note getJson(@PathVariable String noteId) {
        return service.getNote(noteId);
    }
    
## Utilisation de servlets et filters

Il est possible d'accéder aux objets 'request' et 'response' à partir d'une méthode mappé:

    public Note getExplicitlyConvertedJson(@PathVariable String noteId, HttpServletResponse response) {
        ...
    }
    
Mais certains paramètres seront modifiés, l'utilisation de setContentType() par exemple ne produit pas de changements.

### Servlet traditionnelles

L'utilisation de servlet traditionnelles est possible:

    @WebServlet(name = "TestServlet", urlPatterns = {"/servlet"})
    public class PlainServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ...
        }
    }
    
Il faut cependant penser à ajouter l'annotation @ServletComponentScan à la configuration ou à l'application courante.


## Export de bean au format XML avec Spring et JAXB

Voir: https://docs.oracle.com/javase/tutorial/jaxb/intro/
Exemple de Bean:

    // spécifier le nom de l'élément principal
    @XmlRootElement(name = "note")
    public class Note {
        
        private String markdown;
    
        private String html;
    
        private String name;
    
        // constructeur sans arguments obligatoire
        public Note(){
    
        }
        
Ensuite:

    @RequestMapping(value = "get/xml/{noteId}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Note getXml(@PathVariable String noteId) {
        Note note = service.getNote(noteId);
        return note;
    }

D'autres solutions comme XStream peuvent être envisagées.