# AppChat

## Descripción
AppChat es una aplicación de mensajería instantánea desarrollada en Java que permite a los usuarios comunicarse entre sí, gestionar contactos y grupos, y acceder a funcionalidades premium como la exportación de conversaciones en formato PDF.

![Logo](https://www.um.es/documents/1073494/42130150/LogosimboloUMU-positivo.png/e1f004bd-ed22-23dd-682f-ab3f1f39b435?t=1693480807647&download=true)

## Características principales

### Gestión de usuarios
- Registro de nuevos usuarios
- Inicio de sesión con número de teléfono y contraseña
- Personalización de perfil (cambio de imagen y mensaje de saludo)

### Gestión de contactos
- Añadir contactos individuales
- Crear grupos de contactos

### Mensajería
- Envío de mensajes de texto a usuarios individuales
- Envío de mensajes a grupos, como una lista de difusión
- Envío de emojis

### Búsqueda de mensajes
- Filtrado por texto contenido en el mensaje
- Filtrado por contacto
- Filtrado por teléfono
- Combinación de múltiples filtros

### Funcionalidades Premium
- Activación de cuenta Premium mediante pago (simulado)
- Descuentos según fecha de registro o número de mensajes enviados
- Exportación de chats a PDF

## Arquitectura
La aplicación sigue el patrón de arquitectura Modelo-Vista-Controlador (MVC):
- **Modelo**: Clases que representan las entidades del dominio (Usuario, Contacto, Mensaje, etc.)
- **Vista**: Interfaces gráficas desarrolladas con Swing
- **Controlador**: Clase principal AppChat que gestiona la lógica de negocio

## Estructura del proyecto
- `src/main/java`: Código fuente de la aplicación
  - `umu.tds.apps.controlador`: Controladores de la aplicación
  - `umu.tds.apps.modelo`: Clases del modelo de dominio
  - `umu.tds.apps.persistencia`: Clases para la persistencia de datos
  - `umu.tds.apps.resources`: Recursos de la aplicación (imágenes, iconos)
  - `umu.tds.apps.vista`: Interfaces gráficas de usuario
  - `umu.tds.apps.vista.customcomponents`: Componentes personalizados para la UI

## Autores
- [@evaapinaa](https://www.github.com/evaapinaa)
- [@OkeV2](https://www.github.com/OkeV2)
