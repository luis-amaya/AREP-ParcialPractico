# Whether APP

En este proyecto se pide crear un app web en donde se pueda consultar el clima de una ciudad dada, utilizando el APi de  [Open Weather] ( https://openweathermap.org/)

## Comenzando 🚀

Se presentaran un conjunto de instrucciones para que este pueda tener un funcionamiento correcto en maquina local.

Mira **Deployment** para conocer como desplegar el proyecto.


### Pre-requisitos 📋

Para un optimo funcionamiento del proyecto en un ambiente de desarrollo se debe contar con los siguientes Apps instaladas y configuradas (en el caso de Java y Maven):

- [Java](https://www.java.com/es/download/ie_manual.jsp) : Lenguaje y entorno de desarrollo
- [Maven](https://maven.apache.org/) : Gestor de dependencias
- [Git](https://git-scm.com/) : Software para control de versiones

### Instalación 🔧

Para esto se puede utilizar cualquier shell que prefiera.

1. ***Clonar el proyecto***
    ```
    git clone https://github.com/luis-amaya/AREP-ParcialPractico
    ```

2. ***Compilar el proyecto***
   ````
   mvn package
   ````
3. ***Ejecucion del Proyecto***
   ````
   java $JAVA_OPTS -cp target/classes:target/dependency/* edu.escuelaing.arep.App
   ````
4 ***Entorno Web***
    ````
    localhost:35001/clima
    localhost:35001/consulta?lugar={ciudad o lugar}
    ````
## Despliegue 📦

El despliegue se puede encontrar en [Heroku](https://areptallerconocimientos.herokuapp.com/)

## Construido con 🛠️
Proyecto construido con:

* [Maven](https://maven.apache.org/) - Manejador de dependencias



## Autores ✒️

***Luis Gerardo Amaya Ortiz***

## Licencia 📄

Este proyecto está bajo la Licencia (Tu Licencia) - mira el archivo [LICENSE.md](LICENSE.md) para detalles


Plantilla de README por: [Villanuevand](https://github.com/Villanuevand) 
