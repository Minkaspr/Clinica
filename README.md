# Gestión de Clínica 🏥💼

¡Bienvenido/a al sistema de Gestión de Clínica! Este proyecto es una simulación de una base de datos para la gestión de una clínica, implementada en Java con enfoque en POO, polimorfismo y listas.

## Objetivo 🎯

El objetivo de este proyecto es permitir la gestión de una clínica. Los usuarios podrán iniciar sesión, cerrar sesión, cambiar su contraseña, y según su rol, realizar diversas operaciones. Además, todas las acciones quedan registradas en el historial.

## Características ✨

- Inicio de Sesión: Permite a los usuarios iniciar sesión en el sistema.
- Cierre de Sesión: Permite a los usuarios cerrar sesión en el sistema.
- Cambio de Contraseña: Permite a los usuarios cambiar su contraseña. La contraseña es encriptada y hasheada usando AES 128 y Base64.
- Gestión de Usuarios: Según el rol del usuario, se muestran las operaciones que pueden realizar.
- Historial: Todas las acciones quedan registradas en el historial. Si el usuario es administrador, también se registra cuando este crea, actualiza o elimina un usuario.

## Requisitos 🛠️

- Java Development Kit (JDK) 8 o superior.
- NetBeans IDE (opcional, pero recomendado para facilidad de desarrollo).
- MySQL

## Instrucciones de Uso 📖

1. Clona o descarga el repositorio del proyecto desde GitHub.
2. Abre el proyecto en NetBeans o tu IDE preferido.
3. Sigue las instrucciones en pantalla para interactuar con el sistema y gestionar la clínica.

> [!TIP]
> ```bash
> git clone https://github.com/Minkaspr/Clinica.git
> ```

##Estructura del Proyecto 🏗️
El proyecto está organizado en los siguientes paquetes y clases:

clinica: Paquete principal del proyecto.
dao: Contiene las interfaces y clases para el acceso a la base de datos.
dto: Contiene clases para el transporte de datos específicos de las entidades como MedicoDTO y UsuarioDTO. 
entity: Contiene las clases que representan las entidades de la base de datos.
util: Contiene las clases de utilidad, como la conexión a la base de datos y seguridad.
web: Contiene el servlet y el validador.

##Base de Datos 🗄️
La base de datos consta de las siguientes tablas:

Usuario: Tabla principal con campos como id_usuario, correo, clave, rol, estado.
Medico: Tabla con campos id_medico, nombres, apellidos, numero_colegiado, especialidad, hora_entrada, hora_salida, usuario_id.
Secretario: Tabla con campos id_secretario, nombres, apellidos, salario, hora_entrada, hora_salida, usuario_id.
Historial: Tabla con campos id_historial, usuario_id, accion, fecha_hora, observaciones.

##Tecnologías Utilizadas 🚀
Java
NetBeans IDE
MySQL
Apache Tomcat
Servlets
HTML
