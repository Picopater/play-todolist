#Practica 1 - Play TodoList

API REST en proceso de desarrollo para una aplicación de tareas hecha en [Scala], usando como framework de desarrollo [Play Framework]. Esta pensada solo la parte de servidor, que devuelve listados de objetos [JSon] para posteriormente implementar la vista y hacer un uso rapido y sencillo de ellos.


Creación
--------
- Creación de una tarea para un usuario anónimo ("guest")
- Creación de una tarea para un usuario registrado


Borrado
-------
- Borrado de una tarea


Edición
-------
- No implementada aun


Listado
-------
- Listado de tareas de usuarios anónimos
- Listado de tareas de un usuario registrado
- Ver una tarea concreta 


Arquitectura Usada
==================

Controlador
-----------
Una vez se ha enrutado la petición y recogidos los parámetros HTTP, se encarga de procesarlos y de pasarle los datos necesarios al Modelo para asegurar la persistencia en la base de datos. 


Modelo
------
Sera el encargado de las altas, bajas, lecuturas y modificaciones de las tareas en la base de datos.


Vista
-----
No estamos trabajando con vistas, dejamos que cada cliente desarrolle la suya si quiere. Cada respuesta devolverá un código HTTP y una lista json si se da el caso. 


Probando el API
===============


```sh
git clone https://dmcc88@bitbucket.org/dmcc88/play-todolist.git
cd play-todolist
activator clean
activator run
```

>Una vez puesto en marcha el servicio podremos probarlo con ayuda de la siguiente tabla


| Recurso                              | Parámetros         | Descripción                      | Respuesta        |
| ------------------------------------ | ------------------ | ------------------------------- | ---------------- |
| **GET** localhost:9000/              | Ninguno            |Index                            | 200              |
| **GET** localhost:9000/tasks         | Ninguno            |Tareas de usuarios no registrados| 200              |
| **GET** localhost:9000/tasks/{id}    | {id}               |Muestra la tarea con esa id      | 200 y Json,404   |
| **POST** localhost:9000/tasks/       | {label}            |Crea una tarea anónima           | 200 y Json,400   |
| **DELETE** localhost:9000/tasks/{id} | {id}               |Borra la tarea con esa id        | 200,404          |
| **GET** localhost:9000/{login}/tasks | {login}            |Lista las tareas de un usuario   | 200 y Json,400   |
| **POST** localhost:9000/{login}/tasks| {login}            |Crea la tarea para un usuario    | 200 y Json,400   |
----

Actualmente el servicio también esta desplegado en [Heroku] en el siguiente [enlace] para poder hacer pruebas.


Datos Personales
================
Desarrollador: Daniel M. Campos Carranza
Profesor: Domingo Gallardo Lopez


[Scala]:http://www.scala-lang.org/api/current/#package
[Play Framework]:https://www.playframework.com/documentation/2.3.x/ScalaHome
[JSon]:http://json.org/
[Heroku]:https://www.heroku.com/
[enlace]:http://secure-chamber-5214.herokuapp.com/