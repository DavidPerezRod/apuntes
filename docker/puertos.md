## Puertos

Un elemento muy importante de todos los que se pueden ver al hacer el inspect, es la ip en la que corre el contenedor

```JSON
"NetworkSettings": {
            "Bridge": "",
            "SandboxID": "067ed1f8c489a6cd49781cd615ef764cc41886f866878324053d3023ac9209f8",
            "HairpinMode": false,
            "LinkLocalIPv6Address": "",
            "LinkLocalIPv6PrefixLen": 0,
            "Ports": {},
            "SandboxKey": "/var/run/docker/netns/067ed1f8c489",
            "SecondaryIPAddresses": null,
            "SecondaryIPv6Addresses": null,
            "EndpointID": "92c62d50d7f0912e3c8f171a192fce91f0ab1cdbf6b05c9932dbfc99bb5d66c4",
            "Gateway": "172.17.0.1",
            "GlobalIPv6Address": "",
            "GlobalIPv6PrefixLen": 0,
            "IPAddress": "172.17.0.2",
            "IPPrefixLen": 16,
            "IPv6Gateway": "",
            "MacAddress": "02:42:ac:11:00:02",
            "Networks": {
                "bridge": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": null,
                    "NetworkID": "d112535e86d112345270102eb2bea71895216f2770532d2237c841fe96d54bcf",
                    "EndpointID": "92c62d50d7f0912e3c8f171a192fce91f0ab1cdbf6b05c9932dbfc99bb5d66c4",
                    "Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "02:42:ac:11:00:02",
                    "DriverOpts": null
                }
            }
        }
```

**_172.17.0.2_** en el ejemplo. Siguiendo con el ejemplo del Dockerfile descrito en la [creación de la imagen](creacion-imagenes.md), se debería ver "Hello World".

Sin embargo este procedimiento no es muy práctico porque implicaría tener que hacer un inspect sobre cada contenedor para saber la ip en la que esta corriendo.

Por eso, con docker se trabaja **_publicando puertos_**. Este mecanismo consiste en asignar al contenedor un puerto en nuestra máquina. Este paso se debe hacer en el momento de crear el contenedor:

```sh
docker create -p 8080:80 --name web01 helloworld
```

De esta forma se le asigna al contenedor el puerto 8080 del host en el que está corriendo docker. El hostport sería el 8080 y el container port el 80, así que el puerto 80 del contenedor estaría mapeado al 8080 de todos los interfaces del host.

Este mecanismo es útil porque impide la reutilización de puertos, es decir si se intenta lanzar un nuevo contenedor al mismo puerto se produce une error.

Si por el contrario se quiere que varias instancias de un contenedor puedan utilizar el mismo puerto, entonces lo que hay que hacer es **_exponer puertos_**

Esta estrategia implica a la creación de la imagen, así que se hace en el dockerfile.

```dockerfile
FROM ubuntu:18.04 

RUN apt-get update
RUN apt-get install nginx -y
RUN echo 'hello world' > /var/www/html/index.html

#se expone el puerto 80.
EXPOSE 80

CMD nginx -g 'daemon off;'
```

Por defecto los puertos definidos en el expose, son puertos tcp, si se quisiese exponer otro tipo, habría que indicarlo a continuación del número de puerto, por ejemplo:

```dockerfile
FROM ubuntu:18.04 

RUN apt-get update
RUN apt-get install nginx -y
RUN echo 'hello world' > /var/www/html/index.html

EXPOSE 80/udp

CMD nginx -g 'daemon off;'
```

Docker de esta forma, ya sabe que la imagen tiene un puerto, y lo puede publicar al crear el contenedor, si se utiliza la opción -P. La diferencia es que el puerto que se publicará, será asignado por docker al arrancar.

El rango de puertos que va a utilizar docker en un sistema linux, será el definido en el fichero /pro/sys/net/ipv4/ip_local_port_range