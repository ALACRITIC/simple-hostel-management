# Thymeleaf

Mémo intéréssant: https://github.com/engma/thyemeleaf-cheat-sheet

Insérer une variable:

    <p th:text="${content}"/>
    
    <!-- Use utext to insert unescaped text-->
    <p th:utext="${content}"/>
    
    