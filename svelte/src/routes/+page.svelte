<script lang="ts">
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  let submitting = false;
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

  async function submitForm() {
    if (!form.email || !form.fullname) {
      alert('Please provide your name and email.');
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
        const err = await res.text();
        alert('Submission failed: ' + err);
      }
    } catch (e) {
      alert('Network error.');
    } finally {
      submitting = false;
    }
  }
</script>

<svelte:head>
  <title>Telecommunications Network Design & Delivery | Supernovae Hub</title>
  <meta name="description" content="Private 4G/5G, DWDM transmission, FWA and AI-enabled optimisation. Book a telecommunications consultation."/>
</svelte:head>

<section class="prose max-w-4xl m-auto p-6">
  <h1>Telecommunications Network Design & Delivery</h1>
  <p>Fixed wireless access, private 4G/5G, DWDM transmission and AI-enabled optimisation. We design and deliver reliable, scalable networks.</p>

  <h2>Services</h2>
  <ul>
    <li>Fixed wireless access (FWA)</li>
    <li>Private 4G / 5G</li>
    <li>Fibre & DWDM transmission</li>
    <li>AI-enabled optimisation and monitoring</li>
  </ul>

  <h2>Book a Telecommunications Consultation</h2>
  <form on:submit|preventDefault={submitForm} class="space-y-3">
    <div>
      <label>Full name *</label>
      <input bind:value={form.fullname} required />
    </div>

    <div>
      <label>Company</label>
      <input bind:value={form.company} />
    </div>

    <div>
      <label>Email *</label>
      <input type="email" bind:value={form.email} required />
    </div>

    <div>
      <label>Phone</label>
      <input bind:value={form.phone} />
    </div>

    <div>
      <label>Project type</label>
      <select bind:value={form.projectType}>
        <option value="">Select</option>
        <option>Private 4G/5G</option>
        <option>DWDM / Fibre</option>
        <option>FWA</option>
        <option>Other</option>
      </select>
    </div>

    <div>
      <label>Brief description</label>
      <textarea bind:value={form.message}></textarea>
    </div>

    <div>
      <label>Preferred contact time</label>
      <input bind:value={form.preferredContact} />
    </div>

    <div>
      <label><input type="checkbox" bind:checked={form.consent} /> I agree to be contacted</label>
    </div>

    <div>
      <button type="submit" disabled={submitting}>Submit</button>
    </div>
  </form>
</section>
