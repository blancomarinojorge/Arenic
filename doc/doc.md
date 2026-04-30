# Xestión de reservas de pistas deportivas

- [Introducción](#introducción)
- [Estado de arte o análisis del contexto](#estado-de-arte-o-análisis-del-contexto)
- [Propósito](#propósito)
- [Objetivos](#objetivos)
- [Alcance](#alcance)
- [Análise](#análise)
  - [Requerimentos e funcionalidades](#requerimentos-e-funcionalidades)
  - Base de datos
- [TODO: A partir de este punto eres libre de organizar la documentación como estimes pero debes desarrollar el cuerpo de tu proyecto con apartados y subapartados que completen tu documentación](#todo-a-partir-de-este-punto-eres-libre-de-organizar-la-documentación-como-estimes-pero-debes-desarrollar-el-cuerpo-de-tu-proyecto-con-apartados-y-subapartados-que-completen-tu-documentación)
- [Conclusiones](#conclusiones)
- [Referencias, Fuentes consultadas y Recursos externos: Webgrafía](#referencias-fuentes-consultadas-y-recursos-externos-webgrafía)

## Introducción

O presente proxecto ten como obxectivo principal o deseño e desenvolvemento dunha **aplicación web** destinada á reserva e xestión integral de instalacións deportivas. A proposta xorde da necesidade de modernizar a interacción entre os centros deportivos e os seus usuarios, eliminando as barreiras administrativas e fomentando a creación dunha comunidade activa e conectada.

### Funcionalidades para o Usuario (Xogadores)
A aplicación foi concibida baixo unha filosofía *mobile-first*, garantindo unha experiencia de usuario intuitiva e fluída desde calquera dispositivo móbil. Entre as súas capacidades destacan:

* **Sistema de Reservas:** Proceso simplificado para a selección de pistas e horarios en tempo real.
* **Xestión de Competicións:** Ferramentas para a organización e participación en torneos, permitindo un seguimento dinámico de cadros de xogo e clasificacións.
* **Perfil Social e Gamificación:** Os usuarios poderán monitorizar os seus progresos persoais, comparar estatísticas cos perfís da comunidade e outorgar puntuacións aos clubs baseándose na calidade das súas instalacións e servizos.

### Panel de Control para Administradores (Clubs)
Para os propietarios e xestores de centros, a plataforma ofrece un **ecosistema de administración robusto** que permite un control total sobre a operativa diaria:

* **Dashboard Analítico:** Visualización de datos clave mediante gráficos interactivos que facilitan a toma de decisións estratéxicas.
* **Control de Finanzas:** Xestión automatizada de reservas e fluxos de pago de forma segura.
* **Explotación de Datos:** Capacidade para xerar e imprimir informes detallados sobre o rendemento do club, a ocupación das pistas e as métricas de usuarios.

## Estado de arte o análisis del contexto

* **Contexto social:** O auxe dos deportes de raqueta nos últimos anos xerou unha alta demanda de pistas. Os usuarios actuais buscan inmediatez e fuxen das reservas telefónicas ou presenciais.
* **Necesidades a cubrir:**
    * Para o xogador: Atopar pistas dispoñibles en tempo real, organizar torneos de forma sinxela e interactuar con outros xogadores.
    * Para o dono: Dixitalizar a axenda, evitar pistas baleiras e obter métricas de rendemento do club.
* **Competencia:** Existen aplicacións como Playtomic. Aínda que son eficaces, moitos clubs pequenos buscan solucións máis personalizadas ou con menores comisións por reserva. Este proxecto busca replicar as funcións core cun enfoque máis centrado na analítica para o dono.
* **Oportunidade de negocio:** É un modelo SaaS (*Software as a Service*) escalable. Pódese comercializar mediante unha subscrición mensual para os clubs ou unha pequena comisión por transacción de reserva.

## Propósito
O propósito principal é centralizar a xestión deportiva de centros de tenis e pádel nunha aplicación web intuitiva, optimizando o proceso de reserva para o cliente final e profesionalizando a administración diaria para os propietarios dos clubs.

## Objetivos

* **Desenvolvemento técnico:**
    * Implementar un sistema de autenticación seguro con diferentes roles (Xestor / Cliente).
    * Crear un calendario dinámico para a reserva de pistas en tempo real.
    * Deseñar un panel de control para os donos con gráficas de ocupación.
    * Desenvolver un módulo de creación e xestión de torneos.
    * No caso de ser posible por cuestións de tempo, levar a cabo a implementación de un chat en tempo real xogador-xogador e xogador-xestor.
* **Aprendizaxe persoal:**
    * Dominar as tecnoloxías Angular, Springboot, Tailwind e Figma, así como librerías de gráficas e a xestión de pagos.
    * Mellorar no ámbito de seguimento de tarefas e xestión de proxectos.
## Alcance

### O que inclúe:
* **Módulo de Clientes:** Rexistro, procura de pistas dispoñibles por data/hora, confirmación de reservas e historial de partidos.
* **Módulo de Administración:** Xestión de pistas (crear/editar/borrar), visualización de reservas en formato lista e calendario, e xeración de informes en PDF.
* **Módulo Social:** Sistema de clasificación/ranking de xogadores baseado en resultados subidos, sistema de amizades entre xogadores con publicación de fotos e textos cos que poder interactuar e sistema de valoración de instalacións (1-5 estrelas).
* **Analítica:** Panel visual con gráficas de ingresos e horas de maior ocupación.

### O que queda fóra:
* **Chat en tempo real:** Queda marcado como unha mellora futura dependente do tempo dispoñible.
* **Pasarela de pago real:** Realizarase unha simulación de pago, pero non se integrará con Stripe ou PayPal nesta fase.
* **Aplicación Móbil Nativa:** O proxecto centrarase nunha web *responsive* optimizada para móbiles, pero non nunha app de iOS/Android. No caso de seguir
desenvolvendo o proxecto, planificarase unha app para Android.

---

# Análise

## Requerimentos e funcionalidades

A funcionalidade principal da aplicación é a xestión flexible e segura
de pistas deportivas de distintos clubes. Para iso, debense cumplir os seguintes requerimentos:

Contará con **clubes** deportivos, dos cales nos interesa saber o seu nome e dirección, ademais
de poder saber se se atopa ou non activo. Cada clube poderá ter varias **pistas**, das cales
sabemos o tipo de pista, nome e número de pista. A creación de horarios e prezos das pistas
debe ser flexible e permitir a visualización de datos históricos, podendo variar por día da semana
e hora.

Dos **usuarios** interesanos saber o nome completo, email, contrasinal e teléfono móvil. Poderán
estar relacionados con un ou varios clubes, podendo ter varios roles (DONO, EMPREGADO, ENTRENADOR, MEMBRO)
dentro de un clube. Por exemplo, un usuario pode ser dono e entrenador de un clube, o cal lle dará
a posibilidade de dar clases e consultar as análiticas. Ao mesmo tempo pode ser membro de outro clube, o cal
lle permitirá facer reservas con un prezo mais reducido. Un clube sempre terá un usuario creador, o
cal non poderá ser borrado ata a eliminación do clube.

Poderanse realizar **reservas** nas pistas dispoñibles, sendo posible a participación de varios
usuarios na mesma. A aplicación permitirá que os usuarios participantes divídan o custo da reserva,
deixandolles pagar só a sua parte, o prezo completo, ou elixir se queren pagar por algún dos participantes. 
No caso de que algún deles non realice o pago antes da hora posterior a finalización da reserva,
será o usuario que a creou quen pague o restante.

Para facilitar o sistema de pago, os usuarios poderán rexistrar varios **metodos de pago** cos cales
realizar as reservas.




## Base de datos

Para satisfacer todos os requerimentos funcionales deseñouse o seguinte esquema Entidade-Relación.

![ER diagram.png](img/database/ER%20diagram.png)

---

## TODO: A partir de este punto eres libre de organizar la documentación como estimes pero debes desarrollar el cuerpo de tu proyecto con apartados y subapartados que completen tu documentación

> Hemos elaborado un [checklist](checklist.md) de puntos necesarios para tu PFC, para que revises estas recomendaciones/especificaciones.
> Apóyate en tu tutor/a si tienes duda de cómo organizar tu proyecto y estos apartados/subapartado. Cada proyecto y su contexto determinará la mejor forma de estructurarlo. Piensa bien cómo lo vas a hacer.

## Conclusiones

> Deja esta apartado para el final. Realiza un resumen de todo lo que ha supuesto la realización de tu proyecto. Debe ser una redacción breve. Un resumen de los hitos conseguidos más importantes y de lo aprendido durante el proceso.
> Puede ser un buen punto de partida para organizar tu presentación y ajustarla al tiempo que tienes.

## Referencias, Fuentes consultadas y Recursos externos: Webgrafía

> *TODO*: Enlaces externos y descipciones de estos enlaces que creas conveniente indicar aquí. Generalmente ya van a estar integrados con tu documentación, pero si requieres realizar un listado de ellos, este es el lugar.


---

# Análisis e deseño

## Análisis de requisitos

X é unha aplicación creada para axilizar a xestión de resevas de pistas deportivas e
facilitar a xestión de torneos