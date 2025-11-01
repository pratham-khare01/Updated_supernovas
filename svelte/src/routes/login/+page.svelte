<script lang="ts">
	import { _ } from "svelte-i18n"
	import logo from "$lib/images/logo.png"
	let email = $state("")
	let password = $state("")
</script>

<section class="page-background relative h-full overflow-hidden">
	<div class="absolute flex w-full justify-center">
		<div class="z-10 flex px-6 py-4">
			<img src={logo} alt="Logo" class="h-10 md:h-12" />
		</div>
	</div>
	<main
		class="relative z-10 mx-auto flex h-screen max-w-screen-xl items-center justify-center px-4"
	>
		<form class="bg-base-100/90 w-full max-w-md rounded-2xl shadow-2xl backdrop-blur-sm">
			<div class="card-body">
				<h1 class="card-title mb-2 justify-center text-2xl">{$_("login.welcome_to_supernovae")}</h1>

				<label for="email" class="label">{$_("login.email_address")}</label>
				<input
					id="email"
					type="email"
					name="email"
					bind:value={email}
					placeholder={$_("login.email_address")}
					class="input input-bordered w-full"
				/>

				<label for="password" class="label">{$_("login.password")}</label>
				<input
					id="password"
					type="password"
					name="password"
					bind:value={password}
					placeholder={$_("login.password")}
					class="input input-bordered w-full"
				/>

				<div class="card-actions mt-4">
					<button type="submit" class="btn btn-primary w-full">{$_("login.login")}</button>
				</div>
			</div>
		</form>
	</main>
</section>

<style>
	.page-background {
		background-image: url("$lib/images/background.png");
		background-size: cover;
		background-position: center;
	}
</style>
