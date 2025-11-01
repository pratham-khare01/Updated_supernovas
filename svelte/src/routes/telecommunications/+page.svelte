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
    email: '',
    phone: '',
    projectType: '',
    message: '',
    preferredContact: '',
    consent: false
  };

  // Detect success via URL param
  $: {
    const url = $page.url;
    showSuccess = url.searchParams.get('submitted') === '1';
  }

  async function submitForm() {
    if (!form.email || !form.fullname) {
      alert('Please fill in required fields.');
      return;
    }

    submitting = true;

    try {
      const res = await fetch('/api/forms/telecom-consultation', {
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify(form)
      });

      if (res.ok) {
        goto('/solutions/telecommunications?submitted=1');
      } else {
        alert('Error: submission failed.');
      }
    } catch {
      alert('Network error.');
    } finally {
      submitting = false;
    }
  }

  // Auto-close success modal
  onMount(() => {
    if (showSuccess) {
      setTimeout(() => {
        window.location.href = '/solutions/telecommunications';
      }, 4000);
    }
  });
</script>

<svelte:head>
  <title>Telecommunications Network Design & Delivery | Supernovae Hub</title>
</svelte:head>

<section class="max-w-5xl mx-auto p-10 space-y-12">
  <!-- Hero Section -->
  <div class="text-center">
    <h1 class="text-4xl font-extrabold text-indigo-700 mb-3">Telecommunications Network Design & Delivery</h1>
    <p class="text-gray-600 max-w-2xl mx-auto">
      Design and deploy private 4G/5G, fixed wireless access, and DWDM networks with precision and reliability.
    </p>
  </div>

  <!-- Consultation Form -->
  <div class="bg-gradient-to-b from-indigo-50 to-white shadow-xl rounded-3xl p-10 border border-indigo-100">
    <h2 class="text-2xl font-semibold text-indigo-800 mb-6 border-b-2 border-indigo-200 pb-2">
      Book a Consultation
    </h2>

    <form on:submit|preventDefault={submitForm} class="space-y-10">
      <!-- Personal Details -->
      <div>
        <h3 class="text-lg font-semibold text-indigo-600 mb-3">1. Personal Details</h3>
        <div class="grid md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Full Name *</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-indigo-400 focus:border-indigo-400 transition-all"
              bind:value={form.fullname}
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Company</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-indigo-400 focus:border-indigo-400 transition-all"
              bind:value={form.company}
            />
          </div>
        </div>
      </div>

      <!-- Contact Information -->
      <div>
        <h3 class="text-lg font-semibold text-indigo-600 mb-3">2. Contact Information</h3>
        <div class="grid md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Email *</label>
            <input
              type="email"
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-indigo-400 focus:border-indigo-400 transition-all"
              bind:value={form.email}
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-indigo-400 focus:border-indigo-400 transition-all"
              bind:value={form.phone}
            />
          </div>
        </div>
      </div>

      <!-- Project Info -->
      <div>
        <h3 class="text-lg font-semibold text-indigo-600 mb-3">3. Project Information</h3>
        <div class="grid md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Project Type</label>
            <select
              class="w-full border border-gray-300 rounded-xl px-4 py-2 bg-white focus:ring-2 focus:ring-indigo-400 focus:border-indigo-400 transition-all"
              bind:value={form.projectType}
            >
              <option value="">Select</option>
              <option>Private 4G/5G</option>
              <option>DWDM / Fibre</option>
              <option>Fixed Wireless Access</option>
              <option>Other</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Preferred Contact Time</label>
            <input
              class="w-full border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-indigo-400 focus:border-indigo-400 transition-all"
              bind:value={form.preferredContact}
            />
          </div>
        </div>

        <div class="mt-6">
          <label class="block text-sm font-medium text-gray-700 mb-1">Project Description</label>
          <textarea
            class="w-full border border-gray-300 rounded-xl px-4 py-2 min-h-[120px] focus:ring-2 focus:ring-indigo-400 focus:border-indigo-400 transition-all"
            bind:value={form.message}
          ></textarea>
        </div>
      </div>

      <!-- Consent -->
      <div>
        <h3 class="text-lg font-semibold text-indigo-600 mb-3">4. Consent</h3>
        <label class="flex items-center space-x-2">
          <input
            type="checkbox"
            bind:checked={form.consent}
            class="w-5 h-5 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500"
          />
          <span class="text-gray-700 text-sm">I agree to be contacted by Supernovae Hub</span>
        </label>
      </div>

      <!-- Submit Button -->
      <div class="pt-6">
        <button
          type="submit"
          disabled={submitting}
          class="w-full bg-indigo-600 text-white py-3 rounded-xl font-semibold text-lg shadow-md hover:bg-indigo-700 transform hover:-translate-y-1 transition-all duration-300 focus:ring-4 focus:ring-indigo-300 disabled:opacity-60 disabled:cursor-not-allowed"
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
        <p class="text-gray-600 mb-6">
          Thank you for reaching out. We’ll contact you shortly.
        </p>

        <a
          href="/solutions/telecommunications"
          class="inline-block bg-green-500 text-white px-6 py-2 rounded-lg font-medium hover:bg-green-600 transition-all"
        >
          Close
        </a>
      </div>
    </div>
  {/if}
</section>
