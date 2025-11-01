<script lang="ts">
  import { goto } from '$app/navigation';
  import { page } from '$app/stores';
  import { fade, fly } from 'svelte/transition';
  import { onMount } from 'svelte';

  let submitting = false;
  let showSuccess = false;

  let form = {
    fullname: '',
    company: '',
    role: '',
    email: '',
    phone: '',
    locations: '',
    chargerTypes: [] as string[],
    description: '',
    consent: false
  };

  function toggleType(t: string) {
    const idx = form.chargerTypes.indexOf(t);
    if (idx === -1) form.chargerTypes.push(t);
    else form.chargerTypes.splice(idx, 1);
  }

  async function submitForm() {
    if (!form.email || !form.company || !form.fullname) {
      alert('Please provide full name, company, and email.');
      return;
    }

    submitting = true;
    try {
      const res = await fetch('/api/forms/become-charging-partner', {
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify(form)
      });

      if (res.ok) {
        goto('/solutions/ev-charging?submitted=1');
      } else {
        alert('Submission failed.');
      }
    } catch {
      alert('Network error.');
    } finally {
      submitting = false;
    }
  }

  $: {
    const url = $page.url;
    showSuccess = url.searchParams.get('submitted') === '1';
  }

  onMount(() => {
    if (showSuccess) {
      setTimeout(() => {
        window.location.href = '/solutions/ev-charging';
      }, 4000);
    }
  });
</script>

<svelte:head>
  <title>EV Charging & Energy Infrastructure | Supernovae Hub</title>
  <meta
    name="description"
    content="Turnkey charging solutions, CPO services, DER integration and grant support. Become a charging partner."
  />
</svelte:head>

<section class="max-w-5xl mx-auto p-10 space-y-12">
  <!-- Hero -->
  <div class="text-center">
    <h1 class="text-4xl font-extrabold text-emerald-700 mb-3">
      EV Charging & Energy Infrastructure
    </h1>
    <p class="text-gray-600 max-w-2xl mx-auto">
      Turnkey AC/DC charging, CPO services, DER integration, and grant support. We help businesses deploy and operate efficient, future-ready charging infrastructure.
    </p>
  </div>

  <!-- Main Form -->
  <div class="bg-gradient-to-b from-emerald-50 to-white shadow-xl rounded-3xl p-10 border border-emerald-100">
    <h2 class="text-2xl font-semibold text-emerald-800 mb-6 border-b-2 border-emerald-200 pb-2">
      Become a Charging Partner
    </h2>

    <form on:submit|preventDefault={submitForm} class="space-y-10">
      <!-- Section 1 -->
      <div>
        <h3 class="text-lg font-semibold text-emerald-600 mb-3">1. Basic Information</h3>
        <div class="grid md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Full Name *</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-emerald-400 focus:border-emerald-400 transition-all"
              bind:value={form.fullname}
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Company *</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-emerald-400 focus:border-emerald-400 transition-all"
              bind:value={form.company}
              required
            />
          </div>
        </div>
      </div>

      <!-- Section 2 -->
      <div>
        <h3 class="text-lg font-semibold text-emerald-600 mb-3">2. Contact Details</h3>
        <div class="grid md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Role</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-emerald-400 focus:border-emerald-400 transition-all"
              bind:value={form.role}
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Email *</label>
            <input
              type="email"
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-emerald-400 focus:border-emerald-400 transition-all"
              bind:value={form.email}
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-emerald-400 focus:border-emerald-400 transition-all"
              bind:value={form.phone}
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Location(s) of Interest</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-emerald-400 focus:border-emerald-400 transition-all"
              bind:value={form.locations}
            />
          </div>
        </div>
      </div>

      <!-- Section 3 -->
      <div>
        <h3 class="text-lg font-semibold text-emerald-600 mb-3">3. Charger Type</h3>
        <div class="grid sm:grid-cols-2 md:grid-cols-4 gap-4">
          {#each ['AC', 'DC', 'Hardware+Service', 'OCPP'] as type}
            <label
              class="flex items-center space-x-2 border border-gray-300 rounded-xl p-3 cursor-pointer hover:bg-emerald-50 transition-all"
            >
              <input
                type="checkbox"
                checked={form.chargerTypes.includes(type)}
                on:change={() => toggleType(type)}
                class="w-5 h-5 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500"
              />
              <span class="text-gray-700">{type}</span>
            </label>
          {/each}
        </div>
      </div>

      <!-- Section 4 -->
      <div>
        <h3 class="text-lg font-semibold text-emerald-600 mb-3">4. Capability Description</h3>
        <textarea
          class="w-full border border-gray-300 rounded-xl px-4 py-2 min-h-[120px] focus:ring-2 focus:ring-emerald-400 focus:border-emerald-400 transition-all"
          bind:value={form.description}
        ></textarea>
      </div>

      <!-- Section 5 -->
      <div>
        <h3 class="text-lg font-semibold text-emerald-600 mb-3">5. Consent</h3>
        <label class="flex items-center space-x-2">
          <input
            type="checkbox"
            bind:checked={form.consent}
            class="w-5 h-5 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500"
          />
          <span class="text-gray-700 text-sm">I consent to being contacted by Supernovae Hub</span>
        </label>
      </div>

      <!-- Submit -->
      <div class="pt-6">
        <button
          type="submit"
          disabled={submitting}
          class="w-full bg-emerald-600 text-white py-3 rounded-xl font-semibold text-lg shadow-md hover:bg-emerald-700 transform hover:-translate-y-1 transition-all duration-300 focus:ring-4 focus:ring-emerald-300 disabled:opacity-60 disabled:cursor-not-allowed"
        >
          {submitting ? 'Submitting...' : 'Submit'}
        </button>
      </div>
    </form>
  </div>

  {#if showSuccess}
    <!-- Success Overlay -->
    <div
      in:fade={{ duration: 400 }}
      class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm z-50"
    >
      <div
        in:fly={{ y: 25, duration: 400 }}
        class="bg-white rounded-3xl shadow-2xl p-10 max-w-md w-full text-center border-t-8 border-green-500"
      >
        <svg
          class="mx-auto mb-6 text-green-500"
          xmlns="http://www.w3.org/2000/svg"
          width="80"
          height="80"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          stroke-width="2"
        >
          <circle cx="12" cy="12" r="10" stroke="currentColor" fill="none" />
          <path stroke="currentColor" stroke-width="2" d="M8 12l3 3 5-5" />
        </svg>

        <h2 class="text-2xl font-bold text-gray-800 mb-2">Success!</h2>
        <p class="text-gray-600 mb-6">Thank you for your interest. We'll get in touch soon.</p>

        <a
          href="/solutions/ev-charging"
          class="inline-block bg-green-500 text-white px-6 py-2 rounded-lg font-medium hover:bg-green-600 transition-all"
        >
          Close
        </a>
      </div>
    </div>
  {/if}
</section>
