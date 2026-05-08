# Arenic, xestión de reservas de pistas deportivas

- [Introducción](#introducción)
- [Estado de arte o análisis del contexto](#estado-de-arte-o-análisis-del-contexto)
- [Propósito](#propósito)
- [Objetivos](#objetivos)
- [Alcance](#alcance)
- [Análise](#análise)
  - [Requerimentos e funcionalidades](#requerimentos-e-funcionalidades)
  - [Base de datos](#base-de-datos)
  - [Desarrollo](#desarrollo)
    - [Creacion de repositorio github](#creacion-de-repositorio-github)
    - [Creacion de VPS e dominio](#creacion-de-vps-en-digitalocean-e-dominio-en-namecheap)
  - [Base do deseño UI e identidade da empresa](#base-do-deseño-ui-e-identidade-da-empresa)
    - [Nome da aplicación](#nome-da-aplicación)
    - [Logo](#logo)
- [Configuración do entorno]
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
* **Analítica:** Panel visual con gráficas de ingresos e horas de maior ocupación.

### O que queda fóra:
* **Chat en tempo real:** Queda marcado como unha mellora futura dependente do tempo dispoñible.
* **Pasarela de pago real:** Realizarase unha simulación de pago, pero non se integrará con Stripe ou PayPal nesta fase.
* **Aplicación Móbil Nativa:** O proxecto centrarase nunha web *responsive* optimizada para móbiles, pero non nunha app de iOS/Android. No caso de seguir
desenvolvendo o proxecto, planificarase unha app para Android.
* **Módulo Social:** Sistema de clasificación/ranking de xogadores baseado en resultados subidos, sistema de amizades entre xogadores con publicación de fotos e textos cos que poder interactuar e sistema de valoración de instalacións (1-5 estrelas). Aínda que no inicio de deseño da aplicación se tiña pensado incluir este apartado, tras a fase de análise e creación da base de datos replantease por cuestións de alcance e tempo de desenvolvemento. 
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

Deseñarase a base de datos tendo en mente que mais adiante se engadiran partidos, entrenos e torneos, entidades que terán
que relacionarse coa tabla de reservas para xestionar a reserva dos participantes.


## Base de datos

Para satisfacer todos os requerimentos funcionales deseñouse o seguinte esquema Entidade-Relación.

![ER diagram.png](img/database/ER%20diagram.png)

## Desarrollo

Utilizaranse o seguinte stack tecnolóxico:

- Springboot 3.5.14 con Java 21
- Angular 18
- PostgreSQL 17
- Docker e DockerHub
- Github Actions para o despregue. 
  - Jenkins levaría demasiado tempo de configuración e mantemento.
  - Gitlab CI sería a opción mais optima neste caso, pero ao estar enlazado a miña conta de instituto tería que pasar o
  proxecto a github unha vez finalizado o curso, tendo que facer a migración a Github Actions para poder seguin facendo
  cambios na aplicación no futuro.
- DigitalOcean VPS

### Creacion de repositorio github

Crease un novo repositorio en github para poder usar github actions e configurase git en local para subir os cambios en ambos repositorios:

```shell
git remote add github git@github.com:blancomarinojorge/Arenic.git
git remote set-url --add --push origin git@github.com:blancomarinojorge/Arenic.git
git remote set-url --add --push origin ssh://git@gitlab.iessanclemente.net:60600/dawd/a17jorgebm1.git
```

### Creacion de VPS en Digitalocean e dominio en NameCheap

Crease o vps para o proxecto.

```text
206.189.18.120
arenic.online
```

Configurase o dominio para que apunte ao VPS:

| Type | Host | Value | TTL |
| :--- | :--- | :--- | :--- |
| A Record | www | 206.189.18.120 | 30 min |
| A Record | @ | 206.189.18.120 | 30 min |


- [ ] facendo para acabar coa CI de github actions, mirar e preguntar se
esta usando o tema do dominio, que para eso o comprei, redirigir todo a 
443 para que a conexion sexa segura
- [ ] unha vez acabado esto, montar os proyectos de springboot e angular

## Base do deseño UI e identidade da empresa

### Nome da aplicación

Quero que a aplicación se sinta sofisticada e premium, facendo referencia a elegancia dos deportes de raqueta. Ao mesmo
tempo ten que transmitir a rapidez e a emoción do deporte.

Opcions:
 - Terra
 - Arenic
 - YourCourt
 - OpenCourt

Finalmente decidese o nome **Arenic**, o can fai referencia as pistas de terra batida e as areas nas que se disputan enfrentamentos. Fixose unha busqueda do uso da palabra en outros ámbitos ou aplicacións, sendo esta a menos utilizada e a que mais pode definir a empresa no mercado, dandolle dende un comezo un nome recoñecible e diferenciable.

### Logo

A partir do nome da empresa faise o deseño do logo en figma e gimp, usando a plataforma pinterest como referencia de outros deseños. A continuación mostrase un resumo do proceso de creación a partir de unha imaxe base modificada para satisfacer os requisitos da nosa marca.

![logo_process.png](img/ui/brand/logo_process.png)

Logo final:

![logo_final.png](img/ui/brand/logo_final.png)

---

# Configuración do entorno

## Configuración do VPS

Usarase un único servidor VPS tanto para o frontend como para o backend e configurarase para que use
unha conexión segura mediante o porto 443 facendo que un servidor nginx redirixa a este porto todas
as peticións provintes do 80.

Como o servidor VPS ten poucos recursos, tomouse a decisión de montar as imaxes dende a pipeline de github e subir as imaxes dos contedores
a DockerHub, de esta maneira o VPS só descarga os contedores e iniciaos, gastando moitos menos recursos.

1.  **Facemos update e instalamos Docker:**
    ```bash
    sudo apt update && sudo apt upgrade -y
    sudo apt install ca-certificates curl gnupg lsb-release -y
    # Add Docker’s official GPG key
    sudo mkdir -p /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    # Set up the repository
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt update
    sudo apt install docker-ce docker-ce-cli containerd.io docker-compose-plugin -y
    ```
2.  Crease o usuario `deployer`:
    ```bash
    adduser deployer
    usermod -aG sudo,docker deployer
    ```
3. Configuro a clave publica ssh no usuario deployer para poder acceder con el por ssh:
    ```bash
    # 1. Create the .ssh directory for the deployer user (if it doesn't exist)
    mkdir -p /home/deployer/.ssh
    
    # 2. Copy the authorized_keys from root to deployer
    cp /root/.ssh/authorized_keys /home/deployer/.ssh/
    
    # 3. Change ownership so the 'deployer' user actually owns the file
    chown -R deployer:deployer /home/deployer/.ssh
    
    # 4. Set the strict permissions SSH requires
    chmod 700 /home/deployer/.ssh
    chmod 600 /home/deployer/.ssh/authorized_keys
    ```

3.  **Crease o directorio da aplicación, onde se subirán os cambios e se atopará o arquivo `.env`, arquivo necesario para que funcione docker-compose no momento de facer o despregue. É o único arquivo que se subirá manualmente por motivos de seguridade:**
    ```bash
    mkdir -p /home/deployer/app
    chown deployer:deployer /home/deployer/app
    ```
4. Como usuario **deployer** creo e configuro o arquivo .env:
    ```bash
    nano /home/deployer/app/.env
    ```
5. Indicar que no docker-compose limitase o acceso público a base de datos, solo deixando acceder mediante dende localhost (para acceder a do VPS usarase SSH):
    ```yaml
    # I don't actually want to expose the port to the internet, so I only map the port to localhost.
    # This way, we can access this port via SSH to the VPS but keeping it secure.
    ports:
      - "127.0.0.1:5432:5432"
    ```
   
### Configuración nginx con SSL

Para servir as peticións ao exterior contaremos con un contedor docker de nginx, configurado na carpeta
_/frontend/nginx.conf_. Configurarase para que sirva as peticións dirixidas a `/` ao frontend e `/api` ao
backend. Para configurar SSL usarase **certbot**.

1. Creanse as carpetas donde certbot deixará os ficheiros, para que docker non as cree automaticamente con permisos de root.
    ```bash
   #como usuario deployer
    mkdir -p /home/deployer/app/certbot/conf /home/deployer/app/certbot/www
    ```

2. Faise o deployment por primeira vez tendo solo a configuración para o porto **80** en nginx.
    ```text
    server {
        listen 80;
        server_name arenic.online www.arenic.online;
    
        # Certbot challenge location
        location /.well-known/acme-challenge/ {
            root /var/www/certbot;
        }
    
        # Redirect all HTTP traffic to HTTPS
        location / {
            return 301 https://$host$request_uri;
        }
    }
    ```

3. Generanse as claves SSL e descomentase a configuración para https de nginx:

    ```bash
    docker run -it --rm --name certbot -v "/home/deployer/app/certbot/conf:/etc/letsencrypt" -v "/home/deployer/app/certbot/www:/var/www/certbot" certbot/certbot certonly --webroot -w /var/www/certbot -d arenic.online -d www.arenic.online
    ```
   
4. Configuro o porto 433 na configuración de nginx e fago commit para que se volva a facer o despregue, esta vez co ssl habilitado:


## TODO: A partir de este punto eres libre de organizar la documentación como estimes pero debes desarrollar el cuerpo de tu proyecto con apartados y subapartados que completen tu documentación

> Hemos elaborado un [checklist](checklist.md) de puntos necesarios para tu PFC, para que revises estas recomendaciones/especificaciones.
> Apóyate en tu tutor/a si tienes duda de cómo organizar tu proyecto y estos apartados/subapartado. Cada proyecto y su contexto determinará la mejor forma de estructurarlo. Piensa bien cómo lo vas a hacer.

## Conclusiones

> Deja esta apartado para el final. Realiza un resumen de todo lo que ha supuesto la realización de tu proyecto. Debe ser una redacción breve. Un resumen de los hitos conseguidos más importantes y de lo aprendido durante el proceso.
> Puede ser un buen punto de partida para organizar tu presentación y ajustarla al tiempo que tienes.

## Referencias, Fuentes consultadas y Recursos externos: Webgrafía

> *TODO*: Enlaces externos y descipciones de estos enlaces que creas conveniente indicar aquí. Generalmente ya van a estar integrados con tu documentación, pero si requieres realizar un listado de ellos, este es el lugar.

- [Deploy con Docker y Ubuntu en 5 minutos (y mas) - Nginx y Certbot](https://www.youtube.com/watch?v=Hz_Jr2I_n8w)
- [Crash course Angular](https://www.youtube.com/watch?v=oUmVFHlwZsI&t=628s)
- [Install letsencrypt](https://www.inmotionhosting.com/support/website/ssl/lets-encrypt-ssl-ubuntu-with-certbot/)