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

## Updated Section


## 1. Add the Svelte Pages

**Location:**
`svelte/src/routes/solutions/`

**Files to create:**

* `ev-charging/+page.svelte`
* `ev-charging/+page.ts`*(if you’re separating logic)*

Both pages should include:

* Tailwind styling
* The interactive form with rounded inputs, animations, and transitions
* Success overlay and fade/fly animations
* The updated `<script lang="ts">` section you’re already working on

Then test with:

<pre class="overflow-visible!" data-start="608" data-end="631"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-bash"><span><span>bun run dev
</span></span></code></div></div></pre>

or

<pre class="overflow-visible!" data-start="635" data-end="658"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-bash"><span><span>npm run dev
</span></span></code></div></div></pre>

---

## 2. Backend (Kotlin / Spring Boot)

**Files to add:**

* `FormController.kt` → `src/main/kotlin/com/supernovae/forms/controller/`
* `FormService.kt` → `src/main/kotlin/com/supernovae/forms/service/`

Both handle:

* `/api/forms/become-charging-partner`
* Validation
* DB persistence
* Optional email notification

If you haven’t implemented them yet, I can write clean working versions that integrate with Spring Boot and a repository layer.

---

## 3. Database Migration

**Location:**
`src/main/resources/db/migration/V1__create_form_submissions.sql`

**Example contents:**

<pre class="overflow-visible!" data-start="1242" data-end="1626"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-sql"><span><span>CREATE</span><span> </span><span>TABLE</span><span> form_submissions (
    id SERIAL </span><span>PRIMARY</span><span> KEY,
    fullname </span><span>VARCHAR</span><span>(</span><span>100</span><span>) </span><span>NOT</span><span> </span><span>NULL</span><span>,
    company </span><span>VARCHAR</span><span>(</span><span>100</span><span>) </span><span>NOT</span><span> </span><span>NULL</span><span>,
    role </span><span>VARCHAR</span><span>(</span><span>100</span><span>),
    email </span><span>VARCHAR</span><span>(</span><span>150</span><span>) </span><span>NOT</span><span> </span><span>NULL</span><span>,
    phone </span><span>VARCHAR</span><span>(</span><span>50</span><span>),
    locations </span><span>VARCHAR</span><span>(</span><span>255</span><span>),
    charger_types TEXT,
    description TEXT,
    consent </span><span>BOOLEAN</span><span> </span><span>DEFAULT</span><span> </span><span>FALSE</span><span>,
    submitted_at </span><span>TIMESTAMP</span><span> </span><span>DEFAULT</span><span> </span><span>CURRENT_TIMESTAMP</span><span>
);
</span></span></code></div></div></pre>

If Flyway or Liquibase isn’t configured, this SQL can be run manually.

---

## 4. README Additions

Append a short section in your root `README.md`:

<pre class="overflow-visible!" data-start="1778" data-end="1898"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-markdown"><span><span>## EV Charging Partner Form Setup</span><span>

</span><span>### Backend</span><span>
</span><span>-</span><span> Configure DB credentials in </span><span>`application.properties`</span><span>:
</span></span></code></div></div></pre>

spring.datasource.url=jdbc:postgresql://localhost:5432/supernovae
spring.datasource.username=your\_username
spring.datasource.password=your\_password
spring.jpa.hibernate.ddl-auto=none

<pre class="overflow-visible!" data-start="2092" data-end="2131"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre!"><span><span>
- </span><span>To</span><span> send admin emails, </span><span>set</span><span>:
</span></span></code></div></div></pre>

app.admin.email=[admin@supernovae.com]()

<pre class="overflow-visible!" data-start="2173" data-end="2210"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre!"><span><span>
</span><span>### Frontend</span><span>
From `/svelte`:
</span></span></code></div></div></pre>

bun install
bun run dev

<pre class="overflow-visible!" data-start="2235" data-end="2337"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre!"><span><span>This runs the Svelte frontend </span><span>on</span><span> `http:</span><span>//localhost:5173` and proxies API calls to the backend.</span><span>
</span></span></code></div></div></pre>

---

## 5. Commit Guide

When ready, from the repo root:

<pre class="overflow-visible!" data-start="2395" data-end="2796"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-bash"><span><span>git add svelte/src/routes/solutions/ev-charging/+page.svelte
git add src/main/kotlin/com/supernovae/forms/controller/FormController.kt
git add src/main/kotlin/com/supernovae/forms/service/FormService.kt
git add src/main/resources/db/migration/V1__create_form_submissions.sql
git add README.md
git commit -m </span><span>"Add EV Charging Partner form, backend endpoints, DB migration, and docs"</span><span>
git push</span></span></code></div></div></pre>
