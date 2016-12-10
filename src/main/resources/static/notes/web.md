# Web

## Mapping

Pour mapper une requête utiliser @RequestMapping:

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public void methodName(){
    
    }
    
## Retour

Pour retourner directement un résultat sous forme de chaine de caractère par exemple utiliser @ResponseBody:

    @ResponseBody
    public String employee() {
        Employee employee = employsvc.affectTask("Pierre", "Go make french fries !");
        return "Task affected: <br/>" + employee.toString();
    }
    
## Arborescence 

Arborescence pour le web:

    /src/resources/static       ->  sert les ressources statiques
    /src/resources/templates    ->  sert les ressources templates (Thymeleaf ici)
    