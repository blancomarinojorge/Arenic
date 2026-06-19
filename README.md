# Arenic

## Descripción

**Arenic** é unha aplicación web enfocada nos procesos de reserva de pistas de tenis,
pádel e pickleball, dirixida tanto aos clubes que dispoñen das instalacións como
aos xogadores que queren reservalas de maneira rápida e fiable.

Os clubes poden configurar as súas pistas,
establecer horarios e definir prezos de forma sinxela. O sistema permite aplicar descontos
exclusivos para socios e membros, fomentando a fidelidade. Toda a xestión de reservas queda centralizada,
eliminando chamadas telefónicas e anotacións en papel.

Os xogadores poden consultar a dispoñibilidade en tempo real e confirmar a súa reserva en segundos. Arenic
inclúe unha funcionalidade de división do pagamento entre participantes, de xeito que cada xogador pode aboar
a súa parte de forma independente, sen que ninguén teña que adiantar o importe total.

### Stack

<p align="left">
  <img src="https://skillicons.dev/icons?i=spring,java,angular,postgres,figma,docker,githubactions,digitalocean" />
</p>

- **Springboot** 3.5.14 con Java 21
    - Framework robusto e moi usado na industria, con boa documentación e gran comunidade.
    - Java 21 ao ser LTS garante soporte a longo prazo e mellora de rendemento fronte a versións anteriores.
- **Angular** 18
    - Framework completo (routing, formularios, HTTP...) sen depender de moitas librarías externas.
    - Ao usar TypeScript dá maior seguridade de tipos fronte a JavaScript puro.
- **PostgreSQL** 17
    - Base de datos relacional de código aberto, fiable e con moi bo soporte para tipos de datos complexos.
    - Boa integración con Spring Boot a través de JPA/Hibernate.
- **Figma** para o deseño do sistema, creando os compoñentes que despois se pasan a Angular.
  - Permite deseñar e prototipar a interface antes de programala, detectando problemas de UX pronto.
  - Os compoñentes e estilos definidos serven de referencia directa para crear os compoñentes en Angular.
- **Docker** e DockerHub
    - Permite empaquetar a aplicación e as súas dependencias de xeito reproducible en calquera entorno.
    - Facilita o despregue automatizado xunto coas GitHub Actions.
- **Github Actions** para o despregue.
    - Integración nativa con GitHub, sen necesidade de configurar nin manter infraestrutura propia.
    - Permite automatizar build, test e despregue directamente desde o repositorio.
- DigitalOcean VPS
    - Prezo accesible para un proxecto persoal/académico fronte a outros provedores cloud máis grandes.
    - Configuración sinxela e suficiente control para xestionar Docker directamente no servidor.
- Bruno para facer test a api de springboot
    - Alternativa de código aberto a Postman, sen necesidade de conta nin sincronización na nube.
    - As coleccións gárdanse en ficheiros de texto, polo que se poden versionar con Git xunto co proxecto.
## Instalación / Posta en marcha

### Demo pública

A aplicación atopase despregada nun VPS, polo que se pode acceder mediante a url https://arenic.online.

Facilitase un usuario de proba:

```shell
#usuario
player@arenic.com
#contrasinal
Player1234
```

### Entorno local e desenvolvemento

Para o desarrollo en angular e springboot usarase docker para a base de datos e os servidores embebidos de angular e springboot, xa que é a maneira mais áxil de traballar co stack.

#### Requisitos previos

Asegúrate de ter instalado o seguinte antes de executar o proxecto:

| Ferramenta | Versión mínima |
|------------|----------------|
| Java | 21             | 
| Maven | 3.9            | 
| Node.js | 22             | 
| Angular CLI | 18             | 
| Docker | 24             | 

**1. Base de datos**

Copia o ficheiro de variables de entorno e cubre os valores:

```bash
cp .env.example .env
```

Inicia o contedor de Postgres:

```bash
docker compose -f docker-compose.local.yml up db
```

**2. Backend**

```bash
cd backend
mvn spring-boot:run
```

**3. Frontend**

```bash
cd frontend
npm install
ng serve
```

A aplicación estará dispoñible en `http://localhost:4200`.

## Uso

> *TODO*: Es este apartado describe brevemente cómo se usará este software. Plantea un uso básico (como un *quickstart*) Si tiene una interfaz de terminal, puedes describir aquí su sintaxis. Si tiene una interfaz gráfica de usuario, describe aquí **sólo el uso** (a modo de sumario) **de los aspectos más relevantes de su funcionamiento** (máxima brevedad, como si fuese un anuncio reclamo o comercial).
> Podrías incluso hacer una pequeña demo en *gif* o un pantallazo de la misma muy descriptivo. Recueda que esto es un reclamo para que la prueben o lean tu documentación más extensa.

## Sobre o autor

Son Jorge, desenvolvedor con case 2 anos de experiencia profesional: un ano traballando con PHP e Laravel, e outro ano (actualmente en activo) con Java, WebLogic, JSPs, HTML, JavaScript e CSS. As tecnoloxías que mellor domino son Laravel (Blade, Tailwind, compoñentes) e Java/JavaScript.

Decanteime polo stack deste proxecto (Spring Boot, Spring Data JPA, Spring Security e Angular) porque é a dirección que están tomando varios proxectos novos na miña empresa, e quería aprendelo pola miña conta antes de usalo no traballo. Tamén aproveitei para aprender Figma, deseñando un sistema de deseño completo con compoñentes e variables antes de levalo a Angular.

En canto á temática, escollín unha aplicación de reserva de pistas de tenis porque é un deporte que practico a diario, e penso que pode ser unha ferramenta moi útil para clubs reais.

Podes contactar comigo en blancomarinojorge@gmail.com ou a través de [LinkedIn](https://www.linkedin.com/in/jorge-blanco-mari%C3%B1o-267a4917a/).


## Licencia

![License](https://img.shields.io/badge/License-MIT-yellow.svg)

Este proxecto está licenciado baixo a **[MIT License](./LICENSE)**.

Esta licenza permite o uso, copia, modificación e distribución libre do código, mantendo o aviso de copyright orixinal. Isto permite que, por exemplo, outro alumno poida darlle continuidade ao proxecto en futuros cursos.

## Documentación

Este proxecto dispón dunha [documentación máis extensa](doc/doc.md) que recomendo revisar para entender en detalle a arquitectura, decisións de deseño e funcionamento da aplicación.

## Guía de contribución

Se queres contribuír a este proxecto, podes facelo de varias formas:

- **Issues**: Para reportar bugs ou propor novas funcionalidades, abre unha [issue](../../issues) describindo claramente o problema ou a mellora proposta.
- **Pull Requests**:
    1. Fai un fork do repositorio.
    2. Crea unha rama a partir de `main` co formato `feature/nome-da-funcionalidade` ou `fix/nome-do-bug`.
    3. Realiza os cambios e asegúrate de que o backend (Spring Boot) e o frontend (Angular) compilan sen erros.
    4. Se engades unha nova funcionalidade na API, engade tamén a súa colección de tests en Bruno.
    5. Abre un Pull Request describindo os cambios realizados.
- **Estilo de código**: Sigue a convención de código xa existente no proxecto (formato, nomes de clases/variables, etc.) para manter a coherencia.

Calquera contribución, por pequena que sexa (corrección de erros, optimización, tests, documentación...), é benvida.