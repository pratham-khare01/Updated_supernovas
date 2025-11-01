# Supernovae website
This is the repository for the supernovae website re-design. All code for the project will be stored here.
## Tech stack

### Front-end

- SvelteKit (for routing only and pages display only, no back-end features used)
	- Svelte docs: https://svelte.dev/docs/svelte/overview
	- SvelteKit docs: https://svelte.dev/docs/kit/introduction
- Tailwind (for styling) (https://tailwindcss.com)
- DaisyUI (Tailwind extension that helps with styling) (https://daisyui.com)
	- The theme will be based off the "winter" theme, and for the dark theme (if needed), the theme will be based off the "night" theme
- Tailwind Typography (Tailwind extension that helps with structured content) (https://github.com/tailwindlabs/tailwindcss-typography)
- Tiptap (for content editor) (https://tiptap.dev/docs)

### Back-end

- Kotlin (https://kotlinlang.org/docs/home.html)
- Spring Boot Webflux (https://docs.spring.io/spring-framework/reference/web/webflux.html)
- PostgreSQL (https://www.postgresql.org/docs/)

## How to build

To build, there are 4 tools that are needed to be installed:
- Git
	- Windows: https://git-scm.com/downloads/win
	- Mac: https://git-scm.com/downloads/mac
	- Linux: https://git-scm.com/downloads/linux
- IntelliJ IDEA (refer to https://www.jetbrains.com/idea/download/ for downloads)
- bun (used for building the SvelteKit project) (refer to https://bun.sh/ on how to install)
- OpenJDK 24 (latest Java version as of writing this)
	- Windows: refer to https://www.youtube.com/watch?v=rOD4llj6tJg (but don't use JDK 14, use JDK 24)
	- Mac: run `brew install openjdk@24`

### PostgreSQL

PostgreSQL is required for development and testing; it is not required for building the application. A PostgreSQL server needs to be running in the background for the application to actually function properly.
- Mac downloads: https://www.postgresql.org/download/macosx/
- Windows downloads: https://www.postgresql.org/download/windows/

It is recommended that the database you use is called `supernovae`. To create a database, connect to the Postgres server and execute the query

```sql
CREATE DATABASE supernovae;
```

You need to create a file called `.env` in the root of the project. Here is an example of a `.env` file:

```
DATABASE_URL_R2DBC=r2dbc:postgresql://localhost:5432/supernovae
DATABASE_URL_JDBC=jdbc:postgresql://localhost:5432/supernovae
DATABASE_USER=postgres
DATABASE_PASSWORD=example
```

Change the values accordingly. If you are using a database called `supernovae` and the server is running on port 5432, don't worry about changing the `DATABASE_URL_R2DBC` and `DATABASE_URL_JDBC` variables.

### Instructions

1. Clone this repository using the command `git clone https://github.com/Bluheir/supernovae-redesign.git`
2. Open the folder in IntelliJ IDEA
3. On the sidebar on the right, click the Gradle elephant icon, then click on the build folder, and click on the `fullBuild` task
<img width="2558" height="1551" alt="image" src="https://github.com/user-attachments/assets/1b06dc5c-2430-428c-83a6-704d30c6468a" />
4. Once that is done, run the `jar` task. The produced jar file will be located in the directory `/build/libs/`

### Instructions for running development

Note that the project should have been built before doing these steps.
1. Open a new terminal, and cd into the project folder.
2. Run the command `bun run dev`
3. Open up IntelliJ IDEA, and click on the run button next to `SupernovaeApplicationKt`.
4. Open up a browser window, and go to the url `http://localhost:8080`. Saving changes in the SvelteKit project will automatically reload the page to show those changes.
