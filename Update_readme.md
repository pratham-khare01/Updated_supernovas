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
