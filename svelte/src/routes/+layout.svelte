<script lang="ts">
	import "../app.css"
	import favicon from "$lib/assets/favicon.svg"

	import { isLoading } from "svelte-i18n"
	import "$lib/i18n"
	let { children } = $props()
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
</svelte:head>

{#if !$isLoading}
{@render children?.()}
{/if}