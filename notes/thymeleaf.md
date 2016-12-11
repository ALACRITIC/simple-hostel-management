# Thymeleaf

Mémo intéréssant: https://github.com/engma/thyemeleaf-cheat-sheet

Insérer une variable:

    <p th:text="${content}"/>
    
    <!-- utet permet de ne pas échaper du conteny (HTML par exemple)-->
    <p th:utext="${content}"/>
    
    <!-- Avec un getter -->
    <p th:utext="${currentNote.getHtml()}"/>
    
## Changer un attribut

Exemple:

    <td><a href="notes/display/note_name" th:attr="href='notes/display/' + ${name}" th:text="${name}">Name</a></td>
    
## Itération

Itération simple de chaines de caractères (Arraylist par exemple):

    <table>
        <tr th:each="name : ${notesList}">
            <td><a href="notes?name=note_name" th:attr="href='note?name=' + ${name}" th:text="${name}">Name</a></td>
        </tr>
    </table>