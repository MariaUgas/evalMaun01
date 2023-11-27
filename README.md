## Evaluacion tecnica Maria Ugas

El proyecto cuenta con dos endpoint 
 - El solicitado para registrar usuarios que es un metodo POST
   
   URL: http://localhost:8080/user
   
   JSON de prueba:
   
        {
        	"name":"Maria",
        	"email": "maun42@gmail.com",
        	"password":"maun123",
        	"phones": [
        			{
        				"number":"123456",
        		    "citycode":"1",
        				"countrycode":"57"
        			},
        		 {
        				"number":"1234567",
        		    "citycode":"11",
        				"countrycode":"57"
        			}
        		
        	]
        }
Cumple con la minimo solicitado usando JWT
Por temas de Tiempo no he realizado el Swagger y los Test Unitarios. Eventualmente según mi tipo los hare y los subire a la rama.

- Tambien hay un endpoit que consulta por ID de usuario.

